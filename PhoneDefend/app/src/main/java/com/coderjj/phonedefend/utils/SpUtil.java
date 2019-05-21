package com.coderjj.phonedefend.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2019/5/9.
 */

public class SpUtil {

    private static SharedPreferences sp;

    public static void putBoolean(Context ctx, String key, boolean value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
    public static void putString(Context ctx, String key, String value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    public static String getString(Context ctx, String key, String defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }
    public static void putInt(Context ctx, String key, int value) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(Context ctx, String key, int defValue) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

}
