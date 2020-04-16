package com.dimeno.permission.callback;

/**
 * abstract implementation of {@link PermissionCallback}
 * Created by wangzhen on 2020/4/16.
 */
public abstract class AbsPermissionCallback implements PermissionCallback {
    @Override
    public void onNotDeclared(String[] permissions) {

    }
}
