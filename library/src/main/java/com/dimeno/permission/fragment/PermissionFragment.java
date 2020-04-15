package com.dimeno.permission.fragment;

import android.content.pm.PackageManager;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.dimeno.permission.callback.PermissionCallback;
import com.dimeno.permission.callback.PermissionOperate;

import java.util.ArrayList;
import java.util.List;

/**
 * internal permission fragment
 * Created by wangzhen on 2020/4/15.
 */
public class PermissionFragment extends Fragment implements PermissionOperate {
    private static SparseArray<PermissionCallback> mRequestCaches = new SparseArray<>();
    private static List<String> mGrantedPermissions = new ArrayList<>();
    private static List<String> mDeniedPermissions = new ArrayList<>();
    private static List<String> mNeverAskPermissions = new ArrayList<>();
    private PermissionCallback mCallback;

    @Override
    public void exeRequestPermissions(final String[] permissions, PermissionCallback callback, final int requestCode) {
        mGrantedPermissions.clear();
        mDeniedPermissions.clear();
        mNeverAskPermissions.clear();
        mRequestCaches.put(requestCode, callback);
        if (getHost() != null) {
            requestPermissions(permissions, requestCode);
        } else {
            getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        requestPermissions(permissions, requestCode);
                    }
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mCallback = mRequestCaches.get(requestCode);
        if (mCallback != null) {
            mRequestCaches.remove(requestCode);
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    mGrantedPermissions.add(permission);
                } else {
                    if (!shouldShowRequestPermissionRationale(permission)) {
                        //permission never ask
                        mNeverAskPermissions.add(permission);
                    } else {
                        //permission denied
                        mDeniedPermissions.add(permission);
                    }
                }
            }
            dispatch();
        }
    }

    /**
     * dispatch permission callback
     */
    private void dispatch() {
        if (mDeniedPermissions.isEmpty() && mNeverAskPermissions.isEmpty()) {
            mCallback.onGrant(mGrantedPermissions.toArray(new String[0]));
        } else {
            mCallback.onDeny(mDeniedPermissions.toArray(new String[0]), mNeverAskPermissions.toArray(new String[0]));
        }
    }
}
