package com.hunter.bjhealthcode;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yodo1
 */
public class YSharedPreferences {

    private final static String FILE_NAME = "sp_bj";
    private static Editor edt;
    private static SharedPreferences sharedPreferences;
    /**
     * 线程池，切换子线程使用
     */
    private static ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * 初始化操作，创建sharedPreferences edt对象，仅此一次
     * 注意：初始化必须在程序最开始的部分立刻执行，避免提前调用put get方法导致异常
     */
    public static void init(Context context) {
        if (sharedPreferences != null && edt != null) {
            return;
        }
        sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        edt = sharedPreferences.edit();
        edt.apply();
    }

    /**
     * 批量储存数据，多次put，一次apply
     * 只接受string类型的key和value，储存时注意转换成string类型，取值时自行解析数据
     */
    public static void put(Context context, final HashMap<String, String> data) {
        if (data == null || data.size() == 0) {
            return;
        }
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (String key : data.keySet()) {
                        edt.putString(key, data.get(key));
                    }
                    edt.apply();
                }
            });
        } else {
            for (String key : data.keySet()) {
                edt.putString(key, data.get(key));
            }
            edt.apply();
        }
    }

    /**
     * 批量储存数据，多次put，一次apply
     * 只接受string类型的key和value，储存时注意转换成string类型，取值时自行解析数据
     */
    public static void put(Context context, final HashMap<String, String> data, final Yodo1SharedPreferencesCallback callback) {
        if (data == null || data.size() == 0) {
            return;
        }
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    for (String key : data.keySet()) {
                        edt.putString(key, data.get(key));
                    }
                    edt.apply();
                    if (callback != null) {
                        callback.ready();
                    }
                }
            });
        } else {
            for (String key : data.keySet()) {
                edt.putString(key, data.get(key));
            }
            edt.apply();
        }
    }

    /**
     * 单个储存数据，不检测key和values，自行注意
     */
    public static void put(Context context, final String key, final String value) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putString(key, value);
                    edt.apply();
                }
            });
        } else {
            edt.putString(key, value);
            edt.apply();
        }
    }

    /**
     * 单个储存数据，不检测key和values，自行注意
     */
    public static void put(Context context, final String key, final String value, final Yodo1SharedPreferencesCallback callback) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putString(key, value);
                    edt.apply();
                    /*
                     * 主线程进来的，内部开子线程储存数据的，会比直接子线程慢一点，所以需要对外回调储存完成，以便获取数据时能获取到有效值
                     */
                    if (callback != null) {
                        callback.ready();
                    }
                }
            });
        } else {
            /*
             * 子线程进来的储存数据会实时获取到 不需要对外回调数据储存已完成
             */
            edt.putString(key, value);
            edt.apply();
        }
    }

    public static void put(Context context, final String key, final boolean value) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putBoolean(key, value);
                    edt.apply();
                }
            });
        } else {
            edt.putBoolean(key, value);
            edt.apply();
        }

    }

    public static void put(Context context, final String key, final int value) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putInt(key, value);
                    edt.apply();
                }
            });
        } else {
            edt.putInt(key, value);
            edt.apply();
        }

    }

    public static void put(Context context, final String key, final float value) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putFloat(key, value);
                    edt.apply();
                }
            });
        } else {
            edt.putFloat(key, value);
            edt.apply();
        }

    }

    public static void put(Context context, final String key, final long value) {
        init(context);
        if (Looper.myLooper() == Looper.getMainLooper()) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    edt.putLong(key, value);
                    edt.apply();
                }
            });
        } else {
            edt.putLong(key, value);
            edt.apply();
        }

    }

    public static String getString(Context context, String key) {
        init(context);
        return sharedPreferences.getString(key, "");
    }

    public static int getInt(Context context, String key) {
        init(context);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getBoolean(Context context, String key) {
        init(context);
        return sharedPreferences.getBoolean(key, false);
    }

    public static float getFloat(Context context, String key) {
        init(context);
        return sharedPreferences.getFloat(key, 0);
    }

    public static long getLong(Context context, String key) {
        init(context);
        return sharedPreferences.getLong(key, 0);
    }

    public interface Yodo1SharedPreferencesCallback {
        /**
         * 储存操作是否已完成回调
         */
        void ready();
    }

}
