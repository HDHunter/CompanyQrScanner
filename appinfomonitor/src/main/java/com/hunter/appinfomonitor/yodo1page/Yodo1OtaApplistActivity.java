package com.hunter.appinfomonitor.yodo1page;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.GunqiuApi;
import com.hunter.appinfomonitor.network.okbiz.RxResponse;
import com.hunter.appinfomonitor.network.okbiz.RxResultHelper;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.ui.JsonUtils;
import com.hunter.appinfomonitor.ui.OtaAPi;
import com.hunter.appinfomonitor.yodo1bean.OTALoginBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;
import com.hunter.appinfomonitor.yodo1bean.OtaMemberListBean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yodo1
 */
public class Yodo1OtaApplistActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    OTALoginBean loginBean;
    TextView content;
    ExpandableListView listView;
    OtaAdapter otaAdapter;
    private List<OtaAllAppListBean.DataBean.TeamsBean> allAppList;

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
        content.setText("ota账户：" + loginBean.getData().getUsername() + "\nota邮箱:" + loginBean.getData().getEmail());
        otaAdapter = new OtaAdapter(Yodo1OtaApplistActivity.this, new OtaAdapterBean(loginBean));
        listView.setAdapter(otaAdapter);
        listView.setOnItemClickListener(this);

        getTeamsInfos();

        AppManager.getAppManager().addActivity(this);

        View viewById = findViewById(R.id.downloadmanager);
        viewById.setVisibility(View.VISIBLE);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File sdCardPath = new File(Environment.getExternalStorageDirectory(), "yodo1");
                Uri uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:yodo1");
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "file/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Yodo1OtaApplistActivity.this, "sth error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getTeamsInfos() {
        final List<OTALoginBean.DataBean.TeamsBean> teams = loginBean.getData().getTeams();
        int timeDuration = 0;//ms
        for (final OTALoginBean.DataBean.TeamsBean team : teams) {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    GunqiuApi.getInstance().get(OtaAPi.membersList.replace("{}", team.get_id())).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
                        @Override
                        public void onSuccess(String result) {
                            OtaMemberListBean otaLoginBean = JsonUtils.fromJson(result, OtaMemberListBean.class);
                            if (otaLoginBean.isSuccess()) {
                                List<OtaMemberListBean.DataBean.MembersBean> members = otaLoginBean.getData().getMembers();
                                ArrayList<String> name = new ArrayList<>();
                                for (OtaMemberListBean.DataBean.MembersBean b : members) {
                                    name.add(b.getUsername() + "\n" + b.getEmail() + "     " + b.getRole());
                                }
                                for (OTALoginBean.DataBean.TeamsBean team : teams) {
                                    if (TextUtils.equals(team.get_id(), otaLoginBean.getData().get_id())) {
                                        team.setMembers(name);
                                        break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e) {
                            Toast.makeText(Yodo1OtaApplistActivity.this, "teams 请求失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }, timeDuration);
            timeDuration += 150;
        }
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                otaAdapter = new OtaAdapter(Yodo1OtaApplistActivity.this, new OtaAdapterBean(loginBean));
                listView.setAdapter(otaAdapter);
                getAppList();
            }
        }, timeDuration + 100);
    }

    private void getAppList() {
        GunqiuApi.getInstance().get(OtaAPi.allAppList).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                OtaAllAppListBean otaLoginBean = JsonUtils.fromJson(result, OtaAllAppListBean.class);
                if (otaLoginBean.isSuccess()) {
                    allAppList = otaLoginBean.getData().getTeams();
                    otaAdapter.addVersionCount(allAppList);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(Yodo1OtaApplistActivity.this, "appList请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().removeActivity(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        LogUtils.e("onItemClick", "click:position:" + position + " id:" + id);
        if (allAppList != null && position < allAppList.size()) {
            Intent intent = new Intent(this, OtaAppListActivity.class);
            intent.putExtra("applist", allAppList.get(position));
            startActivity(intent);
        } else {
            Toast.makeText(this, "try later.", Toast.LENGTH_SHORT).show();
        }
    }
}
