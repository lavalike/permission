package com.wangzhen.permission.callback

/**
 * PermissionCallback
 * Created by wangzhen on 2020/4/15.
 */
interface PermissionCallback {
    /**
     * all permissions are granted
     *
     * @param permissions permissions
     */
    fun onGrant(permissions: Array<String>)

    /**
     * permissions are denied or refused
     *
     * @param deniedPermissions   denied permissions
     * @param neverAskPermissions refused permissions
     */
    fun onDeny(deniedPermissions: Array<String>, neverAskPermissions: Array<String>)

    /**
     * permissions not declared in manifest
     *
     * @param permissions permissions
     */
    fun onNotDeclared(permissions: Array<String>)
}