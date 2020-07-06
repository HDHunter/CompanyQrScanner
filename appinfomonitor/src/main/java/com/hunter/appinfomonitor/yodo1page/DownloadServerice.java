package com.hunter.appinfomonitor.yodo1page;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.MainActivity;
import com.hunter.appinfomonitor.MyApplication;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;

import okhttp3.OkHttpClient;

public class DownloadServerice extends Service {

    private static DownloadServerice mInstance;
    private Fetch fetch;

    public static DownloadServerice getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadServerice();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance();
        String CHANNEL_ID = getPackageName() + ".downloader";
        PendingIntent intent = PendingIntent.getActivity(this, 1000, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "downloader", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        builder.setContentTitle("Yodo1 AppInfo OtaDownloader");
        builder.setContentText("");
        builder.setWhen(System.currentTimeMillis());
        builder.setTicker("OtaDownloader");
        builder.setContentIntent(intent);
        Notification build = builder.build();
        startForeground(12345, build);
    }

    public DownloadServerice() {
        super();
        if (mInstance == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
            FetchConfiguration fetchConfiguration = new FetchConfiguration.Builder(MyApplication.mApplication)
                    .setDownloadConcurrentLimit(5)
                    .setAutoRetryMaxAttempts(2)
                    .setHttpDownloader(new OkHttpDownloader(okHttpClient))
                    .build();

            fetch = Fetch.Impl.getInstance(fetchConfiguration);
            fetch.enableLogging(LogUtils.isLog());

            mInstance = this;
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public Fetch getFetch() {
        return fetch;
    }


    @Override
    public void onDestroy() {
        getFetch().pauseAll();
        getFetch().close();
        super.onDestroy();
        LogUtils.e("yodo1", "downloader onDestory");
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        getFetch().pauseAll();
        getFetch().close();
        super.onTaskRemoved(rootIntent);
        LogUtils.e("yodo1", "downloader onTaskRemoved");
    }
}
