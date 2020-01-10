package com.hunter.appinfomonitor;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

/**
 * 浮动气泡的服务
 *
 * @author Hunter
 */
public class FloatServerice extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        FloatWindow.with(getApplicationContext()).setView(R.layout.float_pop).setWidth(80).setHeight(80).setX(100).setY(100).setDesktopShow(true).
//                setViewStateListener(this).setMoveType(MoveType.slide).setPermissionListener(this).build();
        log("FloatService    oncreate");
        refresh();
    }

    private void refresh() {
        ActivityManager ams = (ActivityManager) getApplication().getSystemService(Context.ACTIVITY_SERVICE);
        if (ams == null) return;
//        View view = FloatWindow.get().getView();
        TextView textView = null;// view.findViewById(R.id.applable);
        if (textView == null) return;
        textView.setText("无法获取。\n无法获取。");
        List<ActivityManager.RunningTaskInfo> runningTasks = ams.getRunningTasks(1);
        if (runningTasks == null || runningTasks.isEmpty()) return;
        ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0);
        ComponentName topActivity = runningTaskInfo.topActivity;
        String className = topActivity.getClassName();
        String packageName = topActivity.getPackageName();
        String shortClassName = topActivity.getShortClassName();
        StringBuilder sb = new StringBuilder();
        sb.append("包名:" + packageName);
        sb.append("\n");
        sb.append(className);
        textView.setText(sb.toString());
    }

    void log(String content) {
        Log.e("zzzzzzzzzz", content == null ? "null" : content);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
//        FloatWindow.destroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
//        FloatWindow.destroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("FloatService    onStartCommand");
        refresh();
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);

    }

}
