package com.hunter.appinfomonitor.yodo1page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hunter.appinfomonitor.BaseActvity;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.yodo1bean.OTALoginBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;

import java.util.ArrayList;
import java.util.List;

public class OtaListActivity extends BaseActvity {

    TextView content;
    ExpandableListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otaapplist_activity);

        content = findViewById(R.id.content0);
        listView = findViewById(R.id.expanded_listview);

        OTALoginBean loginBean = new OTALoginBean();
        OTALoginBean.DataBean data = new OTALoginBean.DataBean();
        loginBean.setData(data);
        List<OTALoginBean.DataBean.TeamsBean> teams = new ArrayList<>();
        data.setTeams(teams);
        ArrayList<String> stringlist = getIntent().getStringArrayListExtra("stringlist");
        if (stringlist == null) {
            finish();
            return;
        }
        for (String name : stringlist) {
            OTALoginBean.DataBean.TeamsBean bean = new OTALoginBean.DataBean.TeamsBean();
            teams.add(bean);
            bean.setName(name);
        }

        content.setText("总人数：" + stringlist.size());

        OtaAdapter otaAdapter = new OtaAdapter(OtaListActivity.this, new OtaAdapterBean(loginBean));
        listView.setAdapter(otaAdapter);
        listView.setOnItemClickListener(null);
        AppManager.getAppManager().addActivity(this);

        View viewById = findViewById(R.id.downloadmanager);
        viewById.setVisibility(View.VISIBLE);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(OtaListActivity.this, DownloadListActivity.class);
                startActivityForResult(intent, 1111);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }
}
