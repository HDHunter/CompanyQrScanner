package com.hunter.appinfomonitor;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public class BaseActvity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 不能遗漏
    }
}
