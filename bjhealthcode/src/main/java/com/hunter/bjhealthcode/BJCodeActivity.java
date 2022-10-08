package com.hunter.bjhealthcode;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.PictureFileUtils;
import com.yalantis.ucrop.UCrop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BJCodeActivity extends AppCompatActivity {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
    SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
    SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd 24:00");
    Timer timer;
    TextView bj_time;
    ImageView photo_bg;
    ImageView photo;
    Date date;
    int photoflag = 0;
    String TAG = "[BJCode]";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String system = DeviceUtils.getDeviceBrand().toLowerCase();
        Log.e(TAG, "zzzzzzz:" + system);
        if (system.contains("oppo")) {
            setContentView(R.layout.activity_bjcode);
        } else if (system.contains("mi")) {
            setContentView(R.layout.activity_bjcodexiaomi);
        } else {
            setContentView(R.layout.activity_bjcodeother);
        }
        Window window = getWindow();
        window.setStatusBarColor(Color.parseColor("#f02967FF"));


        TextView bj_date = findViewById(R.id.bj_date);
        bj_time = findViewById(R.id.bj_time);
        TextView scan_name = findViewById(R.id.scan_name);
        TextView scan_id = findViewById(R.id.scan_id);
        TextView scan_time = findViewById(R.id.scan_time);
        TextView scan_expire_time = findViewById(R.id.scan_expire_time);
        photo_bg = findViewById(R.id.photo_bg);
        photo = findViewById(R.id.photo);

        date = new Date();
        bj_date.setText(sdf.format(date));
        bj_time.setText(sdf1.format(date));


        String name = YSharedPreferences.getString(this, "name");
        scan_name.setText(name + "*");

        String id = YSharedPreferences.getString(this, "id");
        if (!TextUtils.isEmpty(id) && id.length() == 4) {
            String preId = id.substring(0, 2);
            String afterId = id.substring(2);

            if (system.contains("oppo")) {
                scan_id.setText(preId + "                             " + afterId);
            } else if (system.contains("mi")) {
                scan_id.setText(preId + "                        " + afterId);
            } else {
                scan_id.setText(preId + "                          " + afterId);
            }

        } else {
            scan_id.setText("");
        }

        scan_time.setText(sdf2.format(date));
        scan_expire_time.setText(sdf3.format(date));

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(BJCodeActivity.this)
                        .openSystemGallery(SelectMimeType.ofImage())
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .setCropEngine(new CropFileEngine() {
                            @Override
                            public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
                                // 注意* 如果你实现自己的裁剪库，需要在Activity的.setResult();
                                // Intent中需要给MediaStore.EXTRA_OUTPUT，塞入裁剪后的路径；如果有额外数据也可以通过CustomIntentKey.EXTRA_CUSTOM_EXTRA_DATA字段存入；

                                UCrop uCrop = UCrop.of(srcUri, destinationUri)
                                        .withAspectRatio(9, 9);
                                uCrop.start(fragment.getActivity(), fragment, requestCode);
                            }
                        })
                        .forSystemResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                analyticalSelectResults(result);
                            }

                            @Override
                            public void onCancel() {
                                Toast.makeText(BJCodeActivity.this, "cancel.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });


        boolean isUri = YSharedPreferences.getBoolean(this, "isUri");
        String path = YSharedPreferences.getString(this, "path");
        if (!TextUtils.isEmpty(path)) {
            RequestOptions options = new RequestOptions()
                    .bitmapTransform(new RoundedCorners(DipPx.dip2px(this, 8)));
            Glide.with(this)
                    .load(isUri ? Uri.parse(path)
                            : path)
                    .apply(options)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo);
        }
    }

    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(this, media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(this, media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i(TAG, "文件名: " + media.getFileName());
            Log.i(TAG, "是否压缩:" + media.isCompressed());
            Log.i(TAG, "压缩:" + media.getCompressPath());
            Log.i(TAG, "初始路径:" + media.getPath());
            Log.i(TAG, "绝对路径:" + media.getRealPath());
            Log.i(TAG, "是否裁剪:" + media.isCut());
            Log.i(TAG, "裁剪路径:" + media.getCutPath());
            Log.i(TAG, "是否开启原图:" + media.isOriginal());
            Log.i(TAG, "原图路径:" + media.getOriginalPath());
            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
            Log.i(TAG, "水印路径:" + media.getWatermarkPath());
            Log.i(TAG, "视频缩略图:" + media.getVideoThumbnailPath());
            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i(TAG, "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
            Log.i(TAG, "文件时长: " + media.getDuration());

            String path = media.getAvailablePath();
            RequestOptions options = new RequestOptions()
                    .bitmapTransform(new RoundedCorners(DipPx.dip2px(this, 8)));

//            PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed() ? Uri.parse(path)
//                    : path;
            boolean isUri = PictureMimeType.isContent(path) && !media.isCut() && !media.isCompressed();
            YSharedPreferences.put(this, "isUri", isUri);
            YSharedPreferences.put(this, "path", path);
            Glide.with(this)
                    .load(isUri ? Uri.parse(path)
                            : path)
                    .apply(options)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                analyticalSelectResults(result);
            }
        } else if (resultCode == RESULT_CANCELED) {
            Log.i(TAG, "onActivityResult PictureSelector Cancel");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
        }

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                date.setTime(System.currentTimeMillis());
                bj_time.setText(sdf1.format(date));
            }
        }, 200, 1000);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (photoflag == 0) {
                            photo_bg.setImageResource(R.drawable.photobg1);
                            photoflag = 1;
                        } else {
                            photo_bg.setImageResource(R.drawable.photobg2);
                            photoflag = 0;
                        }
                    }
                });
            }
        }, 200, 700);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {
            timer.cancel();
        }
        super.onDestroy();
    }
}
