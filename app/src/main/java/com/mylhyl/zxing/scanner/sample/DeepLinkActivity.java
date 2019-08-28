package com.mylhyl.zxing.scanner.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DeepLinkActivity extends AppCompatActivity {

    TextView tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deeplink);

        tv = findViewById(R.id.textView);
        Button btn = findViewById(R.id.edit_query);

        tv.setText("https://www.chope.co/singapore-restaurants\n" +
                "https://www.chope.co/singapore-restaurants/list_of_restaurants\n" +
                "https://www.chope.co/bali-restaurants/restaurant/envy\n" +
                "https://www.chope.co/bali-restaurants/pages/chopeexclusives\n" +
                "https://www.chope.co/singapore-restaurants/commerce/product_detail?id=222\n" +
                "https://www.chope.co/singapore-restaurants/commerce/collection?id=111\n" +
                "https://www.chope.co/singapore-restaurants/category/restaurant?t=1&category=1,32,4\n" +
                "https://www.chope.co/[city]/rewards/product_details?id=123423432\n" +
                "https://shop.chope.co/collections/paya-lebar-guide\n" +
                "https://shop.chope.co/collections/paya-lebar-guide/products/pe-taikoo-shing\n" +
                "\n" +
                "promocode:\n" +
                "https://chope.app.link/o7IaRLhhuZ\n" +
                "Voucher:\n" +
                "https://chope.app.link/WzlAfBjhuZ");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeepLinkActivity.this, EditActivity.class);
                intent.putExtra("data", tv.getText().toString());
                startActivityForResult(intent, 1212);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && resultCode == 1212) {
            String stringExtra = data.getStringExtra("data");
            tv.setText(stringExtra);
        }
    }
}
