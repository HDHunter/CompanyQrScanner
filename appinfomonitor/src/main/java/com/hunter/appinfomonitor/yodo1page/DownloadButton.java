package com.hunter.appinfomonitor.yodo1page;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.GunqiuApi;
import com.hunter.appinfomonitor.network.okbiz.RxResponse;
import com.hunter.appinfomonitor.network.okbiz.RxResultHelper;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.ui.OtaAPi;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;
import com.tonyodev.fetch2.Download;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.FetchListener;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.DownloadBlock;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class DownloadButton extends FrameLayout implements View.OnClickListener, FetchListener {


    private OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean mData;
    private Context mContext;
    private ViewGroup childsView;
    private Button button;
    private ProgressBar progressBar;

    public DownloadButton(@NonNull Context context) {
        super(context);
        layoutMe(context);
    }

    public DownloadButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        layoutMe(context);
    }

    public DownloadButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        layoutMe(context);
    }

    private void layoutMe(Context context) {
        childsView = (ViewGroup) View.inflate(context, R.layout.download_layout, this);
        button = childsView.findViewById(R.id.download_layout_btn);
        progressBar = childsView.findViewById(R.id.download_layout_prob);
        mContext = context;
    }

    public void setData(OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean child) {
        this.mData = child;
        refreshButon();
    }

    private void refreshButon() {
        if (!TextUtils.isEmpty(mData.getObbDownloadUrl())) {
            progressBar.setMax(200);
        } else {
            progressBar.setMax(100);
        }
        button.setTag(null);
        DownloadServerice.DownLoaderStatus appFileStatus = DownloadServerice.getAppFileStatus(mData);
        DownloadServerice.DownLoaderStatus appobbFileStatus = DownloadServerice.getObbFileStatus(mData);
        if (appFileStatus == DownloadServerice.DownLoaderStatus.NONE || appobbFileStatus == DownloadServerice.DownLoaderStatus.NONE) {
            button.setText("下载");
            button.setTextColor(Color.BLACK);
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setOnClickListener(this);
            progressBar.setOnClickListener(this);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.UNCOMPLETE || appobbFileStatus == DownloadServerice.DownLoaderStatus.UNCOMPLETE) {
            button.setText("未完成");
            button.setTextColor(Color.BLACK);
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setOnClickListener(this);
            progressBar.setOnClickListener(this);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.COMPLEDTED) {
            button.setText("已下载");
            button.setTextColor(Color.BLUE);
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setOnClickListener(this);
            progressBar.setOnClickListener(this);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.DOWNLODING || appobbFileStatus == DownloadServerice.DownLoaderStatus.DOWNLODING) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(DownloadServerice.getDownloadingList().get(mData.get__v()));
            button.setOnClickListener(this);
            progressBar.setOnClickListener(this);
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setTextColor(Color.RED);
            button.setText("异常");
            button.setOnClickListener(null);
            progressBar.setOnClickListener(null);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        DownloadServerice.getInstance().getFetch().removeListener(this);
        super.onAttachedToWindow();
        LogUtils.e("Yodo1", "显示控件，hash:" + hashCode());
        DownloadServerice.getInstance().getFetch().addListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.e("Yodo1", "控件消失，hash:" + hashCode());
        DownloadServerice.getInstance().getFetch().removeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
            return;
        }
        DownloadServerice.DownLoaderStatus appFileStatus = DownloadServerice.getAppFileStatus(mData);
        DownloadServerice.DownLoaderStatus appobbFileStatus = DownloadServerice.getObbFileStatus(mData);
        if (appFileStatus == DownloadServerice.DownLoaderStatus.NONE || appobbFileStatus == DownloadServerice.DownLoaderStatus.NONE) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            goOnDownload(true);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.UNCOMPLETE || appobbFileStatus == DownloadServerice.DownLoaderStatus.UNCOMPLETE) {
            button.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            goOnDownload(false);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.COMPLEDTED) {
            DownloadServerice.installApp(mContext, mData);
        } else if (appFileStatus == DownloadServerice.DownLoaderStatus.DOWNLODING || appobbFileStatus == DownloadServerice.DownLoaderStatus.DOWNLODING) {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setTextColor(Color.BLACK);
            button.setText("暂停下载");
            pauseDownload();
        } else {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setTextColor(Color.RED);
            button.setText("异常");
        }
    }

    private void pauseDownload() {
        if (mData.get__v() != 0) {
            LogUtils.e("yodo1", "暂停app下载");
            DownloadServerice.getInstance().getFetch().pause(mData.get__v());
        }
        if (mData.get__vobb() != 0) {
            LogUtils.e("yodo1", "暂停obb下载");
            DownloadServerice.getInstance().getFetch().pause(mData.get__vobb());
        }
    }

    private void goOnDownload(boolean isFirst) {
        if (!isFirst) {
            LogUtils.e("yodo1", "继续下载");
            DownloadServerice.getInstance().getFetch().resume(mData.get__v());
            if (mData.get__vobb() != 0) {
                DownloadServerice.getInstance().getFetch().resume(mData.get__v());
            }
        } else {
            LogUtils.e("yodo1", "开始下载");
            Uri uri = Uri.parse(mData.getDownloadUrl());
            File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
            File testFile = new File(sdCardPath, mData.get_id() + uri.getLastPathSegment());
            final Request request = new Request(mData.getDownloadUrl(), testFile.getAbsolutePath());
            request.setPriority(Priority.HIGH);
            request.setNetworkType(NetworkType.ALL);
            DownloadServerice.getInstance().getFetch().enqueue(request, new Func<Request>() {
                @Override
                public void call(@NotNull Request result) {
                    Yodo1SharedPreferences.addDownloadTask(mContext, request.getId());
                    DownloadServerice.getDownloadingList().put(request.getId(), 1);
                }
            }, new Func<Error>() {
                @Override
                public void call(@NotNull Error result) {
                }
            });
            if (!TextUtils.isEmpty(mData.getObbDownloadUrl())) {
                Uri uriobb = Uri.parse(mData.getObbDownloadUrl());
                File testFileobb = new File(sdCardPath, mData.get_id() + uriobb.getLastPathSegment());
                final Request requestobb = new Request(mData.getObbDownloadUrl(), testFileobb.getAbsolutePath());
                requestobb.setPriority(Priority.HIGH);
                requestobb.setNetworkType(NetworkType.ALL);
                DownloadServerice.getInstance().getFetch().enqueue(requestobb, new Func<Request>() {
                    @Override
                    public void call(@NotNull Request result) {
                        Yodo1SharedPreferences.addDownloadTask(mContext, requestobb.getId());
                        DownloadServerice.getDownloadingList().put(requestobb.getId(), 1);
                    }
                }, new Func<Error>() {
                    @Override
                    public void call(@NotNull Error result) {
                    }
                });
            }
            //增加一个下载量
            GunqiuApi.getInstance().get(OtaAPi.createCount.replace("{appid}", mData.getAppId()).replace("{versionId}", mData.get_id())).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onFailure(Throwable e) {

                }
            });
        }
    }


    @Override
    public void onAdded(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onAdded");
        }
    }

    @Override
    public void onCancelled(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onCancelled");
        }
    }

    @Override
    public void onCompleted(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onCompleted");
            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            button.setTextColor(Color.BLUE);
            button.setText("已下载");
        }
        DownloadServerice.getDownloadingList().remove(download.getId());
        Yodo1SharedPreferences.removeDownloadTask(mContext, download.getId());
    }

    @Override
    public void onDeleted(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onDeleted");
        }
    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onError:" + throwable.getMessage());
        }
    }

    @Override
    public void onPaused(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onPaused");
        }
    }

    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            long downloaded = download.getDownloaded();
            int percent = 0;
            if (download.getId() == mData.get__v()) {
                percent = (int) ((downloaded * 100) / mData.getSize());
                LogUtils.e("Fetch", download.getUrl() + "  get_v:" + mData.get__v() + "  downloadId:" + download.getId());
            } else if (download.getId() == mData.get__vobb()) {
                percent = (int) ((downloaded * 100) / mData.getObbSize());
                LogUtils.e("Fetch", download.getUrl() + "  get__vobb:" + mData.get__vobb() + "  downloadId:" + download.getId());
            }
            LogUtils.e("Fetch", download.getUrl() + "  onProgress,percent:" + percent + "  download:" + downloaded);
            if (percent >= 100) {
                DownloadServerice.getDownloadingList().put(download.getId(), 100);
            } else {
                DownloadServerice.getDownloadingList().put(download.getId(), percent);
            }
        }

        //here,zjq
        if (mData != null && (download.getId() == mData.get__v() || download.getId() == mData.get__vobb())) {
            Integer integer = DownloadServerice.getDownloadingList().get(mData.get__v());
            Integer integer2 = DownloadServerice.getDownloadingList().get(mData.get__vobb());
            if (integer != null) {
                LogUtils.e("Fetch", "onProgress:" + integer);
            }
            if (integer2 != null) {
                LogUtils.e("Fetch", "onProgress:" + integer2);
            }
            int prog = (integer == null ? 0 : integer);
            prog += (integer2 == null ? 0 : integer2);
            progressBar.setProgress(prog);
        } else {
            LogUtils.e("Fetch", " onProgress downloadFile:" + download.getFile() + " downloadId:" + download.getId());
        }
    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onQueued");
        }
    }

    @Override
    public void onRemoved(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onRemoved");
        }
    }

    @Override
    public void onResumed(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onResumed");
        }
    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onStarted");
        }
    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {
        if (download.getId() == mData.get__v() || download.getId() == mData.get__vobb()) {
            LogUtils.e("Fetch", download.getUrl() + "  onWaitingNetwork");
        }
    }
}
