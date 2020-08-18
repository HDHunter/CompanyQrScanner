package com.hunter.appinfomonitor.yodo1page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.BaseActvity;
import com.hunter.appinfomonitor.R;
import com.hunter.appinfomonitor.network.okbiz.GunqiuApi;
import com.hunter.appinfomonitor.network.okbiz.RxResponse;
import com.hunter.appinfomonitor.network.okbiz.RxResultHelper;
import com.hunter.appinfomonitor.network.okbiz.Yodo1SharedPreferences;
import com.hunter.appinfomonitor.ui.JsonUtils;
import com.hunter.appinfomonitor.ui.MD5EncodeUtil;
import com.hunter.appinfomonitor.ui.OtaAPi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * @author yodo1
 */
public class PaDownloadListActivity extends BaseActvity {

    private TextView gameSelect, channelSelect, versionSelect;
    private ListView lv;
    private PaDownloadItemAdapter adapter;
    private GameList mGameData;//游戏列表
    private ChannelList mChannelData;//渠道列表

    private VersionList mVersionData;//版本列表。
    private PackageList mPackageData;//包列表。

    private String selectGame, selectVersion, selectChannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padownloadlist);

        gameSelect = findViewById(R.id.gameselect);
        channelSelect = findViewById(R.id.channelselect);
        versionSelect = findViewById(R.id.versionselect);
        lv = findViewById(R.id.pa_download_listview);

        adapter = new PaDownloadItemAdapter(this);
        lv.setAdapter(adapter);

        initData();
        gameSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaDownloadListActivity.this, PaDownloadSelectListActivity.class);
                intent.setType("game");

                ArrayList<String> lists = new ArrayList<>();
                List<GameList.GamesBean> games = mGameData.getGames();
                for (GameList.GamesBean gamesBean : games) {
                    lists.add(gamesBean.getName().getCh() + " " + gamesBean.getName().getEn());
                }

                intent.putExtra("data", lists);
                intent.putExtra("hint", gameSelect.getText().toString());
                startActivityForResult(intent, 5050);
            }
        });
        channelSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaDownloadListActivity.this, PaDownloadSelectListActivity.class);
                intent.setType("channel");

                ArrayList<String> lists = new ArrayList<>();
                List<ChannelList.ListBean> games = mChannelData.getList();
                for (ChannelList.ListBean gamesBean : games) {
                    lists.add(gamesBean.getName());
                }

                intent.putExtra("data", lists);
                intent.putExtra("hint", selectChannel);
                startActivityForResult(intent, 5050);
            }
        });
        versionSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaDownloadListActivity.this, PaDownloadSelectListActivity.class);
                intent.setType("version");

                ArrayList<String> lists = new ArrayList<>();
                List<VersionList.VersionsBean> games = mVersionData.getVersions();
                for (VersionList.VersionsBean gamesBean : games) {
                    lists.add(gamesBean.getVersion());
                }

                intent.putExtra("data", lists);
                intent.putExtra("hint", selectVersion);
                startActivityForResult(intent, 5050);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5050 && resultCode == 5050) {
            String type = data.getType();
            if ("game".equals(type)) {
                int position = data.getIntExtra("position", 0);

                GameList.GamesBean gamesBean = mGameData.getGames().get(position);
                selectGame = gamesBean.getAppkey();
                gameSelect.setText(gamesBean.getName().getCh() + "-" + gamesBean.getName().getEn());

                requestVersionList(selectGame);
            } else if ("version".equals(type)) {
                int position = data.getIntExtra("position", 0);

                VersionList.VersionsBean versionsBean = mVersionData.getVersions().get(position);
                selectVersion = versionsBean.getVersion();
                versionSelect.setText(selectVersion);
                requestPackageList(selectGame, selectVersion);
            } else if ("channel".equals(type)) {
                int position = data.getIntExtra("position", 0);

                ChannelList.ListBean listBean = mChannelData.getList().get(position);
                selectChannel = listBean.getName();
                channelSelect.setText(selectChannel);

                filterList(position);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Yodo1SharedPreferences.put(this, "selectGame", selectGame);
        Yodo1SharedPreferences.put(this, "selectChannel", selectChannel);
        Yodo1SharedPreferences.put(this, "selectVersion", selectVersion);
    }

    private void filterList(int position) {
        if (position == 0 || mPackageData == null || mPackageData.getList() == null || mPackageData.getList().isEmpty()) {
            if (mPackageData != null && mPackageData.getList() != null) {
                adapter.setData(mPackageData.getList());
            } else {
                adapter.setData(Collections.EMPTY_LIST);
            }
        } else {
            List<PackageList.ListBean> list = mPackageData.getList();
            List<PackageList.ListBean> ll = new ArrayList<>();
            for (PackageList.ListBean bean : list) {
                if (selectChannel.contains("所有") || TextUtils.equals(bean.getPublish_name(), selectChannel)) {
                    ll.add(bean);
                }
            }
            adapter.setData(ll);
        }
    }

    private void initData() {
        requestGameList();
        requestChannelList();

        selectGame = Yodo1SharedPreferences.getString(this, "selectGame");
        selectChannel = Yodo1SharedPreferences.getString(this, "selectChannel");
        selectVersion = Yodo1SharedPreferences.getString(this, "selectVersion");
    }

    private void requestGameList() {
        final HashMap<String, String> map = new HashMap<>();
        map.putAll(MD5EncodeUtil.paAddSign(""));
        GunqiuApi.getInstance().get(OtaAPi.gamelist, map).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                mGameData = JsonUtils.fromJson(result, GameList.class);

                if (TextUtils.isEmpty(selectGame)) {
                    GameList.GamesBean gamesBean = mGameData.getGames().get(0);
                    selectGame = gamesBean.getAppkey();
                    gameSelect.setText(gamesBean.getName().getCh() + "-" + gamesBean.getName().getEn());
                } else {
                    boolean has = false;
                    for (GameList.GamesBean b : mGameData.getGames()) {
                        if (TextUtils.equals(b.getAppkey(), selectGame)) {
                            has = true;
                            gameSelect.setText(b.getName().getCh() + "-" + b.getName().getEn());
                            break;
                        }
                    }
                    if (!has) {
                        GameList.GamesBean gamesBean = mGameData.getGames().get(0);
                        selectGame = gamesBean.getAppkey();
                        gameSelect.setText(gamesBean.getName().getCh() + "-" + gamesBean.getName().getEn());
                    }
                }
                requestVersionList(selectGame);
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(PaDownloadListActivity.this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestChannelList() {
        HashMap<String, String> map = new HashMap<>();
        map.putAll(MD5EncodeUtil.paAddSign(""));
        GunqiuApi.getInstance().get(OtaAPi.channelList, map).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                ChannelList versionList = JsonUtils.fromJson(result, ChannelList.class);
                ChannelList.ListBean listBean = new ChannelList.ListBean();
                listBean.setName("所有渠道");
                versionList.getList().add(0, listBean);
                mChannelData = versionList;

                if (!TextUtils.isEmpty(selectChannel)) {
                    channelSelect.setText(selectChannel);
                } else {
                    selectChannel = mChannelData.getList().get(0).getName();
                    channelSelect.setText(selectChannel);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(PaDownloadListActivity.this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestPackageList(String gameKey, String gameVersion) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appkey", gameKey);
        map.put("status", "2");
        map.put("type", "test");
        map.put("game_version", gameVersion);
        map.putAll(MD5EncodeUtil.paAddSign(gameKey + gameVersion + "2"));
        GunqiuApi.getInstance().get(OtaAPi.packagelist, map).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                mPackageData = JsonUtils.fromJson(result, PackageList.class);

                filterList(1);
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(PaDownloadListActivity.this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestVersionList(final String appkey) {
        HashMap<String, String> map = new HashMap<>();
        map.put("appkey", appkey);
        map.putAll(MD5EncodeUtil.paAddSign(appkey));
        GunqiuApi.getInstance().get(OtaAPi.versionList, map).compose(RxResultHelper.<String>handleResult()).subscribe(new RxResponse<String>() {
            @Override
            public void onSuccess(String result) {
                mVersionData = JsonUtils.fromJson(result, VersionList.class);

                if (mVersionData.getVersions().isEmpty()) {
                    Toast.makeText(PaDownloadListActivity.this, "没有打包版本。", Toast.LENGTH_SHORT).show();
                    mPackageData = null;
                    versionSelect.setText(null);
                    selectVersion = null;
                    filterList(0);
                } else {
                    String version = mVersionData.getVersions().get(0).getVersion();
                    versionSelect.setText(version);
                    selectVersion = version;
                    requestPackageList(appkey, version);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                Toast.makeText(PaDownloadListActivity.this, "msg:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
