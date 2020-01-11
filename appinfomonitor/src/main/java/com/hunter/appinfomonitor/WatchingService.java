package com.hunter.appinfomonitor;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.hunter.appinfomonitor.floatui.NotificationActionReceiver;
import com.hunter.appinfomonitor.floatui.TasksWindow;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WatchingService extends Service {

    private Handler mHandler = new Handler();
    private ActivityManager ams;
    private String text = null;
    private Timer timer;

    @Override
    public void onCreate() {
        super.onCreate();
        ams = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        startForeground(NotificationActionReceiver.NOTIFICATION_ID, NotificationActionReceiver.showNotification(getApplicationContext(), false));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(new RefreshTask(), 0, 500);
        }
        return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);
    }

    class RefreshTask extends TimerTask {

        @Override
        public void run() {
            List<RunningTaskInfo> rtis = ams.getRunningTasks(1);
            ComponentName name = rtis.get(0).topActivity;
            String act = name.getPackageName() + "\n" + name.getClassName();
            if (!act.equals(text)) {
                text = act;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        TasksWindow.show(WatchingService.this, text);
                    }
                });
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
        Context context = getApplicationContext();
        Intent inrest = new Intent(context, WatchingService.class);
        inrest.setPackage(getPackageName());
        PendingIntent intent = PendingIntent.getService(context, 1, inrest, PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmService = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, intent);
        super.onTaskRemoved(rootIntent);
    }

}