package com.hunter.appinfomonitor;

import android.util.Log;

public class LogUtils {

    private static boolean isdebug = true;


    public static boolean isLog() {
        return isdebug;
    }

    public static void e(String tag, String message) {
        if (isLog()) {
            Log.e(tag, message);
        }
    }
}
