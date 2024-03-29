package com.hunter.appinfomonitor.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.MainActivity;
import com.hunter.appinfomonitor.R;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class AppInfoAdapter extends RecyclerView.Adapter {


    private MainActivity context;
    private PackageManager pm;
    private List<AppInfoModel> data;
    private List<AppInfoModel> srcData;
    /**
     * -1,all app
     * 0,just user app
     * 1,search
     * other,all app
     */
    private int filterState = -1;


    public AppInfoAdapter(MainActivity mainActivity) {
        this.context = mainActivity;
        pm = mainActivity.getPackageManager();
        data = new ArrayList<>();
        srcData = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AppViewHolder(View.inflate(context, R.layout.appitem, null));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AppViewHolder) {
            AppInfoModel appInfoModel = data.get(position);
            AppViewHolder vh = (AppViewHolder) holder;
            vh.appName.setText("包名:" + appInfoModel.getPackageName());
            vh.packageName.setText("版本号:" + appInfoModel.getVersionCode() + "    版本名:" + appInfoModel.getVersionName());
            vh.versionCode.setText("");

            ApplicationInfo app = appInfoModel.getApplicationInfo();
            if (app != null) {
                vh.view.setVisibility(View.VISIBLE);
                vh.icon.setImageDrawable(app.loadIcon(pm));
                vh.applable.setText("应用名：" + appInfoModel.getAppName());
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    vh.systemflag.setText("系统应用");
                    vh.systemflag.setTextColor(Color.GRAY);
                } else {
                    vh.systemflag.setText("用户应用");
                    vh.systemflag.setTextColor(Color.BLUE);
                }
            } else {
                vh.view.setVisibility(View.INVISIBLE);
            }
            vh.position = position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<AppInfoModel> infos) {
        srcData.clear();
        srcData.addAll(infos);
        data.clear();
        data.addAll(srcData);
        notifyDataSetChanged();
    }

    /**
     * @param filterWay 切换方式
     * @param s1
     */
    public void setFilter(int filterWay, String[] s1) {
        if (srcData.isEmpty()) {
            return;
        }
        //user,sytem切换
        if (filterWay == 0) {
            if (filterState == 0) {
                Toast.makeText(context, "所有app列表", Toast.LENGTH_SHORT).show();
                filterState = -1;
            } else if (filterState == -1) {
                Toast.makeText(context, "系统app列表", Toast.LENGTH_SHORT).show();
                filterState = 2;
            } else {
                Toast.makeText(context, "用户app列表", Toast.LENGTH_SHORT).show();
                filterState = 0;
            }
        } else if (filterWay == 1) {
            filterState = filterWay;
            if (s1 == null) {
                s1 = new String[0];
            }
        }
        data.clear();
        if (filterState == 0) {
            //用户应用
            for (AppInfoModel a : srcData) {
                if (a.getApplicationInfo() != null && (a.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    data.add(a);
                }
            }
        } else if (filterState == 1) {
            for (AppInfoModel a : srcData) {
                for (String ss : s1) {
                    if (a.getAppName() != null && (a.getAppName().contains(ss) || a.getPackageName().contains(ss)) && !data.contains(a)) {
                        data.add(a);
                    }
                }
            }
        } else if (filterState == 2) {
            //系统应用
            for (AppInfoModel a : srcData) {
                if (a.getApplicationInfo() != null && (a.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    data.add(a);
                }
            }
        } else {
            data.addAll(srcData);
        }
        notifyDataSetChanged();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        ImageView icon;
        TextView applable;
        TextView appName;
        TextView packageName;
        TextView versionCode;
        TextView systemflag, sign;
        TextView luacher, delete;
        int position;

        public AppViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            icon = view.findViewById(R.id.imageview);
            appName = view.findViewById(R.id.appname);
            packageName = view.findViewById(R.id.packagename);
            versionCode = view.findViewById(R.id.versioncode);
            systemflag = view.findViewById(R.id.systemflag);
            applable = view.findViewById(R.id.applable);
            luacher = view.findViewById(R.id.launcher);
            delete = view.findViewById(R.id.delete);
            sign = view.findViewById(R.id.sign);

            luacher.setOnClickListener(this);
            view.setOnClickListener(this);
            appName.setOnClickListener(this);
            packageName.setOnClickListener(this);
            versionCode.setOnClickListener(this);
            systemflag.setOnClickListener(this);
            applable.setOnClickListener(this);
            delete.setOnClickListener(this);
            sign.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.launcher) {
                AppInfoModel appInfoModel = data.get(position);
                try {
                    Intent launchIntentForPackage = pm.getLaunchIntentForPackage(appInfoModel.getPackageName());
                    launchIntentForPackage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(launchIntentForPackage);
                } catch (Exception e) {
                    Toast.makeText(context, "没有Laucher: " + appInfoModel.getPackageName(), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.delete) {
                AppInfoModel appInfoModel = data.get(position);
                try {
                    Uri uri = Uri.fromParts("package", appInfoModel.getPackageName(), null);
                    Intent intent = new Intent(Intent.ACTION_DELETE, uri);
                    context.startActivity(intent);
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.REQUEST_DELETE_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.REQUEST_DELETE_PACKAGES}, 545242);
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "卸载错误：" + appInfoModel.getPackageName(), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.sign) {
                AppInfoModel appInfoModel = data.get(position);
                Signature[] signatures = SysUtils.getSignature(context, appInfoModel.getPackageName());

                String md5 = "MD5: " + SHAUtils.signatureMD5(signatures);
                String sha1 = "SHA1: " + SHAUtils.signatureSHA1(signatures);
                String sha256 = "SHA256: " + SHAUtils.signatureSHA256(signatures);

                Toast.makeText(context, md5 + "\n" + sha1 + "\n" + sha256, Toast.LENGTH_LONG).show();
                Log.e("yodo1 sign", "******************************************************************************");
                Log.e("yodo1 sign", md5);
                Log.e("yodo1 sign", sha1);
                Log.e("yodo1 sign", sha256);
                try {
                    for (int i = 0, c = signatures.length; i < c; i++) {
                        CertificateFactory cf = CertificateFactory.getInstance("X.509");
                        ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                        X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
                        String pubKey = cert.getPublicKey().toString();   //公钥
                        String signNumber = cert.getSerialNumber().toString();
                        LogUtils.e("yodo1 sign", "signName签名算法名: " + cert.getSigAlgName());//算法名
                        LogUtils.e("yodo1 sign", "pubKey公钥: " + pubKey);
                        LogUtils.e("yodo1 sign", "signNumber证书序列编号: " + signNumber);
                        LogUtils.e("yodo1 sign", "subjectDNName: " + cert.getSubjectDN().getName());
                        LogUtils.e("yodo1 sign", "subjectDNString: " + cert.getSubjectDN().toString());
                        LogUtils.e("yodo1 sign", "before签名创建时间: " + cert.getNotBefore());
                        LogUtils.e("yodo1 sign", "after签名失效时间: " + cert.getNotAfter());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("yodo1 sign", "******************************************************************************");
            } else {
                AppInfoModel appInfoModel = data.get(position);
                Intent mIntent = new Intent();
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                mIntent.setData(Uri.fromParts("package", appInfoModel.getPackageName(), null));
                context.startActivity(mIntent);
            }
        }
    }
}
