package com.hunter.appinfomonitor.ui;

import android.content.Context;

import com.google.gson.Gson;
import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.floatui.SPHelper;

public class JsonUtils {

    private static Gson gson = new Gson();


    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        return gson.fromJson(jsonString, tClass);
    }

    public static void saveJson(String key, Object db, Context context) {
        String s = gson.toJson(db);
        SPHelper.saveString(key, s, context);
        LogUtils.e("saveJson", s);
    }
}
