package com.hunter.bjhealthcode;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EditorBJCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorbj);

        EditText etname = findViewById(R.id.input_name);
        EditText etid = findViewById(R.id.input_id);
        Button save = findViewById(R.id.input_save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etname.getText().toString();
                YSharedPreferences.put(EditorBJCodeActivity.this, "name", name);

                String id = etid.getText().toString();
                YSharedPreferences.put(EditorBJCodeActivity.this, "id", id);

                finish();
            }
        });

        String name = YSharedPreferences.getString(this, "name");
        String id = YSharedPreferences.getString(this, "id");
        etname.setText(name);
        etid.setText(id);
    }

}
