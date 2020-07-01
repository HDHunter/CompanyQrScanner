package com.hunter.appinfomonitor;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

    public static MyApplication mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApplication = this;
    }
}
