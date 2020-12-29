package com.example.log;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class GalobalApp extends Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        LogUtils.e("attachBaseContext:  :  " + this);
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        LogUtils.e("onConfigurationChanged:  " + this);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        LogUtils.e("onCreate:  " + this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityCreated:  " + activity);
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityCreated:  " + activity);
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityStarted:  " + activity);
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityPaused:  " + activity);
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityStopped:  " + activity);
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

                LogUtils.e("ActivityLifecycleCallbacks onActivitySaveInstanceState:  " + activity);
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

                LogUtils.e("ActivityLifecycleCallbacks onActivityDestroyed:  " + activity);
            }
        });
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

        LogUtils.e("onLowMemory:  " + this);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();

        LogUtils.e("onTerminate:  " + this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

        LogUtils.e("onTrimMemory:  " + this);
    }
}

