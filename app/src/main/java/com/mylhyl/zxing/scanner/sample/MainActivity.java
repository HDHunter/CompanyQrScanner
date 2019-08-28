package com.mylhyl.zxing.scanner.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.encode.QREncode;

import java.io.ByteArrayOutputStream;

public class MainActivity extends BasicActivity {
    private static final int PICK_CONTACT = 1;
    private TextView tvResult;
    private ImageView imageView;
    private int laserMode, scanMode;
    private EditText eitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvResult = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);

        final ToggleButton toggleButton = findViewById(R.id.toggleButton);
        final ToggleButton toggleButton1 = findViewById(R.id.toggleButton1);
        final ToggleButton toggleButton2 = findViewById(R.id.toggleButton2);

        final CheckBox checkBox = findViewById(R.id.checkBox);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_0;
                        break;
                    case R.id.radioButton2:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_1;
                        break;
                    case R.id.radioButton3:
                        laserMode = ScannerActivity.EXTRA_LASER_LINE_MODE_2;
                        break;
                    default:
                }
            }
        });

        RadioGroup radioGroup1 = findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton4:
                        scanMode = ScannerActivity.EXTRA_SCAN_MODE_0;
                        break;
                    case R.id.radioButton5:
                        scanMode = ScannerActivity.EXTRA_SCAN_MODE_1;
                        break;
                    case R.id.radioButton6:
                        scanMode = ScannerActivity.EXTRA_SCAN_MODE_2;
                        break;
                    default:
                }
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予，需要在这里写申请权限的代码
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, 60);
                } else {
                    //权限已经被授予，在这里直接写要执行的相应方法即可
                    ScannerActivity.gotoActivity(MainActivity.this,
                            checkBox.isChecked(), laserMode, scanMode, !toggleButton.isChecked()
                            , toggleButton1.isChecked(), toggleButton2.isChecked());
//                    OptionsScannerActivity.gotoActivity(MainActivity.this);
                }
            }
        });

        eitText = findViewById(R.id.editText);

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                Bitmap logoBitmap = BitmapFactory.decodeResource(res, R.mipmap.connect_logo);
                String qrContent = eitText.getText().toString();
                Bitmap bitmap = new QREncode.Builder(MainActivity.this)
                        .setColors(Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK)//二维码彩色
                        .setMargin(0)//二维码边框
                        .setParsedResultType(TextUtils.isEmpty(qrContent) ? ParsedResultType.URI : ParsedResultType.TEXT)//二维码类型
                        //二维码内容
                        .setContents(TextUtils.isEmpty(qrContent) ? "https://github.com/HDHunter" : qrContent)
                        .setSize(500)//二维码等比大小
                        .setLogoBitmap(logoBitmap, 90)
                        .build().encodeAsBitmap();
                imageView.setImageBitmap(bitmap);
                tvResult.setText("单击识别图中二维码");
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts
                        .CONTENT_URI);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setDrawingCacheEnabled(true);//step 1
                Bitmap bitmap = imageView.getDrawingCache();//step 2
                //step 3 转bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                //step 4
                startActivity(new Intent(MainActivity.this, DeCodeActivity.class).putExtra("bytes", baos.toByteArray()));
                imageView.setDrawingCacheEnabled(false);//step 5
            }
        });

        findViewById(R.id.button_deeplink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeepLinkActivity.class);
                startActivityForResult(intent, 1212);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_CANCELED && resultCode == Activity.RESULT_OK) {
            if (requestCode == ScannerActivity.REQUEST_CODE_SCANNER) {
                if (data != null) {
                    String stringExtra = data.getStringExtra(Scanner.Scan.RESULT);
                    eitText.setText(stringExtra);
                }
            } else if (requestCode == PICK_CONTACT) {
                // Data field is content://contacts/people/984
                showContactAsBarcode(data.getData());
            }
        }
    }

    /**
     * @param contactUri content://contacts/people/17
     */
    private void showContactAsBarcode(Uri contactUri) {
        //可以自己组装bundle;
//        ParserUriToVCard parserUriToVCard = new ParserUriToVCard();
//        Bundle bundle = parserUriToVCard.parserUri(this, contactUri);
//        if (bundle != null) {
//            Bitmap bitmap = QREncode.encodeQR(new QREncode.Builder(this)
//                    .setParsedResultType(ParsedResultType.ADDRESSBOOK)
//                    .setBundle(bundle).build());
//            imageView.setImageBitmap(bitmap);
//            tvResult.setText("单击二维码图片识别");
//        } else tvResult.setText("联系人Uri错误");

        //只传Uri
        Bitmap bitmap = new QREncode.Builder(this)
                .setParsedResultType(ParsedResultType.ADDRESSBOOK)
                .setAddressBookUri(contactUri).build().encodeAsBitmap();
        imageView.setImageBitmap(bitmap);
        tvResult.setText("单击二维码图片识别");
    }
}
