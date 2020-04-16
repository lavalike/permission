package com.dimeno.permission.sample;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dimeno.permission.PermissionManager;
import com.dimeno.permission.callback.AbsPermissionCallback;

/**
 * SampleFragment
 * Created by wangzhen on 2020/4/15.
 */
public class SampleFragment extends Fragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_sample, container, false);
        inflate.findViewById(R.id.btn_call).setOnClickListener(this);
        return inflate;
    }

    @Override
    public void onClick(View v) {
        PermissionManager.request(this, new AbsPermissionCallback() {
            @Override
            public void onGrant(String[] permissions) {
                for (String permission : permissions) {
                    Log.e("TAG", "onGrant permissions -> " + permission);
                }
                Toast.makeText(getContext(), "电话权限已全部授予", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDeny(String[] deniedPermissions, String[] neverAskPermissions) {
                for (String permission : deniedPermissions) {
                    Log.e("TAG", "onDeny deniedPermissions -> " + permission);
                }
                for (String permission : neverAskPermissions) {
                    Log.e("TAG", "onDeny neverAskPermissions -> " + permission);
                }
                Toast.makeText(getContext(), "电话权限部分或全部被拒绝", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNotDeclared(String[] permissions) {
                StringBuilder builder = new StringBuilder();
                for (String permission : permissions) {
                    if (builder.length() > 0) {
                        builder.append("、");
                    }
                    builder.append(permission);
                    Log.e("TAG", "onNotDeclared permissions -> " + permission);
                }
                Toast.makeText(getContext(), builder.toString() + "未在清单文件声明", Toast.LENGTH_SHORT).show();
            }
        }, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE);
    }
}
