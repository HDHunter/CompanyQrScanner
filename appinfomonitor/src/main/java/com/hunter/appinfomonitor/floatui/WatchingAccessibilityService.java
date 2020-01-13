package com.hunter.appinfomonitor.floatui;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by Wen on 1/14/15.
 */
public class WatchingAccessibilityService extends AccessibilityService {


    private static WatchingAccessibilityService sInstance;

    @SuppressLint("NewApi")
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (SPUtils.getState(this)) {
            TasksWindow.show(this, event.getPackageName() + "\n" + event.getClassName());
        } else {
            TasksWindow.dismiss(this);
        }
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    protected void onServiceConnected() {
        sInstance = this;
        NotificationActionReceiver.showNotification(this, false);
        super.onServiceConnected();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        sInstance = null;
        TasksWindow.dismiss(this);
        NotificationActionReceiver.cancelNotification(this);
        return super.onUnbind(intent);
    }

}