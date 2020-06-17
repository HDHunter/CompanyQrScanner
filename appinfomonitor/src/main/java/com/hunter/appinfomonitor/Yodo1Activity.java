package com.hunter.appinfomonitor;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.http.ApiRequest;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class Yodo1Activity extends AppCompatActivity {


    private TextView tv, tv2, console;
    private ProgressDialog progressDialog;
    private EditText input;
    private AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
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
                            Toast.makeText(Yodo1Activity.this, "gms not found.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } finally {
                    Yodo1Activity.this.unbindService(connection);
                }
            }
            return "";
        }

        @Override
        protected void onPostExecute(String advertId) {
            if (!TextUtils.isEmpty(advertId)) {
                tv.setText(advertId);
            } else {
                tv.setText(getIMEI(Yodo1Activity.this));
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yodo1activity);
        final ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        tv = findViewById(R.id.device_id);
        tv2 = findViewById(R.id.device_model_id);

        task.execute();
        tv2.setText(android.os.Build.MODEL);
        console = findViewById(R.id.console);
        findViewById(R.id.copymas).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestImi();
                ClipData mClipData = ClipData.newPlainText("Label", tv.getText());
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
    }

    private void request(final CharSequence text) {
        loading();
        String url = "http://yodo1fc.yodo1api.com/common-deal";
        RequestParams params = new RequestParams();
        params.put("p", text.toString());
        ApiRequest.getClient().get(url, params, new TextHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                String re = "yid:" + text.toString() + "\nstatusCode:" + statusCode + "\nresponseString:\n" + responseString;
                Log.e("yodo1", re);
                console.setText(re);
                console.setTextColor(Color.BLACK);
                hideLoading();
                Toast.makeText(Yodo1Activity.this, "请求成功。", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                String re = "yid:" + text.toString() + "\nstatusCode:" + statusCode + "\nresponseString:\n" + responseString;
                Log.e("yodo1", re);
                console.setText(re);
                console.setTextColor(Color.RED);
                hideLoading();
                Toast.makeText(Yodo1Activity.this, "请求失败。", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void requestImi() {
        if (TextUtils.isEmpty(tv.getText())) {
            ActivityCompat.requestPermissions(Yodo1Activity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 44);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (task != null) {
            task.execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null && !task.isCancelled()) {
            task.cancel(true);
        }
        task = null;
    }

    private String getIMEI(Context context) {
        String imei = "";
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                imei = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(imei) || "0".equals(imei) || "000000000000000".equals(imei)) {
                    imei = "";
                }
            }
        }
        return imei;
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
