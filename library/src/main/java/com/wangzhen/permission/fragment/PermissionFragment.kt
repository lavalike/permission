package com.wangzhen.permission.fragment

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.wangzhen.permission.callback.PermissionCallback
import com.wangzhen.permission.callback.PermissionOperate
import java.util.*

/**
 * internal permission fragment
 * Created by wangzhen on 2020/4/15.
 */
internal class PermissionFragment : Fragment(), PermissionOperate {
    private var mCallback: PermissionCallback? = null

    override fun exeRequestPermissions(permissions: Array<String>, callback: PermissionCallback?) {
        mGrantedPermissions.clear()
        mDeniedPermissions.clear()
        mNeverAskPermissions.clear()
        mNotDeclaredPermissions.clear()
        mCallback = callback
        if (host != null) {
            launcher.launch(permissions)
        } else {
            lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_CREATE) {
                        lifecycle.removeObserver(this)
                        launcher.launch(permissions)
                    }
                }
            })
        }
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            for ((permission, value) in result) {
                if (value) {
                    mGrantedPermissions.add(permission)
                } else {
                    if (containsManifest(permission)) {
                        if (!shouldShowRequestPermissionRationale(permission)) {
                            //permission never ask
                            mNeverAskPermissions.add(permission)
                        } else {
                            //permission denied
                            mDeniedPermissions.add(permission)
                        }
                    } else {
                        //permission not declared
                        mNotDeclaredPermissions.add(permission)
                    }
                }
            }
            dispatch()
        }

    /**
     * dispatch permission callback
     */
    private fun dispatch() {
        if (mDeniedPermissions.isEmpty() && mNeverAskPermissions.isEmpty() && mNotDeclaredPermissions.isEmpty()) {
            mCallback?.onGrant(mGrantedPermissions.toTypedArray())
        } else {
            if (mNotDeclaredPermissions.isNotEmpty()) {
                mCallback?.onNotDeclared(mNotDeclaredPermissions.toTypedArray())
            } else {
                mCallback?.onDeny(
                    mDeniedPermissions.toTypedArray(),
                    mNeverAskPermissions.toTypedArray()
                )
            }
        }
    }

    /**
     * whether permission exists in manifest or not
     *
     * @param permission permission
     * @return result
     */
    private fun containsManifest(permission: String): Boolean {
        return manifestPermissions().contains(permission)
    }

    /**
     * get all permissions in manifest
     *
     * @return permissions set
     */
    private fun manifestPermissions(): Set<String> {
        var manifestPermissions: Set<String>? = null
        var packageInfo: PackageInfo? = null
        try {
            context?.let { ctx ->
                packageInfo = ctx.applicationContext?.packageManager?.getPackageInfo(
                    ctx.packageName,
                    PackageManager.GET_PERMISSIONS
                )
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        packageInfo?.let { info ->
            val permissions = info.requestedPermissions
            if (permissions != null && permissions.isNotEmpty()) {
                manifestPermissions = HashSet(listOf(*permissions))
            }
        }
        return manifestPermissions ?: HashSet(0)
    }

    companion object {
        private val mGrantedPermissions: MutableList<String> = ArrayList()
        private val mDeniedPermissions: MutableList<String> = ArrayList()
        private val mNeverAskPermissions: MutableList<String> = ArrayList()
        private val mNotDeclaredPermissions: MutableList<String> = ArrayList()
    }
}