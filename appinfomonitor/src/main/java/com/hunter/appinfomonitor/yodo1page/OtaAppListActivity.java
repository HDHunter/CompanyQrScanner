package com.hunter.appinfomonitor.yodo1page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;

import java.io.Serializable;

public class OtaAppListActivity extends Activity {

    TextView content;
    ExpandableListView listView;
    OtaAllAppListBean.DataBean.TeamsBean teamsData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otaapplist_activity);


        content = findViewById(R.id.content0);
        listView = findViewById(R.id.expanded_listview);

        Serializable applist = getIntent().getSerializableExtra("applist");
        if (applist instanceof OtaAllAppListBean.DataBean.TeamsBean) {
            teamsData = (OtaAllAppListBean.DataBean.TeamsBean) applist;
            setData();
        } else {
            finish();
        }

        AppManager.getAppManager().addActivity(this);

        View viewById = findViewById(R.id.downloadmanager);
        viewById.setVisibility(View.VISIBLE);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtaAppListActivity.this, DownloadListActivity.class);
                startActivityForResult(intent, 1111);
            }
        });
    }

    private void setData() {
        content.setText("组织名称：" + teamsData.getName() + "\n" + "角色:" + teamsData.getRole() + "  拥有:" + teamsData.getApps().size() + "个应用。");
        OtaAppListAdapter adapter = new OtaAppListAdapter(this, teamsData);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
}
