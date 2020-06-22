package com.hunter.appinfomonitor.ui;

import com.google.gson.Gson;

public class JsonUtils {

    private static Gson gson = new Gson();


    public static <T> T fromJson(String jsonString, Class<T> tClass) {
        return gson.fromJson(jsonString, tClass);
    }

}
