package com.dimeno.permission.callback;

/**
 * PermissionOperate
 * Created by wangzhen on 2020/4/15.
 */
public interface PermissionOperate {
    void exeRequestPermissions(String[] permissions, PermissionCallback callback, int requestCode);
}
