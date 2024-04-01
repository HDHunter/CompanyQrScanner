package com.hunter.appinfomonitor;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.yodo1page.DownloadListActivity;
import com.hunter.appinfomonitor.yodo1page.DownloadServerice;
import com.tonyodev.fetch2.Error;
import com.tonyodev.fetch2.NetworkType;
import com.tonyodev.fetch2.Priority;
import com.tonyodev.fetch2.Request;
import com.tonyodev.fetch2core.Func;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * @author yodo1
 */
public class Yodo1QrCodeActivity extends BaseActvity implements QRCodeView.Delegate {

    private ZBarView mZBarView;
    private TextView bottomdisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);


        mZBarView = findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
        mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码

//        mZBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//        mZBarView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
//        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别

        mZBarView.setType(BarcodeType.ALL, null); // 识别所有类型的码

        bottomdisplay = findViewById(R.id.bottomdisplay);
        findViewById(R.id.bottomdisplab_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = bottomdisplay.getText();
                if (!TextUtils.isEmpty(text)) {
                    final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("Label", text);
                    cm.setPrimaryClip(mClipData);
                    Toast.makeText(Yodo1QrCodeActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    public void onResume() {
        super.onResume();
        mZBarView.startSpot(); // 开始识别
    }

    @Override
    public void onPause() {
        super.onPause();
        mZBarView.stopSpot(); // 停止识别
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        LogUtils.e("QrCode", "result:" + result);
        vibrate();

        bottomdisplay.setText(result);

        if (result.startsWith("http")) {
            if (result.endsWith("apk")) {
                LogUtils.e("yodo1", "开始下载");
                Uri uri = Uri.parse(result);
                File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
                File testFile = new File(sdCardPath, uri.getLastPathSegment());
                final Request request = new Request(result, testFile.getAbsolutePath());
                request.setPriority(Priority.NORMAL);
                request.setNetworkType(NetworkType.ALL);
                DownloadServerice.getInstance().getFetch().enqueue(request, new Func<Request>() {
                    @Override
                    public void call(@NotNull Request result) {
                        Yodo1SharedPreferences.addDownloadTask(Yodo1QrCodeActivity.this, request.getId());
                        DownloadServerice.getDownloadingList().put(request.getId(), 1);

                        mZBarView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(Yodo1QrCodeActivity.this, DownloadListActivity.class);
                                startActivityForResult(intent, 1111);
                            }
                        }, 1000);
                    }
                }, new Func<Error>() {
                    @Override
                    public void call(@NotNull Error result) {
                    }
                });
            } else if (result.endsWith("pdf")) {
                Uri uri = Uri.parse(result);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Uri uri = Uri.parse(result);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        } else {
            //go on
            goon();
        }
    }

    private void goon() {
        mZBarView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZBarView.startSpot(); // 开始识别
            }
        }, 1000);
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZBarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(this, "打开相机出错", Toast.LENGTH_SHORT).show();
        LogUtils.e("TAG", "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mZBarView.showScanRect();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
