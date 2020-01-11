package com.hunter.appinfomonitor.floatui;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.hunter.appinfomonitor.BuildConfig;
import com.hunter.appinfomonitor.MainActivity;
import com.hunter.appinfomonitor.R;

import java.util.List;

/**
 * Created by Wen on 4/18/15.
 */
public class NotificationActionReceiver extends BroadcastReceiver {

    public static final int NOTIFICATION_ID = 11111;
    public static final String ACTION_NOTIFICATION_RECEIVER = BuildConfig.APPLICATION_ID + ".ACTION_NOTIFICATION_RECEIVER";
    public static final int ACTION_PAUSE = 0;
    public static final int ACTION_RESUME = 1;
    public static final int ACTION_STOP = 2;
    public static final String EXTRA_NOTIFICATION_ACTION = "command";

    @Override
    public void onReceive(Context context, Intent intent) {
        int command = intent.getIntExtra(EXTRA_NOTIFICATION_ACTION, -1);
        switch (command) {
            case ACTION_RESUME:
                showNotification(context, false);
                if (Build.VERSION.SDK_INT < 21) {
                    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> infos = am.getRunningTasks(1);
                    ComponentName ac = infos.get(0).topActivity;
                    String act = ac.getPackageName() + "\n" + ac.getClassName();
                    TasksWindow.show(context, act);
                } else {
                    TasksWindow.show(context, null);
                }
                break;
            case ACTION_PAUSE:
                showNotification(context, true);
                TasksWindow.dismiss(context);
                break;
            case ACTION_STOP:
            default:
                TasksWindow.dismiss(context);
                cancelNotification(context);
                break;
        }
    }

    public static Notification showNotification(Context context, boolean isPaused) {
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentTitle("运行的:" + context.getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("yodo1 app monitor")
                .setColor(0xFFe215e0)
                .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                .setOngoing(!isPaused);
        if (isPaused) {
            builder.addAction(R.mipmap.ic_noti_action_resume, "Resume", getPendingIntent(context, ACTION_RESUME));
        } else {
            builder.addAction(R.mipmap.ic_noti_action_pause, "Pause", getPendingIntent(context, ACTION_PAUSE));
        }
        builder.addAction(R.mipmap.ic_noti_action_stop, "Stop", getPendingIntent(context, ACTION_STOP)).setContentIntent(pIntent);
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification build = builder.build();
        nm.notify(NOTIFICATION_ID, build);
        return build;
    }

    public static PendingIntent getPendingIntent(Context context, int command) {
        Intent intent = new Intent(ACTION_NOTIFICATION_RECEIVER);
        intent.putExtra(EXTRA_NOTIFICATION_ACTION, command);
        return PendingIntent.getBroadcast(context, command, intent, 0);
    }

    public static void cancelNotification(Context context) {
        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_ID);
    }

}