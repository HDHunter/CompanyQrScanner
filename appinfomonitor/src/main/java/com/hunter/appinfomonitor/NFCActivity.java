package com.hunter.appinfomonitor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hunter.appinfomonitor.floatui.SPHelper;
import com.hunter.appinfomonitor.ui.JsonUtils;
import com.hunter.appinfomonitor.yodo1bean.NFCDB;
import com.hunter.appinfomonitor.yodo1bean.NfcUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class NFCActivity extends BaseActvity {

    /**
     * 非写即读
     */
    public static boolean isWriteFlag = false;
    private ListView listView;
    private NFCListAdapter listAdapter;
    private TextView nfcWrite;
    private TextInputDiloag dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        NfcUtils nfc = new NfcUtils(this);
        String nfcList = SPHelper.getNFCList(this);
        NFCDB nfcdb = JsonUtils.fromJson(nfcList, NFCDB.class);
        ListView listView = findViewById(R.id.nfc_listview);
        listAdapter = new NFCListAdapter(this);
        listView.setAdapter(listAdapter);
        if (nfcdb != null) {
            listAdapter.setData(nfcdb.getNfcList());
        }

        nfcWrite = findViewById(R.id.nfc_write);
        nfcWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new TextInputDiloag(NFCActivity.this);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        isWriteFlag = false;
                    }
                });
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //开启前台调度系统

        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.enableForegroundDispatch(this, NfcUtils.mPendingIntent, NfcUtils.mIntentFilter, NfcUtils.mTechList);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //关闭前台调度系统
        if (NfcUtils.mNfcAdapter != null) {
            NfcUtils.mNfcAdapter.disableForegroundDispatch(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        List<NfcUtils.NFC> nfcs = listAdapter.getmData();
        NFCDB db = new NFCDB();
        db.setNfcList(nfcs);
        JsonUtils.saveJson("nfc", db, this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isWriteFlag) {
            dialog.write(intent);
        } else {
            //当该Activity接收到NFC标签时，运行该方法
            //调用工具方法，读取NFC数据
            try {
                NfcUtils.NFC nfc = NfcUtils.getNFC(intent);
                listAdapter.addNfc(nfc);
                listAdapter.notifyDataSetChanged();
                String s = nfc.toString();
                LogUtils.e("NFC:", s);
//                Toast.makeText(this, "读取到NFC:" + s, Toast.LENGTH_SHORT).show();
            } catch (UnsupportedEncodingException e) {
                Toast.makeText(this, "read error.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class NFCListAdapter extends BaseAdapter {
        List<NfcUtils.NFC> mData;
        NFCActivity activity;

        public NFCListAdapter(NFCActivity nfcActivity) {
            activity = nfcActivity;
            mData = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public NfcUtils.NFC getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            VH vh;
            if (convertView == null) {
                convertView = View.inflate(activity, R.layout.item_nfcitem, null);
                vh = new VH();
                vh.name = convertView.findViewById(R.id.item_name);
                vh.tagid = convertView.findViewById(R.id.item_tag);
                vh.dataid = convertView.findViewById(R.id.item_data_id);
                vh.msg = convertView.findViewById(R.id.item_message);
                vh.idid = convertView.findViewById(R.id.item_idid);
                convertView.setTag(vh);
            } else {
                vh = (VH) convertView.getTag();
            }
            NfcUtils.NFC item = getItem(position);
            vh.name.setText("名称:" + item.getName());
            vh.tagid.setText("TAG:" + item.getTag_id());
            vh.dataid.setText("DATA_ID:" + item.getDATA_id());
            vh.idid.setText("ID:" + item.getID_id());
            vh.msg.setText("Msg:" + item.getMsg());
            return convertView;
        }

        public void setData(List<NfcUtils.NFC> nfcList) {
            mData.clear();
            if (nfcList != null) {
                mData.addAll(nfcList);
            }
            notifyDataSetChanged();
        }

        public List<NfcUtils.NFC> getmData() {
            return mData;
        }

        public void addNfc(NfcUtils.NFC nfc) {
            boolean isContain = false;
            for (NfcUtils.NFC nn : getmData()) {
                if (TextUtils.equals(nn.getDATA_id(), nfc.getDATA_id()) &&
                        TextUtils.equals(nn.getID_id(), nfc.getID_id()) &&
                        TextUtils.equals(nn.getMsg(), nfc.getMsg()) &&
                        TextUtils.equals(nn.getTag_id(), nfc.getTag_id())
                ) {
                    isContain = true;
                }
            }
            if (!isContain) {
                getmData().add(nfc);
                LogUtils.e("print", "添加到列表中");
                notifyDataSetChanged();
            } else {
                Toast.makeText(activity, "列表中已经包含", Toast.LENGTH_SHORT).show();
            }
        }

        class VH {
            TextView name;
            TextView tagid;
            TextView idid;
            TextView dataid;
            TextView msg;
        }
    }
}
