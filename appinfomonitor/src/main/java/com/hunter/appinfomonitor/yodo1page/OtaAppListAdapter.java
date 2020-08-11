package com.hunter.appinfomonitor.yodo1page;

import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;

public class OtaAppListAdapter extends BaseExpandableListAdapter {

    OtaAppListActivity mActivity;
    OtaAllAppListBean.DataBean.TeamsBean data;


    public OtaAppListAdapter(OtaAppListActivity otaAppListActivity, OtaAllAppListBean.DataBean.TeamsBean teamsData) {
        mActivity = otaAppListActivity;
        data = teamsData;
    }

    @Override
    public int getGroupCount() {
        return data.getApps().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return getGroup(groupPosition).getVersions().size();
    }

    @Override
    public OtaAllAppListBean.DataBean.TeamsBean.AppsBean getGroup(int groupPosition) {
        return data.getApps().get(groupPosition);
    }

    @Override
    public OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean getChild(int groupPosition, int childPosition) {
        return getGroup(groupPosition).getVersions().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_itaapp, null);
            vh = new VH();
            vh.icon = convertView.findViewById(R.id.appitem_iv);
            vh.appName = convertView.findViewById(R.id.item_appname);
            vh.packageName = convertView.findViewById(R.id.item_packagename);
            vh.versionInfo = convertView.findViewById(R.id.item_version);
            vh.downloadCount = convertView.findViewById(R.id.item_download);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        OtaAllAppListBean.DataBean.TeamsBean.AppsBean group = getGroup(groupPosition);
        Glide.with(convertView).load(group.getIcon()).into(vh.icon);
        vh.appName.setText(group.getAppName() + "             已传" + group.getVersions().size() + "个版本");
        vh.packageName.setText(group.getBundleId());
        vh.downloadCount.setText("总下载量:" + group.getTotalDownloadCount() + "               今日下载量:" + group.getTodayDownloadCount().getCount());
        vh.versionInfo.setText("当前v号:" + group.getCurrentVersion() + "        创建者:" + group.getCreator());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        VH vh;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_itaapp_child, null);
            vh = new VH();
            vh.appName = convertView.findViewById(R.id.item_appname);
            vh.packageName = convertView.findViewById(R.id.item_packagename);
            vh.packageName.setTextColor(Color.BLACK);
            vh.versionInfo = convertView.findViewById(R.id.item_version);
            vh.downloadCount = convertView.findViewById(R.id.item_download);
            vh.download = convertView.findViewById(R.id.item_download_btn);
            convertView.setTag(vh);
        } else {
            vh = (VH) convertView.getTag();
        }
        OtaAllAppListBean.DataBean.TeamsBean.AppsBean.VersionsBean child = getChild(groupPosition, childPosition);
        vh.appName.setText("v名:" + child.getVersionStr() + "       v号:" + child.getVersionCode() + "       上传者:" + child.getUploader());
        vh.packageName.setText("下载次数:" + child.getDownloadCount() + "         上传时间:" + child.getUploadAt().substring(0, 10));
        String obb = "";
        if (child.getObbSize() > 0) {
            obb = "     ObbSize:" + Formatter.formatFileSize(mActivity, child.getObbSize());
        }
        vh.downloadCount.setText("ApkSize:" + Formatter.formatFileSize(mActivity, child.getSize()) + obb);
        if (!TextUtils.isEmpty(child.getChangelog())) {
            vh.versionInfo.setVisibility(View.VISIBLE);
            vh.versionInfo.setText("备注:" + child.getChangelog());
        } else {
            vh.versionInfo.setVisibility(View.GONE);
        }
        vh.download.setData(child);
//        vh.packageName.setText(group.getBundleId());
//        vh.downloadCount.setText("下载量:" + group.getTotalDownloadCount() + " 今日下载:" + group.getTodayDownloadCount().getCount());
//        vh.versionInfo.setText("当前版本号:" + group.getCurrentVersion() + "  创建者:" + group.getCreator());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class VH {
        ImageView icon;
        TextView appName, versionInfo, packageName, downloadCount;
        DownloadButton download;
    }
}
