package com.dimeno.permission.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * internal permission fragment
 * Created by wangzhen on 2020/4/15.
 */
public class PermissionFragment extends Fragment implements PermissionOperate {
    private static SparseArray<PermissionCallback> mRequestCaches = new SparseArray<>();
    private static List<String> mGrantedPermissions = new ArrayList<>();
    private static List<String> mDeniedPermissions = new ArrayList<>();
    private static List<String> mNeverAskPermissions = new ArrayList<>();
    private static List<String> mNotDeclaredPermissions = new ArrayList<>();

    private Set<String> mManifestPermissions;
    private PermissionCallback mCallback;

    @Override
    public void exeRequestPermissions(final String[] permissions, PermissionCallback callback, final int requestCode) {
        mGrantedPermissions.clear();
        mDeniedPermissions.clear();
        mNeverAskPermissions.clear();
        mNotDeclaredPermissions.clear();
        mRequestCaches.put(requestCode, callback);
        if (getHost() != null) {
            requestPermissions(permissions, requestCode);
        } else {
            getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        getLifecycle().removeObserver(this);
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
                    if (containsManifest(permission)) {
                        if (!shouldShowRequestPermissionRationale(permission)) {
                            //permission never ask
                            mNeverAskPermissions.add(permission);
                        } else {
                            //permission denied
                            mDeniedPermissions.add(permission);
                        }
                    } else {
                        //permission not declared
                        mNotDeclaredPermissions.add(permission);
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
        if (mDeniedPermissions.isEmpty() && mNeverAskPermissions.isEmpty() && mNotDeclaredPermissions.isEmpty()) {
            mCallback.onGrant(mGrantedPermissions.toArray(new String[0]));
        } else {
            if (!mNotDeclaredPermissions.isEmpty()) {
                mCallback.onNotDeclared(mNotDeclaredPermissions.toArray(new String[0]));
            }
            if (!mDeniedPermissions.isEmpty() || !mNeverAskPermissions.isEmpty()) {
                mCallback.onDeny(mDeniedPermissions.toArray(new String[0]), mNeverAskPermissions.toArray(new String[0]));
            }
        }
    }

    /**
     * whether permission exists in manifest or not
     *
     * @param permission permission
     * @return result
     */
    private boolean containsManifest(String permission) {
        if (mManifestPermissions == null) {
            mManifestPermissions = getManifestPermissions();
        }
        return mManifestPermissions.contains(permission);
    }

    /**
     * get all permissions in manifest
     *
     * @return permissions set
     */
    private Set<String> getManifestPermissions() {
        Set<String> manifestPermissions = null;
        PackageInfo packageInfo = null;
        Context context = getContext();
        try {
            if (context != null) {
                context = context.getApplicationContext();
                packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null && permissions.length != 0) {
                manifestPermissions = new HashSet<>(Arrays.asList(permissions));
            }
        }

        return manifestPermissions != null ? manifestPermissions : new HashSet<String>(0);
    }
}
