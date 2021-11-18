package com.wangzhen.permission.util

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity

/**
 * Utils
 * Created by wangzhen on 2020/4/15.
 */
object Utils {
    /**
     * find fragment activity by context
     *
     * @param context context
     * @return fragment activity
     */
    @JvmStatic
    fun getFragmentActivity(context: Context): FragmentActivity? {
        var ctx = context
        while (ctx is ContextWrapper) {
            if (ctx is FragmentActivity) {
                return ctx
            }
            ctx = ctx.baseContext
        }
        return null
    }
}