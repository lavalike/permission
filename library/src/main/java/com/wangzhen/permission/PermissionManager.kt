package com.wangzhen.permission

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.wangzhen.permission.callback.PermissionCallback
import com.wangzhen.permission.callback.PermissionOperate
import com.wangzhen.permission.common.Common.FRAGMENT_TAG
import com.wangzhen.permission.fragment.PermissionFragment
import com.wangzhen.permission.util.Utils.getFragmentActivity

/**
 * PermissionManager
 * Created by wangzhen on 2020/4/15.
 */
object PermissionManager {

    @JvmStatic
    fun request(
        fragment: Fragment?,
        callback: PermissionCallback?,
        vararg permissions: String
    ) {
        request(fragment?.activity, callback, *permissions)
    }

    @JvmStatic
    fun request(context: Context?, callback: PermissionCallback?, vararg permissions: String) {
        context?.let { ctx ->
            request(
                getFragmentActivity(ctx) as FragmentActivity, callback, *permissions
            )
        }
    }

    @JvmStatic
    fun request(
        activity: FragmentActivity,
        callback: PermissionCallback?,
        vararg permissions: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermission(activity, callback, *permissions)
        } else {
            callback?.onGrant(arrayOf(*permissions))
        }
    }

    /**
     * get intent to application details setting page
     *
     * @param context context
     * @return intent
     */
    @JvmStatic
    fun getSettingIntent(context: Context): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:" + context.packageName)
        return intent
    }

    /**
     * start permission request
     *
     * @param activity    activity
     * @param callback    callback
     * @param permissions permissions
     */
    private fun requestPermission(
        activity: FragmentActivity,
        callback: PermissionCallback?,
        vararg permissions: String
    ) {
        val manager = activity.supportFragmentManager
        val tag = manager.findFragmentByTag(FRAGMENT_TAG)
        val fragment: PermissionFragment
        if (tag is PermissionOperate) {
            fragment = tag as PermissionFragment
        } else {
            fragment = PermissionFragment()
            manager.beginTransaction().add(fragment, FRAGMENT_TAG).commitAllowingStateLoss()
        }
        (fragment as PermissionOperate).exeRequestPermissions(arrayOf(*permissions), callback)
    }
}