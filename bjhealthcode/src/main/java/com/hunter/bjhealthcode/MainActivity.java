package com.hunter.bjhealthcode;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = findViewById(R.id.enterButton);
        Button btn2 = findViewById(R.id.editorButton);

        YSharedPreferences.init(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "注意安全，准备跑。", Toast.LENGTH_SHORT).show();
                long l = System.currentTimeMillis();
                Log.e("***", "time:" + l);
                if (l < 1665234000000l) {
                    startActivity(new Intent(MainActivity.this, BJCodeActivity.class));
                }
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "注意安全，准备跑。", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, EditorBJCodeActivity.class));
            }
        });
    }

}