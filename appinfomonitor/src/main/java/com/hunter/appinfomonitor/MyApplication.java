package com.hunter.appinfomonitor;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

public class MyApplication extends Application {

    public static MyApplication mApplication;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mApplication = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        UMConfigure.init(this, "5cf7a1aa570df38dee000b5b", "yodo1", UMConfigure.DEVICE_TYPE_PHONE, null);
        UMConfigure.setLogEnabled(LogUtils.isLog());

        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.LEGACY_AUTO);

    }
}
