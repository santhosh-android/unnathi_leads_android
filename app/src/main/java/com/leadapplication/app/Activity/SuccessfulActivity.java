package com.leadapplication.app.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SuccessfulActivity extends AppCompatActivity  {
    private TextView tv_home_btn, home_content;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);

        tv_home_btn = findViewById(R.id.tv_home_btn);
        home_content = findViewById(R.id.home_content);

        tv_home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });
    }
}