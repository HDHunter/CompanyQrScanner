package com.hunter.appinfomonitor.yodo1page;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        PackageList.ListBean item = getItem(position);
        vh.appname.setText("发布渠道:" + item.getPublish_name() + "       v名:" + item.getGame_version() + "       v号:" + item.getVersion_code());
        vh.packageName.setText(item.getPackage_name() + "  创建时间:" + item.getCreate_date());
        vh.downloadlog.setText("完成时间:" + item.getFinish_date());
        vh.versionInfo.setText("打包用户: " + item.getUser_id());
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
        TextView appname;
        TextView packageName;
        TextView downloadlog;
        TextView versionInfo;
        DownloadButton button;
    }
}
