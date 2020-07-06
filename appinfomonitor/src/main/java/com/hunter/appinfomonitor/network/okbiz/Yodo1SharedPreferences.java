package com.hunter.appinfomonitor.network.okbiz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author yodo1
 */
public class Yodo1SharedPreferences {
    private final static String FILE_NAME = "sp_yodo1games";

    public static void put(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, float value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public static void put(Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, null);
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static float getFloat(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getFloat(key, 0);
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong(key, 0);
    }


    /**
     * ota downloader.
     */
    public static boolean hasDownloadTask(Context context, int downloadTaskId) {
        String fetchTask = getString(context, "FetchTask");
        if (fetchTask == null) {
            return false;
        }
        String id = String.valueOf(downloadTaskId);
        return fetchTask.contains(id);
    }

    public static void addDownloadTask(Context context, int downloadTaskId) {
        String fetchTask = getString(context, "FetchTask");
        if (fetchTask == null) {
            put(context, "FetchTask", String.valueOf(downloadTaskId));
        } else {
            put(context, "FetchTask", fetchTask + downloadTaskId);
        }
    }

    public static void removeDownloadTask(Context context, int downloadTaskId) {
        String fetchTask = getString(context, "FetchTask");
        if (fetchTask != null) {
            String id = String.valueOf(downloadTaskId);
            fetchTask = fetchTask.replace(id, "");
            put(context, "FetchTask", fetchTask);
        }
    }
}
