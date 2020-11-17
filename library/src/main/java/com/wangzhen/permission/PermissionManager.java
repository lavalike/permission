package com.wangzhen.permission;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.wangzhen.permission.callback.PermissionCallback;
import com.wangzhen.permission.callback.PermissionOperate;
import com.wangzhen.permission.fragment.PermissionFragment;
import com.wangzhen.permission.util.Utils;

import static com.wangzhen.permission.common.Common.FRAGMENT_TAG;

/**
 * PermissionManager
 * Created by wangzhen on 2020/4/15.
 */
public final class PermissionManager {
    private static PermissionManager sInstance;
    private static int sCode;

    static {
        sInstance = new PermissionManager();
    }

    public static void request(Fragment fragment, PermissionCallback callback, String... permissions) {
        request(fragment.getActivity(), callback, permissions);
    }

    public static void request(Context context, PermissionCallback callback, String... permissions) {
        request(Utils.getFragmentActivity(context), callback, permissions);
    }

    public static void request(FragmentActivity activity, PermissionCallback callback, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            sInstance.requestPermission(activity, callback, permissions);
        } else {
            if (callback != null) {
                callback.onGrant(permissions);
            }
        }
    }

    /**
     * start permission request
     *
     * @param activity    activity
     * @param callback    callback
     * @param permissions permissions
     */
    private void requestPermission(FragmentActivity activity, PermissionCallback callback, String... permissions) {
        FragmentManager manager = activity.getSupportFragmentManager();
        Fragment tag = manager.findFragmentByTag(FRAGMENT_TAG);
        PermissionFragment fragment;
        if (tag instanceof PermissionOperate) {
            fragment = (PermissionFragment) tag;
        } else {
            fragment = new PermissionFragment();
            manager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss();
        }
        ((PermissionOperate) fragment).exeRequestPermissions(permissions, callback, obtainRequestCode());
    }

    /**
     * get intent to application details setting page
     *
     * @param context context
     * @return intent
     */
    public static Intent getSettingIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        return intent;
    }

    /**
     * obtain request code
     * {@link FragmentActivity#checkForValidRequestCode(int)}
     *
     * @return request code
     */
    private static int obtainRequestCode() {
        if ((sCode & 0xffff0000) != 0)
            sCode = 0;
        return sCode++;
    }
}
