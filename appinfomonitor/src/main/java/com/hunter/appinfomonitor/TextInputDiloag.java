package com.hunter.appinfomonitor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hunter.appinfomonitor.yodo1bean.NfcUtils;
import com.hunter.appinfomonitor.yodo1bean.PhoneUtil;

public class TextInputDiloag extends BottomSheetDialog {

    private NFCActivity activity;
    private EditText et;

    public TextInputDiloag(NFCActivity nfcActivity) {
        super(nfcActivity);
        activity = nfcActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcwrite);

        LinearLayout ll = findViewById(R.id.ndf_writebody);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.width = PhoneUtil.getDisplayWidth(activity);
        ll.setLayoutParams(lp);
        et = findViewById(R.id.content_edittext);
        final Button btn = findViewById(R.id.write_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                NFCActivity.isWriteFlag = true;
                btn.setText("请贴近NFC标签或器具，.... ...");
            }
        });
    }

    public void write(Intent intent) {
        try {
            String data = et.getText().toString();
            NfcUtils.writeNFCToTag(data, intent);
            Toast.makeText(activity, "写入成功", Toast.LENGTH_SHORT).show();
            dismiss();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, "Exception Happened:" + e.getMessage(), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }
}
