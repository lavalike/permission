# dimeno-permission
> 动态权限申请

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![](https://jitpack.io/v/dimeno-tech/dimeno-permission.svg)](https://jitpack.io/#dimeno-tech/dimeno-permission)

#### 支持方法
方法一

```java
PermissionManager.request(Fragment fragment, PermissionCallback callback, String... permissions)
```

方法二

```java
PermissionManager.request(Context context, PermissionCallback callback, String... permissions)
```

方法三

```java
PermissionManager.request(FragmentActivity activity, PermissionCallback callback, String... permissions)
``` 

#### 接口说明
```java
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
```

#### 使用方式
```java
PermissionManager.request(this, new PermissionCallback() {
    @Override
    public void onGrant(String[] permissions) {
        
    }

    @Override
    public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
        
    }
    
    @Override
    public void onNotDeclared(String[] permissions) {
        
    }
}, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
```

为简便开发，建议使用抽象接口实现类**AbsPermissionCallback**