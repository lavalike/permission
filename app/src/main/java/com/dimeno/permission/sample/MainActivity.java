package com.dimeno.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dimeno.permission.PermissionManager;
import com.dimeno.permission.callback.PermissionCallback;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addFragment();
    }

    private void addFragment() {
        getSupportFragmentManager().beginTransaction().add(R.id.container, new SampleFragment()).commitAllowingStateLoss();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_storage:
                requestStorage();
                break;
            case R.id.btn_sms:
                requestSMS();
                break;
        }
    }

    private void requestSMS() {
        PermissionManager.request(this, new PermissionCallback() {
            @Override
            public void onGrant(String[] permissions) {
                for (String permission : permissions) {
                    Log.e("TAG", "onGrant permissions -> " + permission);
                }
                Toast.makeText(MainActivity.this, "短信权限已全部授予", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
                for (String permission : deniedPermissions) {
                    Log.e("TAG", "onDeny deniedPermissions -> " + permission);
                }
                for (String permission : neverAskPermissions) {
                    Log.e("TAG", "onDeny neverAskPermissions -> " + permission);
                }
                Toast.makeText(MainActivity.this, "短信权限部分或全部被拒绝", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS);
    }

    private void requestStorage() {
        PermissionManager.request(this, new PermissionCallback() {
            @Override
            public void onGrant(String[] permissions) {
                for (String permission : permissions) {
                    Log.e("TAG", "onGrant permissions -> " + permission);
                }
                Toast.makeText(MainActivity.this, "存储权限已全部授予", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
                for (String permission : deniedPermissions) {
                    Log.e("TAG", "onDeny deniedPermissions -> " + permission);
                }
                for (String permission : neverAskPermissions) {
                    Log.e("TAG", "onDeny neverAskPermissions -> " + permission);
                }
                Toast.makeText(MainActivity.this, "存储权限部分或全部被拒绝", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
}
