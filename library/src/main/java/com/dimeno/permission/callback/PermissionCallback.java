package com.dimeno.permission.callback;

/**
 * PermissionCallback
 * Created by wangzhen on 2020/4/15.
 */
public interface PermissionCallback {
    void onGrant(String[] permissions);

    void onDeny(String[] deniedPermissions, String[] neverAskPermissions);
}
