package com.dimeno.permission.callback;

/**
 * PermissionCallback
 * Created by wangzhen on 2020/4/15.
 */
public interface PermissionCallback {
    /**
     * all permissions are granted
     *
     * @param permissions permissions
     */
    void onGrant(String[] permissions);

    /**
     * permissions are denied or refused
     *
     * @param deniedPermissions   denied permissions
     * @param neverAskPermissions refused permissions
     */
    void onDeny(String[] deniedPermissions, String[] neverAskPermissions);

    /**
     * permissions not declared in manifest
     *
     * @param permissions permissions
     */
    void onNotDeclared(String[] permissions);
}
