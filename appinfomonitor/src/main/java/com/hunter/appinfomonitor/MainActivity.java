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
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.floatui.NotificationActionReceiver;
import com.hunter.appinfomonitor.floatui.SPHelper;
import com.hunter.appinfomonitor.floatui.TasksWindow;
import com.hunter.appinfomonitor.floatui.WatchingAccessibilityService;
import com.hunter.appinfomonitor.ui.AppInfoAdapter;
import com.hunter.appinfomonitor.ui.AppInfoModel;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.yodo1page.DownloadServerice;
import com.hunter.appinfomonitor.yodo1page.Yodo1Activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActvity {

    public static final String ACTION_STATE_CHANGED = BuildConfig.APPLICATION_ID + ".ACTION_STATE_CHANGED";
    public static final String EXTRA_FROM_QS_TILE = "from_qs_tile";
    private static boolean openfloat = true;
    private AppInfoAdapter adapter;
    private UpdateSwitchReceiver mReceiver;
    private TextView info;

    public static boolean hasNfc(Context context) {
        boolean bRet = false;
        if (context == null)
            return bRet;
        NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
        if (manager == null) {
            return false;
        }
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter != null && adapter.isEnabled()) {
            // adapter存在，能启用
            bRet = true;
        }
        return bRet;
    }

    /**
     * 从SD卡上读取文件内容
     *
     * @param fileName fileName
     * @return content
     */
    public static String readFilesFromSDCard(String fileName) {

        // 定义文件内容字符串
        String content = null;
        // 文件输入流
        FileInputStream fileInputStream;

        // 判断SD卡是否存在，并且本程序是否拥有SD卡的权限
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            // 获得SD卡的根目录
            File sdCardPath = Environment.getExternalStorageDirectory();
            /*
             * 文件输出操作
             * */
            try {
                File testFile = new File(sdCardPath, fileName);
                // 打开文件输入流
                fileInputStream = new FileInputStream(testFile);
                // 将文件输入流存放在ByteArrayOutputStream
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                // 定义每次读取一个字节
                byte[] buffer = new byte[1024];
                // 定义每次读取的字节长度
                int len;
                // 读取文件输入流的内容，并存入ByteArrayOutputStream中
                while ((len = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                // 将文件输入流数据以字符串的形式存放
                content = outputStream.toString();
                // 关闭文件输入流
                fileInputStream.close();
                // 关闭ByteArrayOutputStream
                outputStream.close();
            } catch (Exception e) {
                Log.e("yodo1 appInfo", "yodo1 缺少SD卡权限  读取文件失败");
            }
        }

        // 返回文件内容
        return content;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainactivity);

        AppManager.getAppManager().addActivity(this);

        final RecyclerView recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);

        adapter = new AppInfoAdapter(this);
        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, 0);
        recyclerView.addItemDecoration(dividerItemDecoration);
        //系统用户app切换
        findViewById(R.id.togglebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setFilter(0, null);
            }
        });
        info = findViewById(R.id.infoshow);
        //输入框
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
                adapter.setFilter(1, new String[]{s1});
            }
        });
        searc.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String s1 = v.getText().toString();
                    adapter.setFilter(1, new String[]{s1});
                    return true;
                } else {
                    return false;
                }
            }
        });
        //浮框界面
        TextView floatBtn = findViewById(R.id.togglebutton2);
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
        //yodo1 apps
        findViewById(R.id.togglebutton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.setFilter(1, new String[]{"yodo1", "steppypants"});
            }
        });
        //写日志
        //系统信息
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
        //关日志
        findViewById(R.id.togglebutton6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 55);
                    return;
                }
                // 定义文件内容字符串
                final String adsFlag = ".yodo1ads";
                final String adsFlag1 = ".yodo1";
                String str = readFilesFromSDCard(adsFlag);
                String str1 = readFilesFromSDCard(adsFlag1);
                if (!TextUtils.isEmpty(str)) {
                    Log.e("文件.yodo1ads内容：", str);
                }
                if (!TextUtils.isEmpty(str1)) {
                    Log.e("文件.yodo1内容：", str1);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(".yodo1文件内容");
                try {
                    builder.setMessage("文件.yodo1ads内容:" + str + "\n文件.yodo1内容:" + str1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                builder.setPositiveButton("全部删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            try {
                                File testFile = new File(Environment.getExternalStorageDirectory(), adsFlag);
                                if (testFile.exists()) {
                                    boolean delete = testFile.delete();
                                    Log.e("zzzzzzzzz", adsFlag + "    删除" + (delete ? "成功" : "失败"));
                                }
                                File testFile1 = new File(Environment.getExternalStorageDirectory(), adsFlag1);
                                if (testFile1.exists()) {
                                    boolean delete = testFile1.delete();
                                    Log.e("zzzzzzzzz", adsFlag1 + "    删除" + (delete ? "成功" : "失败"));
                                }
                            } catch (Exception e) {
                                Log.d("zzzzzz", "yodo1 缺少SD卡权限  读取文件失败");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
                                }
                            }
                        }

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
        }, 0);
        if (getResources().getBoolean(R.bool.use_watching_service)) {
            TasksWindow.show(this, "");
            startService(new Intent(this, WatchingService.class));
        }

        mReceiver = new UpdateSwitchReceiver();
        registerReceiver(mReceiver, new IntentFilter(ACTION_STATE_CHANGED));
        Toast.makeText(this, "正在读取本机中的应用,loading...", Toast.LENGTH_SHORT).show();

        findViewById(R.id.gotoyodo1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Yodo1Activity.class));
            }
        });
        findViewById(R.id.gotoyodo2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                if (ContextCompat.checkSelfPermission(MainActivity.this, perms[0]) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(MainActivity.this, perms[0]) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, Yodo1QrCodeActivity.class));
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, perms, 111);
                }
            }
        });
        View nfc = findViewById(R.id.gotoyodo3);
        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasNfc(v.getContext())) {
                    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.NFC};
                    if (ContextCompat.checkSelfPermission(MainActivity.this, perms[0]) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(MainActivity.this, perms[0]) == PackageManager.PERMISSION_GRANTED) {
                        startActivity(new Intent(MainActivity.this, NFCActivity.class));
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, perms, 111);
                    }
                } else {
                    Toast.makeText(v.getContext(), "手机不支持NFC功能", Toast.LENGTH_SHORT).show();
                }
            }
        });
        nfc.setVisibility(View.GONE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(this, DownloadServerice.class));
        } else {
            startService(new Intent(this, DownloadServerice.class));
        }

        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipte_refreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
                setData();
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.QUERY_ALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.QUERY_ALL_PACKAGES}, 43);
                }
            }
        });
    }

    private void setData() {
        int yodo1Count = 0, userAppCount = 0;
        PackageManager pm = getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
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
            if (info.packageName.contains("com.yodo1")) {
                yodo1Count += 1;
            }
            if ((apI.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                userAppCount += 1;
            }
        }
        //空白
        AppInfoModel appInfoModel = new AppInfoModel();
        infos.add(appInfoModel);

        adapter.setData(infos);
        info.setText("All:" + infos.size() + "个,User:" + userAppCount + "个,yodo1:" + yodo1Count + "个。");
    }

    private String updateInfos() {
        StringBuilder sb = new StringBuilder();
        StringBuilderPrinter ps = new StringBuilderPrinter(sb);
        String display = "Display:" + Build.DISPLAY;
        ps.println(display);
        String print = "FingerPrint:" + Build.FINGERPRINT;
        ps.println(print);
        ps.println("");
        String hardware = "HardWare:" + Build.HARDWARE;
        ps.println(hardware);
        String host = "Host:" + Build.HOST;
        ps.println(host);
        String id = "ID:" + Build.ID;
        ps.println(id);
        String Manufacturer = "Manufacturer:" + Build.MANUFACTURER;
        ps.println(Manufacturer);
        String model = "Model:" + Build.MODEL;
        ps.println(model);
        String brand = "brand:" + Build.BRAND;
        ps.println(brand);
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
    public void onResume() {
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
        AppManager.getAppManager().removeActivity(this);
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (SPHelper.isShowWindow(this) && !(getResources().getBoolean(R.bool.use_accessibility_service) && WatchingAccessibilityService.getInstance() == null)) {
            NotificationActionReceiver.showNotification(this, false);
        }
    }

    class UpdateSwitchReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            refreshWindowSwitch();
            refreshNotificationSwitch();
        }
    }
}
