package com.hunter.appinfomonitor.yodo1page;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;

public class OtaAdapter extends BaseExpandableListAdapter {

    private final Yodo1OtaApplistActivity mActivity;
    private OtaAdapterBean mList;


    public OtaAdapter(Yodo1OtaApplistActivity yodo1OtaApplistActivity, OtaAdapterBean loginBean) {
        this.mActivity = yodo1OtaApplistActivity;
        mList = loginBean;
    }

    @Override
    public int getGroupCount() {
        return mList.getTeams().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public OtaAdapterBean.TeamsGroupBean getGroup(int groupPosition) {
        return mList.getTeams().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * 100 + childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.otaadapter_groupview, null);
            TextView name = convertView.findViewById(R.id.teamname);
            TeamBean tb = new TeamBean();
            tb.name = name;
            convertView.setTag(tb);
        }
        TeamBean vh = (TeamBean) convertView.getTag();
        OtaAdapterBean.TeamsGroupBean group = getGroup(groupPosition);
        vh.name.setText(group.getName());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public class TeamBean {
        TextView name;
    }
}
