package com.wangzhen.permission.sample

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wangzhen.permission.PermissionManager.request
import com.wangzhen.permission.callback.AbsPermissionCallback

/**
 * MainActivity
 * Created by wangzhen on 2021/11/18.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragment()
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction().add(R.id.container, SampleFragment())
            .commitAllowingStateLoss()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_storage -> requestStorage()
            R.id.btn_sms -> requestSMS()
        }
    }

    private fun requestSMS() {
        request(
            this,
            object : AbsPermissionCallback() {
                override fun onGrant(permissions: Array<String>) {
                    for (permission in permissions) {
                        Log.e("TAG", "onGrant permissions -> $permission")
                    }
                    Toast.makeText(this@MainActivity, "短信权限已全部授予", Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this@MainActivity, "短信权限部分或全部被拒绝", Toast.LENGTH_SHORT).show()
                }
            },
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        )
    }

    private fun requestStorage() {
        request(this, object : AbsPermissionCallback() {
            override fun onGrant(permissions: Array<String>) {
                for (permission in permissions) {
                    Log.e("TAG", "onGrant permissions -> $permission")
                }
                Toast.makeText(this@MainActivity, "存储权限已全部授予", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(this@MainActivity, "存储权限部分或全部被拒绝", Toast.LENGTH_SHORT).show()
            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }
}