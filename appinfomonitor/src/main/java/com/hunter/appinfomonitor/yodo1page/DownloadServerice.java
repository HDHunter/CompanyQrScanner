package com.hunter.appinfomonitor.yodo1page;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.MainActivity;
import com.hunter.appinfomonitor.MyApplication;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.network.okbizfile.FileUtils;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.Fetch;
import com.tonyodev.fetch2.FetchConfiguration;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Extras;
import com.tonyodev.fetch2okhttp.OkHttpDownloader;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;

public class DownloadServerice extends Service implements FetchListener {

    private static DownloadServerice mInstance;
    private Fetch fetch;
    /**
     * key:downloadId , value: progress.
     */
    private static HashMap<Integer, Integer> mProgressStore = new HashMap<>();

    @Override
    public void onAdded(@NotNull Download download) {

    }

    @Override
    public void onCancelled(@NotNull Download download) {

    }

    @Override
    public void onCompleted(@NotNull Download download) {
        Yodo1SharedPreferences.removeDownloadTask(this, download.getId());
        DownloadServerice.getDownloadingList().remove(download.getId());

        Extras extras = download.getExtras();
        int apkid = extras.getInt("apkid", 0);
        int obbid = extras.getInt("obbid", 0);
        if (obbid != 0) {
            if (download.getFile().endsWith("obb")) {
                boolean hasDownloaded = Yodo1SharedPreferences.hasDownloadTask(this, apkid);
                if (!hasDownloaded) {
                    installApp(this, download);
                } else {
                    LogUtils.e("installApp", "obb 下载完成，apk 正在下载");
                    return;
                }
            } else {
                boolean hasDownloaded = Yodo1SharedPreferences.hasDownloadTask(this, obbid);
                if (!hasDownloaded) {
                    installApp(this, download);
                } else {
                    LogUtils.e("installApp", "apk 下载完成，obb 正在下载");
                    return;
                }
            }
        } else {
            installApp(this, download);
        }
    }

    public static void installApp(Context context, Download fileUri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            boolean haveInstallPermission = AppManager.getAppManager().currentActivity().getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                Uri packageURI = Uri.parse("package:" + context.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                AppManager.getAppManager().currentActivity().startActivityForResult(intent, 11111);
                return;
            }
        }
        if (fileUri.getFile().endsWith("ipa")) {
            Toast.makeText(context, "ipa苹果应用,无法安装", Toast.LENGTH_SHORT).show();
            return;
        }
        Extras extras = fileUri.getExtras();
        String apkpath = extras.getString("apkpath", "");
        String apkUrl = extras.getString("apkUrl", "");
        String obbpath = extras.getString("obbpath", "");
        String obbUrl = extras.getString("obbUrl", "");
        String packagename = fileUri.getTag();
        LogUtils.e("install App extra", "apkpath:" + apkpath + " apkUrl:" + apkUrl);
        LogUtils.e("install Obb extra", "obbpath:" + obbpath + " obbUrl:" + obbUrl);

        if (!TextUtils.isEmpty(obbpath) && !TextUtils.isEmpty(obbUrl)) {
            //obb copy
            Uri uriobb = Uri.parse(obbUrl);
            String s = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + packagename;
            File obbs = new File(s);
            if (!obbs.exists()) {
                obbs.mkdirs();
            }
            String target = s + "/" + uriobb.getLastPathSegment();
            LogUtils.e("installApp", "obb target path:" + target);
            boolean b = FileUtils.copyFile(obbpath, target);
            LogUtils.e("installApp", "obb 是否成功：" + b);
        }

        Toast.makeText(context, packagename + "  下载完成", Toast.LENGTH_SHORT).show();
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            install.setDataAndType(Uri.fromFile(new File(apkpath)), "application/vnd.android.package-archive");
        } else {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(apkpath));
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(install);
    }

    public static void installApp(Context context, OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            boolean haveInstallPermission = AppManager.getAppManager().currentActivity().getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {//没有权限
                Uri packageURI = Uri.parse("package:" + context.getPackageName());
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                AppManager.getAppManager().currentActivity().startActivityForResult(intent, 11111);
                return;
            }
        }
        if (data.getDownloadUrl().endsWith("ipa")) {
            Toast.makeText(context, "ipa苹果应用,无法安装", Toast.LENGTH_SHORT).show();
            return;
        }
        File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
        //copy obb.
        if (!TextUtils.isEmpty(data.getObbDownloadUrl())) {
            Uri uriobb = Uri.parse(data.getObbDownloadUrl());
            File testFileobb = new File(sdCardPath, data.get_id() + uriobb.getLastPathSegment());
            String targetdir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/obb/" + data.getBundleId();
            String target = targetdir + "/" + uriobb.getLastPathSegment();
            File targetFile = new File(targetdir);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            LogUtils.e("installApp", "obb old target path:" + testFileobb.getAbsolutePath());
            LogUtils.e("installApp", "obb new target path:" + target);
            boolean b = FileUtils.copyFile(testFileobb.getAbsolutePath(), target);
            LogUtils.e("installApp", "obb 是否成功：" + b);
        }
        Uri uri = Uri.parse(data.getDownloadUrl());
        File testFile = new File(sdCardPath, data.get_id() + uri.getLastPathSegment());
        Request request = new Request(data.getDownloadUrl(), testFile.getAbsolutePath());
        Uri fileUri = request.getFileUri();
        Intent install = new Intent(Intent.ACTION_VIEW);
        LogUtils.e("installApp", "app uri:" + fileUri.toString());
        LogUtils.e("installApp", "app path:" + testFile.getAbsolutePath());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            install.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", new File(fileUri.getPath()));
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        }
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(install);
    }

    @Override
    public void onDeleted(@NotNull Download download) {

    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {

    }

    @Override
    public void onPaused(@NotNull Download download) {

    }

    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {
    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {

    }

    @Override
    public void onRemoved(@NotNull Download download) {

    }

    @Override
    public void onResumed(@NotNull Download download) {

    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {

    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {

    }

    public enum DownLoaderStatus {
        NONE, DOWNLODING, UNCOMPLETE, COMPLEDTED, NO
    }

    public static DownloadServerice getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadServerice();
        }
        return mInstance;
    }

    public static HashMap<Integer, Integer> getDownloadingList() {
        return mProgressStore;
    }


    public static DownloadServerice.DownLoaderStatus getAppFileStatus(OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean data) {
        Uri uri = Uri.parse(data.getDownloadUrl());
        File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
        File testFile = new File(sdCardPath, data.get_id() + uri.getLastPathSegment());
        int downloadid = new Request(data.getDownloadUrl(), testFile.getAbsolutePath()).getId();
        data.set__v(downloadid);

        if (TextUtils.isEmpty(data.getDownloadUrl()) || TextUtils.isEmpty(data.get_id())) {
            return DownLoaderStatus.NO;
        } else if (data.get__v() != 0 && getDownloadingList().get(data.get__v()) != null) {
            return DownLoaderStatus.DOWNLODING;
        } else if (data.get__v() != 0 && Yodo1SharedPreferences.hasDownloadTask(MyApplication.mApplication, data.get__v())) {
            //暂停，or未完成
            return DownLoaderStatus.UNCOMPLETE;
        } else {
            if (getDownloadingList().get(downloadid) != null) {
                return DownLoaderStatus.DOWNLODING;
            } else if (Yodo1SharedPreferences.hasDownloadTask(MyApplication.mApplication, downloadid)) {
                return DownLoaderStatus.UNCOMPLETE;
            } else if (testFile.exists()) {
                //TODO 为了健壮，文件的完整校验可以加入。
                return DownLoaderStatus.COMPLEDTED;
            } else {
                return DownLoaderStatus.NONE;
            }
        }
    }

    public static DownloadServerice.DownLoaderStatus getObbFileStatus(OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean data) {
        if (TextUtils.isEmpty(data.getObbDownloadUrl())) {
            return DownLoaderStatus.NO;
        }
        Uri uri = Uri.parse(data.getObbDownloadUrl());
        File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
        File testFile = new File(sdCardPath, data.get_id() + uri.getLastPathSegment());
        int downloadid = new Request(data.getObbDownloadUrl(), testFile.getAbsolutePath()).getId();
        data.set__vobb(downloadid);

        if (data.get__vobb() != 0 && getDownloadingList().get(data.get__vobb()) != null) {
            return DownLoaderStatus.DOWNLODING;
        } else if (data.get__vobb() != 0 && Yodo1SharedPreferences.hasDownloadTask(MyApplication.mApplication, data.get__vobb())) {
            return DownLoaderStatus.UNCOMPLETE;
        } else {
            if (getDownloadingList().get(downloadid) != null) {
                return DownLoaderStatus.DOWNLODING;
            } else if (Yodo1SharedPreferences.hasDownloadTask(MyApplication.mApplication, downloadid)) {
                return DownLoaderStatus.UNCOMPLETE;
            } else if (testFile.exists()) {
                //TODO 为了健壮，文件的完整校验可以加入。
                return DownLoaderStatus.COMPLEDTED;
            } else {
                return DownLoaderStatus.NONE;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getInstance();
        String CHANNEL_ID = getPackageName() + ".downloader";
        PendingIntent intent = PendingIntent.getActivity(this, 1000, new Intent(this, MainActivity.class), PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
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

        getFetch().removeListener(this);
        getFetch().addListener(this);
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
        getDownloadingList().clear();
//        getFetch().close();
        super.onDestroy();
        LogUtils.e("yodo1", "downloader onDestory");
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        getFetch().pauseAll();
        getDownloadingList().clear();
//        getFetch().close();
        super.onTaskRemoved(rootIntent);
        LogUtils.e("yodo1", "downloader onTaskRemoved");
    }
}
