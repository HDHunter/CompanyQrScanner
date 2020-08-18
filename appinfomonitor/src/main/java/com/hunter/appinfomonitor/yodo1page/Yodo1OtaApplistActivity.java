package com.hunter.appinfomonitor.yodo1page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.BaseActvity;
import com.hunter.appinfomonitor.LogUtils;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.GunqiuApi;
import com.hunter.appinfomonitor.network.okbiz.RxResponse;
import com.hunter.appinfomonitor.network.okbiz.RxResultHelper;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.ui.AppManager;
import com.hunter.appinfomonitor.ui.JsonUtils;
import com.hunter.appinfomonitor.ui.MD5EncodeUtil;
import com.hunter.appinfomonitor.ui.OtaAPi;
import com.hunter.appinfomonitor.yodo1bean.OTALoginBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAdapterBean;
import com.hunter.appinfomonitor.yodo1bean.OtaAllAppListBean;
import com.hunter.appinfomonitor.yodo1bean.OtaMemberListBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yodo1
 */
public class Yodo1OtaApplistActivity extends BaseActvity implements AdapterView.OnItemClickListener {

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
                Intent intent = new Intent(Yodo1OtaApplistActivity.this, DownloadListActivity.class);
                startActivityForResult(intent, 1111);
            }
        });

        View viewById1 = findViewById(R.id.gointo_pa);
        viewById1.setVisibility(View.VISIBLE);
        viewById1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palogin();
            }
        });
    }

    private void palogin() {
        JSONObject jsonObject = new JSONObject();
        String username = Yodo1SharedPreferences.getString(this, "username");
        String password = Yodo1SharedPreferences.getString(this, "password");
        try {
            long l = System.currentTimeMillis();
            String s = "" + l;
            jsonObject.put("password", password);
            jsonObject.put("timestamp", l);
            jsonObject.put("type", "ldap");
            jsonObject.put("username", username);
            jsonObject.put("loginFree", false);
            jsonObject.put("sign", MD5EncodeUtil.MD5Encode(username + password + "falseldap" + s + "yodo1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        GunqiuApi.getInstance().post(OtaAPi.palogin, jsonObject.toString()).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject jsonObject1 = new JSONObject(result);
                    Yodo1SharedPreferences.put(Yodo1OtaApplistActivity.this, "tokenpa", jsonObject1.getString("id_token"));

                    Intent intent = new Intent(Yodo1OtaApplistActivity.this, PaDownloadListActivity.class);
                    startActivityForResult(intent, 1112);
                } catch (JSONException e) {
                    onFailure(e);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(Yodo1OtaApplistActivity.this, "登录失败,msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
