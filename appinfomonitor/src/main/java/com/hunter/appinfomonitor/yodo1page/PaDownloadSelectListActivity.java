package com.hunter.appinfomonitor.yodo1page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author yodo1
 */
public class PaDownloadSelectListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText filterEt;
    private ListView lv;
    private MyAdapter adapter;
    private ArrayList<String> d;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_padownloadselect);

        filterEt = findViewById(R.id.padownloadselectet);
        lv = findViewById(R.id.padownloadselectlv);

        Intent intent = getIntent();
        Serializable data = intent.getSerializableExtra("data");

        if (data instanceof ArrayList) {
            d = (ArrayList<String>) data;
            adapter = new MyAdapter(this, d);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(this);
        } else {
            Toast.makeText(this, "data error", Toast.LENGTH_SHORT).show();
            finish();
        }

        filterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getData().clear();
                if (TextUtils.isEmpty(s)) {
                    adapter.setData(d);
                } else {
                    for (String ss : d) {
                        if (ss.contains(s)) {
                            adapter.getData().add(ss);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        findViewById(R.id.clean).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterEt.setText("");
            }
        });
        final String hint = intent.getStringExtra("hint");
        if (!TextUtils.isEmpty(hint)) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    filterEt.setText(hint);
                }
            });
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String type = getIntent().getType();
        Intent intent = new Intent();
        intent.setType(type);
        String item = adapter.getItem(position);
        int posi = 0;
        for (int i = 0; i < d.size(); i++) {
            String s = d.get(i);
            if (TextUtils.equals(s, item)) {
                posi = i;
                break;
            }
        }
        intent.putExtra("position", posi);
        setResult(5050, intent);
        finish();
    }

    private class MyAdapter extends BaseAdapter {

        private ArrayList<String> data;
        private PaDownloadSelectListActivity context;

        public MyAdapter(PaDownloadSelectListActivity paDownloadSelectListActivity, ArrayList<String> d) {
            context = paDownloadSelectListActivity;
            data = new ArrayList<>();
            data.addAll(d);
        }


        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public String getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tv;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_padownload_select, null);
            }
            tv = convertView.findViewById(R.id.item_padownload_tv);
            String item = getItem(position);
            tv.setText(item);
            return convertView;
        }

        public void setData(ArrayList<String> showList) {
            data.clear();
            data.addAll(showList);
            notifyDataSetChanged();
        }

        public ArrayList<String> getData() {
            return data;
        }
    }
}
