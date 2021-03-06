package com.hunter.appinfomonitor;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;

import com.hunter.appinfomonitor.floatui.NotificationActionReceiver;
import com.hunter.appinfomonitor.floatui.SPHelper;
import com.hunter.appinfomonitor.floatui.TasksWindow;
import com.hunter.appinfomonitor.floatui.WatchingAccessibilityService;
import com.hunter.appinfomonitor.ui.AppManager;

/**
 * @author HunterZhang
 */
@TargetApi(Build.VERSION_CODES.N)
public class AppShortcutsActivity extends BaseActvity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (WatchingAccessibilityService.getInstance() == null || !Settings.canDrawOverlays(this)) {
            SPHelper.setIsShowWindow(this, true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        boolean isShow = !SPHelper.isShowWindow(this);
        SPHelper.setIsShowWindow(this, isShow);
        if (!isShow) {
            TasksWindow.dismiss(this);
            NotificationActionReceiver.showNotification(this, true);
        } else {
            TasksWindow.show(this, getPackageName() + "\n" + getClass().getName());
            NotificationActionReceiver.showNotification(this, false);
        }
        sendBroadcast(new Intent(MainActivity.ACTION_STATE_CHANGED));
        finish();

        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
}
