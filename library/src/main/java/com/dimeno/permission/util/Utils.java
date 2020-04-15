package com.dimeno.permission.util;

import android.content.Context;
import android.content.ContextWrapper;

import androidx.fragment.app.FragmentActivity;

/**
 * Utils
 * Created by wangzhen on 2020/4/15.
 */
public class Utils {
    /**
     * find fragment activity by context
     *
     * @param context context
     * @return fragment activity
     */
    public static FragmentActivity getFragmentActivity(Context context) {
        while (context instanceof ContextWrapper) {
            if (context instanceof FragmentActivity) {
                return (FragmentActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
}
