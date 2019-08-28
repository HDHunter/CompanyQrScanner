package com.mylhyl.zxing.scanner.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResultType;
import com.mylhyl.zxing.scanner.common.Scanner;
import com.mylhyl.zxing.scanner.decode.QRDecode;
import com.mylhyl.zxing.scanner.result.URIResult;
import com.mylhyl.zxing.scanner.sample.result.AddressBookActivity;
import com.mylhyl.zxing.scanner.sample.result.BarcodeActivity;
import com.mylhyl.zxing.scanner.sample.result.TextActivity;

/**
 * 单击解析图片
 */
public class DeCodeActivity extends BasicScannerActivity {
    @Override
    void onResultActivity(Result result, ParsedResultType type, Bundle bundle) {
        switch (type) {
            case ADDRESSBOOK:
                AddressBookActivity.gotoActivity(DeCodeActivity.this, bundle);
                break;
            case PRODUCT:
                BarcodeActivity.gotoActivity(DeCodeActivity.this, bundle);
                break;
            case ISBN:
                BarcodeActivity.gotoActivity(DeCodeActivity.this, bundle);
                break;
            case URI:
                String uri = ((URIResult) bundle.getSerializable(Scanner.Scan.RESULT)).getUri();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(uri));
                startActivityForResult(intent, 5252);
                break;
            case TEXT:
                TextActivity.gotoActivity(DeCodeActivity.this, bundle);
                break;
            case GEO:
                break;
            case TEL:
                break;
            case SMS:
                break;
            default:
        }
        dismissProgressDialog();
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            byte[] bytes = extras.getByteArray("bytes");
            if (bytes != null && bytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    showProgressDialog();
                    QRDecode.decodeQR(bitmap, this);
                }
            }
        }
    }

}
