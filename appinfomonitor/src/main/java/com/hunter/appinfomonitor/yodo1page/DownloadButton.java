package com.hunter.appinfomonitor.yodo1page;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
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
import android.widget.Toast;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
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
import java.util.HashMap;
import java.util.List;

public class DownloadButton extends FrameLayout implements View.OnClickListener, FetchListener {


    private OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean mData;
    private Context mContext;
    private ViewGroup childsView;
    private Button button;
    private ProgressBar progressBar;
    public static HashMap<String, Integer> mProgressStore = new HashMap<>();
    private int fetchDownloadID, fetchObbDownloadID;

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
        progressBar.setMax(100);
        button.setTag(null);
        fetchDownloadID = 0;
        fetchObbDownloadID = 0;
        if (mData == null || TextUtils.isEmpty(mData.getDownloadUrl()) || TextUtils.isEmpty(mData.get_id())) {
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setTextColor(Color.RED);
            button.setText("异常");
            button.setOnClickListener(null);
            progressBar.setOnClickListener(null);
        } else {
            Uri uri = Uri.parse(mData.getDownloadUrl());
            File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
            button.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            button.setTextColor(Color.BLACK);
            File testFile = new File(sdCardPath, uri.getLastPathSegment());
            if (mProgressStore.get(mData.get_id()) != null) {
                button.setText("下载中");
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(mProgressStore.get(mData.get_id()));
                final Request request = new Request(mData.getDownloadUrl(), testFile.getAbsolutePath());
                fetchDownloadID = request.getId();
            } else if (testFile.exists()) {
                //TODO 存在不一定下载完成
                button.setText("已下载");
            } else {
                button.setText("下载");
                final Request request = new Request(mData.getDownloadUrl(), testFile.getAbsolutePath());
                fetchDownloadID = request.getId();
            }
            button.setOnClickListener(this);
            progressBar.setOnClickListener(this);
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
    protected void onDraw(Canvas canvas) {
        if (mData != null && mProgressStore.get(mData.get_id()) != null) {
            progressBar.setProgress(mProgressStore.get(mData.get_id()));
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.e("Yodo1", "控件消失，hash:" + hashCode());
        DownloadServerice.getInstance().getFetch().removeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mProgressStore.get(mData.get_id()) != null) {
            if (button.getTag() == null) {
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                button.setTextColor(Color.BLACK);
                button.setText("暂停下载");
                button.setTag("");
                pauseDownload();
            } else {
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                button.setTag(null);
                goOnDownload();
            }
        } else {
            //未下载，已下载。
            if (fetchDownloadID == 0) {
                Toast.makeText(mContext, "已下载文件", Toast.LENGTH_SHORT).show();
            } else {
                button.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(1);
                goDownload();
            }
        }
    }

    private void pauseDownload() {
        LogUtils.e("yodo1", "暂停下载");
        if (mData != null && mProgressStore.get(mData.get_id()) != null) {
            DownloadServerice.getInstance().getFetch().pause(fetchDownloadID);
        }
    }

    private void goOnDownload() {
        LogUtils.e("yodo1", "继续下载");
        goOrContinueDownload();
    }

    private void goDownload() {
        LogUtils.e("yodo1", "开始下载");
        goOrContinueDownload();
    }


    private void goOrContinueDownload() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
            return;
        }
        Uri uri = Uri.parse(mData.getDownloadUrl());
        File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
        /*
         * 文件输出操作
         * */
        File testFile = new File(sdCardPath, uri.getLastPathSegment());
        if (testFile.exists()) {
            Toast.makeText(mContext, "已下载成功的文件", Toast.LENGTH_SHORT).show();
        } else {
            if (!testFile.exists() && Yodo1SharedPreferences.hasDownloadTask(mContext, fetchDownloadID)) {
                DownloadServerice.getInstance().getFetch().resume(fetchDownloadID);
            } else {
                final Request request = new Request(mData.getDownloadUrl(), testFile.getAbsolutePath());
                request.setPriority(Priority.HIGH);
                request.setNetworkType(NetworkType.ALL);
                mProgressStore.put(String.valueOf(request.getId()), 1);
                DownloadServerice.getInstance().getFetch().enqueue(request, new Func<Request>() {
                    @Override
                    public void call(@NotNull Request result) {
                        Yodo1SharedPreferences.addDownloadTask(mContext, request.getId());
                    }
                }, new Func<Error>() {
                    @Override
                    public void call(@NotNull Error result) {
                    }
                });
            }
        }
    }

    @Override
    public void onAdded(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onAdded");
        }
    }

    @Override
    public void onCancelled(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onCancelled");
        }
    }

    @Override
    public void onCompleted(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onCompleted");
            mProgressStore.remove(String.valueOf(download.getId()));
            Yodo1SharedPreferences.removeDownloadTask(mContext, download.getId());
            progressBar.setProgress(0);
            progressBar.setVisibility(View.GONE);
            button.setVisibility(View.VISIBLE);
            button.setTextColor(Color.BLACK);
            button.setText("已下载");
        }
    }

    @Override
    public void onDeleted(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onDeleted");
        }
    }

    @Override
    public void onDownloadBlockUpdated(@NotNull Download download, @NotNull DownloadBlock downloadBlock, int i) {

    }

    @Override
    public void onError(@NotNull Download download, @NotNull Error error, @org.jetbrains.annotations.Nullable Throwable throwable) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onError");
        }
    }

    @Override
    public void onPaused(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onPaused");
        }
    }

    @Override
    public void onProgress(@NotNull Download download, long l, long l1) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onProgress");
            int id = download.getId();
            long downloaded = download.getDownloaded();
        }
    }

    @Override
    public void onQueued(@NotNull Download download, boolean b) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onQueued");
        }
    }

    @Override
    public void onRemoved(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onRemoved");
        }
    }

    @Override
    public void onResumed(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onResumed");
        }
    }

    @Override
    public void onStarted(@NotNull Download download, @NotNull List<? extends DownloadBlock> list, int i) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onStarted");
        }
    }

    @Override
    public void onWaitingNetwork(@NotNull Download download) {
        if (download.getId() == fetchDownloadID) {
            LogUtils.e("Fetch", download.getUrl() + "  onWaitingNetwork");
        }
    }
}
