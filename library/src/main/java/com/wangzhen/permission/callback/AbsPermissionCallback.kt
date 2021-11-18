package com.wangzhen.permission.callback

/**
 * abstract implementation of [PermissionCallback]
 * Created by wangzhen on 2020/4/16.
 */
abstract class AbsPermissionCallback : PermissionCallback {
    override fun onNotDeclared(permissions: Array<String>) {}
}