package com.hunter.appinfomonitor.ui;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.MainActivity;
import com.hunter.appinfomonitor.R;

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
            vh.icon.setImageDrawable(app.loadIcon(pm));
            vh.applable.setText("应用名：" + appInfoModel.getAppName());
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                vh.systemflag.setText("系统应用");
                vh.systemflag.setTextColor(Color.GRAY);
            } else {
                vh.systemflag.setText("用户应用");
                vh.systemflag.setTextColor(Color.BLUE);
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
    public void setFilter(int filterWay, String s1) {
        if (srcData.isEmpty()) {
            return;
        }
        //user,sytem切换
        if (filterWay == 0) {
            if (filterState == 0) {
                filterState = -1;
            } else {
                filterState = 0;
            }
        } else if (filterWay == 1) {
            filterState = filterWay;
            if (s1 == null) {
                s1 = "";
            }
        }
        data.clear();
        if (filterState == 0) {
            //用户应用
            for (AppInfoModel a : srcData) {
                if ((a.getApplicationInfo().flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    data.add(a);
                }
            }
        } else if (filterState == 1) {
            for (AppInfoModel a : srcData) {
                if (a.getAppName().contains(s1) || a.getPackageName().contains(s1)) {
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
                } catch (Exception e) {
                    Toast.makeText(context, "卸载错误：" + appInfoModel.getPackageName(), Toast.LENGTH_SHORT).show();
                }
            } else if (id == R.id.sign) {
                AppInfoModel appInfoModel = data.get(position);
                String temp =
                        SHAUtils.getSHA(SysUtils.getSignature(context, appInfoModel.getPackageName())).replace("-", "");
                String singn = MD5EncodeUtil.MD5Encode(temp);
                Toast.makeText(context, singn + "", Toast.LENGTH_LONG).show();
                Log.e("yodo1 sign", "yodo1_sign:  " + singn);
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
