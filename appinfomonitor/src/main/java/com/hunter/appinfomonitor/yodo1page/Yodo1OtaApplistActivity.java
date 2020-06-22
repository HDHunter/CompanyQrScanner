package com.hunter.appinfomonitor.yodo1page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.yodo1bean.OTALoginBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;

import java.io.Serializable;

/**
 * @author yodo1
 */
public class Yodo1OtaApplistActivity extends AppCompatActivity {

    OTALoginBean loginBean;
    TextView content;
    ExpandableListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otaapplist_activity);
        Serializable logindata = getIntent().getSerializableExtra("logindata");
        if (logindata instanceof OTALoginBean) {
            loginBean = (OTALoginBean) logindata;
        }
        if (loginBean == null) {
            finish();
            return;
        }
        content = findViewById(R.id.content0);
        listView = findViewById(R.id.expanded_listview);
        content.setText(loginBean.getData().getUsername() + "  " + loginBean.getData().getEmail());
        OtaAdapter otaAdapter = new OtaAdapter(Yodo1OtaApplistActivity.this, new OtaAdapterBean(loginBean));
        listView.setAdapter(otaAdapter);
    }
}
