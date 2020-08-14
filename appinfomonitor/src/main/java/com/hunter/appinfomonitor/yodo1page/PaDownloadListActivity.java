package com.hunter.appinfomonitor.yodo1page;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.hunter.appinfomonitor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yodo1
 */
public class PaDownloadListActivity extends AppCompatActivity {

    private Spinner gameSelect, channelSelect, versionSelect;
    private PaSelectAdapter gameSelectAdapter, channelSelectAdapter, versionSelectAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padownloadlist);

        gameSelect = findViewById(R.id.gameselect);
        channelSelect = findViewById(R.id.channelselect);
        versionSelect = findViewById(R.id.versionselect);

        gameSelect.setPrompt("选择游戏");
        channelSelect.setPrompt("选择渠道");
        versionSelect.setPrompt("选择版本");

        gameSelectAdapter = new PaSelectAdapter(this, 0);
        channelSelectAdapter = new PaSelectAdapter(this, 0);
        versionSelectAdapter = new PaSelectAdapter(this, 0);
        gameSelect.setAdapter(gameSelectAdapter);
        channelSelect.setAdapter(channelSelectAdapter);
        versionSelect.setAdapter(versionSelectAdapter);

        initData();
        gameSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        channelSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        versionSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        ArrayList<String> objects = new ArrayList<>();
        objects.add("sdf");
        objects.add("我们");
        objects.add("你们");
        gameSelectAdapter.setData(objects);
        channelSelectAdapter.setData(objects);
        versionSelectAdapter.setData(objects);
    }


    /**
     * @author yodo1
     */
    private class PaSelectAdapter extends BaseAdapter {

        private List<String> mlist;
        private Activity context;
        private int type;

        public PaSelectAdapter(PaDownloadListActivity paDownloadListActivity, int i) {
            context = paDownloadListActivity;
            mlist = new ArrayList<>();
            type = i;
        }

        @Override
        public int getCount() {
            return mlist.size();
        }

        @Override
        public String getItem(int position) {
            return mlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv = null;
            if (convertView == null) {
                convertView = new TextView(context);
                tv = (TextView) convertView;
                ListView.LayoutParams lp = new ListView.LayoutParams(ListView.LayoutParams.WRAP_CONTENT, ListView.LayoutParams.MATCH_PARENT);
                tv.setLayoutParams(lp);
                tv.setTextSize(dip2px(context, 6));
                tv.setTextColor(Color.BLACK);
                tv.setGravity(Gravity.CENTER);
                tv.setMinEms(4);
                int i = dip2px(context, 8);
                tv.setPadding(i, i, i, i);
            } else {
                tv = (TextView) convertView;
            }
            String item = getItem(position);
            tv.setText(item);
            return tv;
        }

        public void setData(ArrayList<String> objects) {
            mlist.clear();
            mlist.addAll(objects);
            notifyDataSetChanged();
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
