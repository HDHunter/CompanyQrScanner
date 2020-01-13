package com.hunter.appinfomonitor.floatui;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {


    public static boolean getState(Context context) {
        SharedPreferences hunter = context.getSharedPreferences("Hunter", Context.MODE_PRIVATE);
        return hunter.getBoolean("state", false);
    }

    public static void setState(Context context, boolean b) {
        SharedPreferences hunter = context.getSharedPreferences("Hunter", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = hunter.edit();
        edit.putBoolean("state", b);
        edit.apply();
    }
}
