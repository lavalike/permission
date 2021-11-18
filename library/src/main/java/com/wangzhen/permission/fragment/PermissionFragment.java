package com.wangzhen.permission.fragment;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.wangzhen.permission.callback.PermissionCallback;
import com.wangzhen.permission.callback.PermissionOperate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * internal permission fragment
 * Created by wangzhen on 2020/4/15.
 */
public class PermissionFragment extends Fragment implements PermissionOperate {
    private static final List<String> mGrantedPermissions = new ArrayList<>();
    private static final List<String> mDeniedPermissions = new ArrayList<>();
    private static final List<String> mNeverAskPermissions = new ArrayList<>();
    private static final List<String> mNotDeclaredPermissions = new ArrayList<>();

    private Set<String> mManifestPermissions;
    private PermissionCallback mCallback;

    @Override
    public void exeRequestPermissions(final String[] permissions, PermissionCallback callback) {
        mGrantedPermissions.clear();
        mDeniedPermissions.clear();
        mNeverAskPermissions.clear();
        mNotDeclaredPermissions.clear();
        mCallback = callback;
        if (getHost() != null) {
            launcher.launch(permissions);
        } else {
            getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        getLifecycle().removeObserver(this);
                        launcher.launch(permissions);
                    }
                }
            });
        }
    }

    private final ActivityResultLauncher<String[]> launcher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                String permission = entry.getKey();
                if (entry.getValue()) {
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
    });

    /**
     * dispatch permission callback
     */
    private void dispatch() {
        if (mCallback == null)
            return;
        if (mDeniedPermissions.isEmpty() && mNeverAskPermissions.isEmpty() && mNotDeclaredPermissions.isEmpty()) {
            mCallback.onGrant(mGrantedPermissions.toArray(new String[0]));
        } else {
            if (!mNotDeclaredPermissions.isEmpty()) {
                mCallback.onNotDeclared(mNotDeclaredPermissions.toArray(new String[0]));
            } else {
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
