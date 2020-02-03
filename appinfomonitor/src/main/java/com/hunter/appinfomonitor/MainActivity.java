package com.hunter.appinfomonitor;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.floatui.NotificationActionReceiver;
import com.hunter.appinfomonitor.floatui.SPHelper;
import com.hunter.appinfomonitor.floatui.TasksWindow;
import com.hunter.appinfomonitor.floatui.WatchingAccessibilityService;
import com.hunter.appinfomonitor.ui.AppInfoAdapter;
import com.hunter.appinfomonitor.ui.AppInfoModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppInfoAdapter adapter;
    public static final String ACTION_STATE_CHANGED = BuildConfig.APPLICATION_ID + ".ACTION_STATE_CHANGED";
    public static final String EXTRA_FROM_QS_TILE = "from_qs_tile";
    private UpdateSwitchReceiver mReceiver;
    private Button floatBtn;
    private static boolean openfloat = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        adapter = new AppInfoAdapter(this);
        recyclerView.setAdapter(adapter);

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
        searc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s1 = v.getText().toString();
                    adapter.setFilter(1, s1);
                    return true;
                } else {
                    return false;
                }
            }
        });
        floatBtn = findViewById(R.id.togglebutton2);
        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNotification.HunterZhang,2020年2月3日19:37:15
                if (SPHelper.hasQSTileAdded(MainActivity.this)) {
                    SPHelper.setNotificationToggleEnabled(MainActivity.this, !openfloat);
                } else if (openfloat) {
                    Toast.makeText(MainActivity.this, R.string.toast_add_tile, Toast.LENGTH_LONG).show();
                } else {
                    SPHelper.setNotificationToggleEnabled(MainActivity.this, false);
                }

                //showFloatButton.HunterZhang
                if (openfloat && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1 && !Settings.canDrawOverlays(MainActivity.this)) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(R.string.dialog_enable_overlay_window_msg)
                            .setPositiveButton(R.string.dialog_enable_overlay_window_positive_btn
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                                            intent.setData(Uri.parse("package:" + getPackageName()));
                                            startActivity(intent);
                                            dialog.dismiss();
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SPHelper.setIsShowWindow(MainActivity.this, false);
                                            refreshWindowSwitch();
                                        }
                                    })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    SPHelper.setIsShowWindow(MainActivity.this, false);
                                    refreshWindowSwitch();
                                }
                            })
                            .create()
                            .show();
                    return;
                } else if (WatchingAccessibilityService.getInstance() == null) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage(R.string.dialog_enable_accessibility_msg)
                            .setPositiveButton(R.string.dialog_enable_accessibility_positive_btn
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SPHelper.setIsShowWindow(MainActivity.this, true);
                                            Intent intent = new Intent();
                                            intent.setAction("android.settings.ACCESSIBILITY_SETTINGS");
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton(android.R.string.cancel
                                    , new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            refreshWindowSwitch();
                                        }
                                    })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    refreshWindowSwitch();
                                }
                            })
                            .create()
                            .show();
                    SPHelper.setIsShowWindow(MainActivity.this, true);
                    return;
                }
                SPHelper.setIsShowWindow(MainActivity.this, openfloat);
                TasksWindow.show(MainActivity.this, getPackageName() + "\n" + getClass().getName());

                if (!openfloat) {
                    TasksWindow.dismiss(MainActivity.this);
                } else {
                    TasksWindow.show(MainActivity.this, getPackageName() + "\n" + getClass().getName());
                }
                openfloat = !openfloat;
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
        findViewById(R.id.togglebutton5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("PackageInfo");
                try {
                    builder.setMessage(updateInfos());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
            }
        });

        recyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 100);
        if (getResources().getBoolean(R.bool.use_watching_service)) {
            TasksWindow.show(this, "");
            startService(new Intent(this, WatchingService.class));
        }

        mReceiver = new UpdateSwitchReceiver();
        registerReceiver(mReceiver, new IntentFilter(ACTION_STATE_CHANGED));
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

    private String updateInfos() {
        StringBuilder sb = new StringBuilder();
        StringBuilderPrinter ps = new StringBuilderPrinter(sb);
        String display = "Display:" + Build.DISPLAY;
        ps.println(display);
        String print = "FingerPrint:";
        ps.println(Build.FINGERPRINT);
        ps.println(print);
        ps.println("");
        String hardware = "HardWare:" + Build.HARDWARE;
        ps.println(hardware);
        String host = "Host:" + Build.HOST;
        ps.println(host);
        String id = "ID:" + Build.ID;
        ps.println(id);
        String msnufactur = "Manufacturer:" + Build.MANUFACTURER;
        ps.println(msnufactur);
        String model = "Module:" + Build.MODEL;
        ps.println(model);
        String product = "Product:" + Build.PRODUCT;
        ps.println(product);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            String abis = "SupportAbis:" + Arrays.toString(Build.SUPPORTED_ABIS);
            ps.println(abis);
        } else {
            String abi = "CPUabi:" + Build.CPU_ABI;
            ps.println(abi);
            String abi2 = "CPUabi2:" + Build.CPU_ABI2;
            ps.println(abi2);
        }
        Log.e("zzzzz", "update Infos");
        return sb.toString();
    }


    class UpdateSwitchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshWindowSwitch();
            refreshNotificationSwitch();
        }
    }

    private void refreshWindowSwitch() {
//        mWindowSwitch.setChecked(SPHelper.isShowWindow(this));
//        if (getResources().getBoolean(R.bool.use_accessibility_service)) {
//            if (WatchingAccessibilityService.getInstance() == null) {
//                mWindowSwitch.setChecked(false);
//            }
//        }
    }

    private void refreshNotificationSwitch() {
//        if (mNotificationSwitch != null) {
//            mNotificationSwitch.setChecked(!SPHelper.isNotificationToggleEnabled(this));
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshWindowSwitch();
        refreshNotificationSwitch();
        NotificationActionReceiver.cancelNotification(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (getIntent().getBooleanExtra(EXTRA_FROM_QS_TILE, false)) {
//            mWindowSwitch.setChecked(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (SPHelper.isShowWindow(this) && !(getResources().getBoolean(R.bool.use_accessibility_service) && WatchingAccessibilityService.getInstance() == null)) {
            NotificationActionReceiver.showNotification(this, false);
        }
    }
}
