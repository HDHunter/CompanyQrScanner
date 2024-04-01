package com.hunter.appinfomonitor.yodo1page;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.AdvertisingIdClient;
import com.hunter.appinfomonitor.BaseActvity;
import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.GunqiuApi;
import com.hunter.appinfomonitor.network.okbiz.RxResponse;
import com.hunter.appinfomonitor.network.okbiz.RxResultHelper;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.ui.JsonUtils;
import com.hunter.appinfomonitor.ui.OtaAPi;
import com.hunter.appinfomonitor.ui.SysUtils;
import com.hunter.appinfomonitor.yodo1bean.DemoHelper;
import com.hunter.appinfomonitor.yodo1bean.OTALoginBean;
import com.hunter.appinfomonitor.yodo1bean.PhoneUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author yodo1
 */
public class Yodo1Activity extends BaseActvity {


    private TextView deviceIdImeiValue, deviceModleValue, console, otaname, otapwd;
    private ProgressDialog progressDialog;
    private TextView deviceIdGaidValue, deviceIdOaidValue;
    private EditText input;
    private AsyncTask<Void, Void, String> taskGaid = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            AdvertisingIdClient.AdvertisingConnection connection = new AdvertisingIdClient.AdvertisingConnection();
            Intent intent = new Intent("com.google.android.gms.ads.identifier.service.START");
            intent.setPackage("com.google.android.gms");
            if (Yodo1Activity.this.bindService(intent, connection, Context.BIND_AUTO_CREATE)) {
                try {
                    AdvertisingIdClient.AdvertisingInterface adInterface = new AdvertisingIdClient.AdvertisingInterface(
                            connection.getBinder());
                    return adInterface.getId();
                } catch (Exception e) {
                    Yodo1Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(Yodo1Activity.this, "gms bind exception.google服务连接异常", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    Yodo1Activity.this.unbindService(connection);
                }
            } else {
                Yodo1Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Yodo1Activity.this, "gms not found.手机没有google服务", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return "";
        }

        @Override
        protected void onPostExecute(String advertId) {
            if (!TextUtils.isEmpty(advertId)) {
                deviceIdGaidValue.setText(advertId);
            } else {
                deviceIdGaidValue.setText(null);
                deviceIdGaidValue.setHint("无法获取");
            }
        }

    };
    private AsyncTask<Void, Void, String> taskOaid = new AsyncTask<Void, Void, String>() {
        @Override
        protected String doInBackground(Void... params) {
            return "";
        }

        @Override
        protected void onPostExecute(String advertId) {
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yodo1activity);
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        deviceIdImeiValue = findViewById(R.id.device_id);
        deviceModleValue = findViewById(R.id.device_model_id);
        deviceIdOaidValue = findViewById(R.id.deviceid3);
        deviceIdGaidValue = findViewById(R.id.deviceid2);
        otaname = findViewById(R.id.otaname);
        otapwd = findViewById(R.id.otapwd);
        taskGaid.execute();
        taskOaid.execute();
        deviceModleValue.setText(android.os.Build.MODEL);
        deviceModleValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipData mClipData = ClipData.newPlainText("Label", deviceModleValue.getText());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Yodo1Activity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                hideInput();
            }
        });
        console = findViewById(R.id.console);
        findViewById(R.id.godeep).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText deeplink = findViewById(R.id.deeplink);
                String s = deeplink.getText().toString();
                if (!TextUtils.isEmpty(s)) {
                    try {
                        Uri uri = Uri.parse(s);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(Yodo1Activity.this, "输入地址有问题。", Toast.LENGTH_SHORT).show();
                    }
                }
                hideInput();
            }
        });
        findViewById(R.id.copymas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImi();
                ClipData mClipData = ClipData.newPlainText("Label", deviceIdGaidValue.getText());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Yodo1Activity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                hideInput();
            }
        });
        findViewById(R.id.copydevicei3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImi();
                ClipData mClipData = ClipData.newPlainText("Label", deviceIdOaidValue.getText());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Yodo1Activity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                hideInput();
            }
        });
        findViewById(R.id.copydeviceid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImi();
                ClipData mClipData = ClipData.newPlainText("Label", deviceIdImeiValue.getText());
                cm.setPrimaryClip(mClipData);
                Toast.makeText(Yodo1Activity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                hideInput();
            }
        });
        findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImi();
            }
        });
        input = findViewById(R.id.yid);
        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(Yodo1Activity.this, "请输入yid", Toast.LENGTH_SHORT).show();
                } else {
                    request(input.getText());
                }
                hideInput();
            }
        });
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (TextUtils.isEmpty(input.getText())) {
                    Toast.makeText(Yodo1Activity.this, "请输入yid", Toast.LENGTH_SHORT).show();
                } else {
                    request(input.getText());
                }
                hideInput();
                return actionId != EditorInfo.IME_ACTION_GO;
            }
        });
        findViewById(R.id.ota_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sn = otaname.getText().toString();
                String sp = otapwd.getText().toString();
                if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(sp)) {
                    Toast.makeText(Yodo1Activity.this, "format not right.", Toast.LENGTH_SHORT).show();
                } else {
                    otaLogin(sn, sp);
                }
            }
        });
        findViewById(R.id.ota_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Yodo1Activity.this, DownloadListActivity.class);
                startActivityForResult(intent, 1111);
            }
        });
        findViewById(R.id.ota_downloadm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Yodo1Activity.this, DownloadListActivity.class);
                startActivityForResult(intent, 1111);
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String username = Yodo1SharedPreferences.getString(Yodo1Activity.this, "username");
                String password = Yodo1SharedPreferences.getString(Yodo1Activity.this, "password");
                otaname.setText(username);
                otapwd.setText(password);
            }
        });
        TextView info = findViewById(R.id.app_info);
        info.setText("版本号:" + SysUtils.getVersionCode(this) + " 版本名称:" + SysUtils.getVersionName(this));
        AppManager.getAppManager().addActivity(this);
    }

    private void otaLogin(String sn, String sp) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", sn);
            jsonObject.put("password", sp);
            jsonObject.put("isLdap", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Yodo1SharedPreferences.put(this, "username", sn);
        Yodo1SharedPreferences.put(this, "password", sp);
        GunqiuApi.getInstance().post(OtaAPi.login, jsonObject.toString()).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                OTALoginBean otaLoginBean = JsonUtils.fromJson(result, OTALoginBean.class);
                if (otaLoginBean.isSuccess()) {
                    Yodo1SharedPreferences.put(Yodo1Activity.this, "token", otaLoginBean.getData().getToken());

                    Intent intent = new Intent(Yodo1Activity.this, Yodo1OtaApplistActivity.class);
                    intent.putExtra("logindata", otaLoginBean);
                    startActivity(intent);
                } else {
                    Toast.makeText(Yodo1Activity.this, "登录失败,检查网络和账户密码", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(Yodo1Activity.this, "登录失败,检查网络和账户密码", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void request(final CharSequence text) {
        loading();
        String url = "http://yodo1fc.yodo1api.com/common-deal";
        HashMap<String, String> maps = new HashMap<>();
        maps.put("yid", text.toString());
        GunqiuApi.getInstance().get(url, maps).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                String re = "yid:" + text.toString() + "\nresponseString:\n" + result;
                Log.e("yodo1", re);
                console.setText(re);
                console.setTextColor(Color.BLACK);
                hideLoading();
                Toast.makeText(Yodo1Activity.this, "请求成功。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable e) {
                String re = "yid:" + text.toString() + "\nresponseString:\n" + e.getMessage();
                Log.e("yodo1", re);
                console.setText(re);
                console.setTextColor(Color.RED);
                hideLoading();
                Toast.makeText(Yodo1Activity.this, "请求失败。", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestImi() {
        if (TextUtils.isEmpty(deviceIdImeiValue.getText())) {
            ActivityCompat.requestPermissions(Yodo1Activity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 44);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        deviceIdImeiValue.setText(PhoneUtil.getIMEI(this));
        new DemoHelper(new DemoHelper.AppIdsUpdater() {
            @Override
            public void OnIdsAvalid(@NonNull String ids) {
                LogUtils.e("Oaid", ids);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceIdOaidValue.setText(ids);
                    }
                });
            }
        }).getDeviceIds(Yodo1Activity.this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this, "赋予权限，请重新启动", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (taskGaid != null && !taskGaid.isCancelled()) {
            taskGaid.cancel(true);
        }
        if (taskOaid != null && !taskOaid.isCancelled()) {
            taskOaid.cancel(true);
        }
        AppManager.getAppManager().removeActivity(this);
        taskGaid = null;
        taskOaid = null;
    }

    public void loading() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.show();
    }

    public void hideLoading() {
        input.setText("");
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

}
