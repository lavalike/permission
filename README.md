# dimeno-permission
> 动态权限申请

[![Platform](https://img.shields.io/badge/Platform-Android-00CC00.svg?style=flat)](https://www.android.com)
[![](https://jitpack.io/v/dimeno-tech/dimeno-permission.svg)](https://jitpack.io/#dimeno-tech/dimeno-permission)

### 依赖导入

项目根目录

``` gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

模块目录

``` gradle
dependencies {
	implementation 'com.github.dimeno-tech:dimeno-permission:0.0.2'
}
```

### 接口说明

为简便接口，可使用抽象接口实现类 **AbsPermissionCallback**

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

### 代码示例

```java
public final class PermissionManager {
    public static void request(Fragment fragment, PermissionCallback callback, String... permissions);
    public static void request(Context context, PermissionCallback callback, String... permissions);
    public static void request(FragmentActivity activity, PermissionCallback callback, String... permissions);
    public static Intent getSettingIntent(Context context);
}
```

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