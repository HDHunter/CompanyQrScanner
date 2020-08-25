package com.hunter.appinfomonitor.yodo1page;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;

import java.util.ArrayList;
import java.util.List;

public class PaDownloadItemAdapter extends BaseAdapter {

    private List<PackageList.ListBean> mData;
    private PaDownloadListActivity context;

    public PaDownloadItemAdapter(PaDownloadListActivity paDownloadListActivity) {
        mData = new ArrayList<>();
        context = paDownloadListActivity;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public PackageList.ListBean getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_itam_pa_app_child, null);
            vh = new VH();
            vh.appname = convertView.findViewById(R.id.item_appname);
            vh.packageName = convertView.findViewById(R.id.item_packagename);
            vh.downloadlog = convertView.findViewById(R.id.item_download);
            vh.versionInfo = convertView.findViewById(R.id.item_version);
            vh.button = convertView.findViewById(R.id.item_download_btn);
            vh.infobtn = convertView.findViewById(R.id.infobtn);
            vh.share = convertView.findViewById(R.id.item_itemapp_share);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        PackageList.ListBean item = getItem(position);
        vh.appname.setText("发布渠道: " + item.getPublish_name() + "       版本名称: " + item.getGame_version() + "       版本号: " + item.getVersion_code());
        vh.packageName.setText("包名:" + item.getPackage_name() + "  创建时间:" + item.getCreate_date().substring(0, 19).replace("T", " "));
        vh.downloadlog.setText("完成时间:" + item.getFinish_date().substring(0, 19).replace("T", " ") + "        打包用户: " + item.getUser_id());
        vh.versionInfo.setVisibility(View.GONE);
        vh.infobtn.setTag(item);
        vh.infobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageList.ListBean item = (PackageList.ListBean) v.getTag();

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setTitle(Uri.parse(item.getAddress()).getLastPathSegment());
                StringBuilder sb = new StringBuilder();
                List<PackageList.ListBean.SdkLstBean> sdk_lst = item.getSdk_lst();
                for (PackageList.ListBean.SdkLstBean b : sdk_lst) {
                    sb.append(b.getLabel() + " ");
                    sb.append(b.getSdk() + " ");
                    sb.append(b.getVersion());
                    sb.append("\n");
                }
                dialog.setMessage(sb.toString());
                dialog.create().show();
            }
        });
        vh.share.setTag(item);
        vh.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageList.ListBean ch = (PackageList.ListBean) v.getTag();
                String shareText = ch.getAddress();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                context.startActivity(Intent.createChooser(intent, "分享下载链接"));
            }
        });
        OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean versionsBean = new OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean();
        versionsBean.setDownloadUrl(item.getAddress());

        versionsBean.set_id(item.get_id());
        versionsBean.setBundleId(item.getPackage_name());
        versionsBean.setVersionCode(item.getVersion_code());
        versionsBean.setVersionStr(item.getGame_version());
        versionsBean.setAppId(item.getAppkey());
        versionsBean.setSize(500000000);
        vh.button.setData(versionsBean);
        return convertView;
    }

    public void setData(List<PackageList.ListBean> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }


    static class VH {
        TextView appname,share;
        TextView packageName;
        TextView downloadlog;
        TextView versionInfo;
        DownloadButton button;
        TextView infobtn;
    }
}
