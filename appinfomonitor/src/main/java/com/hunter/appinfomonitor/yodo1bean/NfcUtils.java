package com.hunter.appinfomonitor.yodo1bean;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Parcelable;
import android.provider.Settings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class NfcUtils {

    //nfc  
    public static NfcAdapter mNfcAdapter;
    public static IntentFilter[] mIntentFilter = null;
    public static PendingIntent mPendingIntent = null;
    public static String[][] mTechList = null;

    /**
     * 构造函数，用于初始化nfc
     */
    public NfcUtils(Activity activity) {
        mNfcAdapter = NfcCheck(activity);
        NfcInit(activity);
    }

    /**
     * 检查NFC是否打开
     */
    public static NfcAdapter NfcCheck(Activity activity) {
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if (mNfcAdapter == null) {
            return null;
        } else {
            if (!mNfcAdapter.isEnabled()) {
                Intent setNfc = new Intent(Settings.ACTION_NFC_SETTINGS);
                activity.startActivity(setNfc);
            }
        }
        return mNfcAdapter;
    }

    /**
     * 初始化nfc设置
     */
    public static void NfcInit(Activity activity) {
        mPendingIntent = PendingIntent.getActivity(activity, 0, new Intent(activity, activity.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter filter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter filter2 = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        try {
            filter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        mIntentFilter = new IntentFilter[]{filter, filter2};
        mTechList = null;
    }

    /**
     * 读取NFC的数据
     */
    public static String readNFCFromTag(Intent intent) throws UnsupportedEncodingException {
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        byte[] nfc_id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        byte[] nfc_data = intent.getByteArrayExtra(NfcAdapter.EXTRA_DATA);
        Tag nfc_tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                String readResult = new String(mNdefRecord.getPayload(), "UTF-8");
                return readResult;
            }
        }
        return "";
    }


    public static NFC getNFC(Intent intent) throws UnsupportedEncodingException {
        NFC nfc = new NFC();
        Parcelable[] rawArray = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        byte[] nfc_id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            byte[] nfc_data = intent.getByteArrayExtra(NfcAdapter.EXTRA_DATA);
            nfc.DATA_id = ByteArrayToHexString(nfc_data);
        }
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        nfc.tag_id = ByteArrayToHexString(tag.getId());
        nfc.ID_id = ByteArrayToHexString(nfc_id);
        if (rawArray != null) {
            NdefMessage mNdefMsg = (NdefMessage) rawArray[0];
            NdefRecord mNdefRecord = mNdefMsg.getRecords()[0];
            if (mNdefRecord != null) {
                nfc.msg = new String(mNdefRecord.getPayload(), "UTF-8");
            }
        }
        return nfc;
    }

    /**
     * 往nfc写入数据
     */
    public static void writeNFCToTag(String data, Intent intent) throws IOException, FormatException {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            NdefRecord ndefRecord = NdefRecord.createTextRecord(null, data);
//        }
        NdefRecord ndefRecord0 = NdefRecord.createApplicationRecord(data);
        NdefRecord[] records0 = {ndefRecord0};
        NdefMessage ndefMessage = new NdefMessage(records0);
        ndef.writeNdefMessage(ndefMessage);
    }


    /**
     * 将字节数组转换为字符串
     */
    private static String ByteArrayToHexString(byte[] inarray) {
        if (inarray == null) {
            return null;
        }
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public static class NFC {
        String tag_id;
        String ID_id;
        String DATA_id;
        String msg;
        //define
        String name;

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getID_id() {
            return ID_id;
        }

        public void setID_id(String ID_id) {
            this.ID_id = ID_id;
        }

        public String getDATA_id() {
            return DATA_id;
        }

        public void setDATA_id(String DATA_id) {
            this.DATA_id = DATA_id;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "NFC{" +
                    "tag_id='" + tag_id + '\'' +
                    ", ID_id='" + ID_id + '\'' +
                    ", DATA_id='" + DATA_id + '\'' +
                    ", msg='" + msg + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
} 