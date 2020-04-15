## dimeno-permission
动态权限申请

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


#### 使用方式
```java
PermissionManager.request(this, new PermissionCallback() {
    @Override
    public void onGrant(String[] permissions) {
        
    }

    @Override
    public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
        
    }
}, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
```