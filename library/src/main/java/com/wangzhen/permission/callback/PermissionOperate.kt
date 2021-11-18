package com.wangzhen.permission.callback

/**
 * PermissionOperate
 * Created by wangzhen on 2020/4/15.
 */
interface PermissionOperate {
    fun exeRequestPermissions(permissions: Array<String>, callback: PermissionCallback?)
}