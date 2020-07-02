package com.hunter.appinfomonitor.yodo1page;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;

import java.util.ArrayList;
import java.util.List;

public class OtaAdapter extends BaseExpandableListAdapter {

    private final Activity mActivity;
    private OtaAdapterBean mList;


    public OtaAdapter(Activity yodo1OtaApplistActivity, OtaAdapterBean loginBean) {
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.otaadapter_groupview, null);
            TextView name = convertView.findViewById(R.id.teamname);
            TeamBean tb = new TeamBean();
            tb.name = name;
            tb.role = convertView.findViewById(R.id.teamnamerole);
            tb.count = convertView.findViewById(R.id.teammemebercount);
            convertView.setTag(tb);
        }
        TeamBean vh = (TeamBean) convertView.getTag();
        OtaAdapterBean.TeamsGroupBean group = getGroup(groupPosition);
        vh.name.setText(group.getName());
        vh.role.setText(group.getRole());
        final List<String> members = group.getMembers();
        if (members == null || members.size() <= 0) {
            vh.count.setVisibility(View.GONE);
        } else {
            vh.count.setVisibility(View.VISIBLE);
            vh.count.setText("共有:" + members.size() + "人");
            vh.count.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<String> lists = new ArrayList<>();
                    for (String l : members) {
                        lists.add(l);
                    }
                    Intent intent = new Intent(mActivity, OtaListActivity.class);
                    intent.putStringArrayListExtra("stringlist", lists);
                    mActivity.startActivity(intent);
                }
            });
        }
        if (mActivity instanceof Yodo1OtaApplistActivity) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Yodo1OtaApplistActivity) mActivity).onItemClick(null, null, groupPosition, groupPosition);
                }
            });
        }
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
        TextView role;
        Button count;
    }
}
