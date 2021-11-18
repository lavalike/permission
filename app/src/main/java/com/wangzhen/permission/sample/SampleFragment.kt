package com.wangzhen.permission.sample

import android.Manifest
import com.wangzhen.permission.PermissionManager.request
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import com.wangzhen.permission.sample.R
import com.wangzhen.permission.PermissionManager
import com.wangzhen.permission.callback.AbsPermissionCallback
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.lang.StringBuilder

/**
 * SampleFragment
 * Created by wangzhen on 2020/4/15.
 */
class SampleFragment : Fragment(), View.OnClickListener {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val inflate = inflater.inflate(R.layout.fragment_sample, container, false)
        inflate.findViewById<View>(R.id.btn_call).setOnClickListener(this)
        return inflate
    }

    override fun onClick(v: View) {
        request(this, object : AbsPermissionCallback() {
            override fun onGrant(permissions: Array<String>) {
                for (permission in permissions) {
                    Log.e("TAG", "onGrant permissions -> $permission")
                }
                Toast.makeText(context, "电话权限已全部授予", Toast.LENGTH_SHORT).show()
            }

            override fun onDeny(
                deniedPermissions: Array<String>,
                neverAskPermissions: Array<String>
            ) {
                for (permission in deniedPermissions) {
                    Log.e("TAG", "onDeny deniedPermissions -> $permission")
                }
                for (permission in neverAskPermissions) {
                    Log.e("TAG", "onDeny neverAskPermissions -> $permission")
                }
                Toast.makeText(context, "电话权限部分或全部被拒绝", Toast.LENGTH_SHORT).show()
            }

            override fun onNotDeclared(permissions: Array<String>) {
                val builder = StringBuilder()
                for (permission in permissions) {
                    if (builder.isNotEmpty()) {
                        builder.append("、")
                    }
                    builder.append(permission)
                    Log.e("TAG", "onNotDeclared permissions -> $permission")
                }
                Toast.makeText(context, builder.toString() + "未在清单文件声明", Toast.LENGTH_SHORT).show()
            }
        }, Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE)
    }
}