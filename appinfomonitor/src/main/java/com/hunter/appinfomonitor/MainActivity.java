package com.hunter.appinfomonitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hunter.appinfomonitor.ui.AppInfoAdapter;
import com.hunter.appinfomonitor.ui.AppInfoModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppInfoAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        adapter = new AppInfoAdapter(this);
        recyclerView.setAdapter(adapter);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        });

        findViewById(R.id.togglebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setFilter(0, null);
            }
        });
        EditText searc = findViewById(R.id.search);
        searc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                adapter.setFilter(1, s1);
            }
        });

        findViewById(R.id.togglebutton2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show Alarm Service
                startService(new Intent(MainActivity.this, WatchingService.class));
                //show Accessibility Setting.
                Intent intent = new Intent();
                intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                startActivity(intent);
            }
        });

        findViewById(R.id.togglebutton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setFilter(1, "com.yodo1");
            }
        });

        findViewById(R.id.togglebutton4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 定义文件内容字符串
                String content = null;

                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File sdCardPath = Environment.getExternalStorageDirectory();

                    try {
                        File testFile = new File(sdCardPath, ".yodo1ads");
                        FileInputStream fileInputStream = new FileInputStream(testFile);
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fileInputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, len);
                        }
                        content = outputStream.toString();
                        fileInputStream.close();
                        outputStream.close();
                    } catch (FileNotFoundException e) {
                        Log.d("zzzzz", "yodo1 缺少SD卡权限  读取文件失败");

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
                        }
                    } catch (IOException e) {
                        Log.d("zzzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                    }
                }
                if (!TextUtils.isEmpty(content)) {
                    Log.e("zzzzzz", content);
                }

                if (content == null || !content.contains("openYodo1Log")) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File sdCardPath = Environment.getExternalStorageDirectory();
                        try {
                            File testFile = new File(sdCardPath, ".yodo1ads");
                            if (!testFile.exists()) {
                                testFile.createNewFile();
                            }
                            FileOutputStream fileOutputStream = new FileOutputStream(testFile);
                            if (content == null) {
                                content = "openYodo1Log";
                            } else {
                                content += "\nopenYodo1Log";
                            }
                            fileOutputStream.write(content.getBytes());
                            fileOutputStream.flush();
                            fileOutputStream.close();
                        } catch (FileNotFoundException e) {
                            Log.d("zzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                        } catch (Exception e) {
                            Log.d("zzzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                        }
                    }
                }

                if (!TextUtils.isEmpty(content)) {
                    Log.e("zzzzzza", content);
                }
                String s = TextUtils.isEmpty(content) ? "全局debugLog标记开启失败" : "全局debugLog标记开启成功";
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        ArrayList<AppInfoModel> infos = new ArrayList<>();
        for (PackageInfo info : installedPackages) {
            AppInfoModel appInfoModel = new AppInfoModel();
            appInfoModel.setPackageName(info.packageName);
            appInfoModel.setVersionCode(String.valueOf(info.versionCode));
            appInfoModel.setVersionName(info.versionName);
            ApplicationInfo apI = info.applicationInfo;
            appInfoModel.setApplicationInfo(apI);
            appInfoModel.setAppName(apI.loadLabel(pm).toString());
            infos.add(appInfoModel);
        }
        adapter.setData(infos);
    }

}
