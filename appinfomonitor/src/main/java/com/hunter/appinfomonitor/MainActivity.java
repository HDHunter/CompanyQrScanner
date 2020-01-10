package com.hunter.appinfomonitor;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hunter.appinfomonitor.floatui.TasksWindow;
import com.hunter.appinfomonitor.ui.AppInfoAdapter;
import com.hunter.appinfomonitor.ui.AppInfoModel;

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
                TasksWindow.show(MainActivity.this, "");
                startService(new Intent(MainActivity.this, WatchingService.class));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    startForegroundService(new Intent(MainActivity.this, FloatServerice.class));
//                } else {
//                    startService(new Intent(MainActivity.this, FloatServerice.class));
//                }
                Intent intent = new Intent();
                intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                startActivity(intent);
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
