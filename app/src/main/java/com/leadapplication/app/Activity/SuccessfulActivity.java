package com.leadapplication.app.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SuccessfulActivity extends AppCompatActivity {
    private TextView tv_home_btn, home_content, idAgent, tv_success;
    private String content, agentId = "", from = "";
    private Button btnYes, btnNo;
    private LinearLayout layBtns;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful);

        castingViews();


        if (getIntent().hasExtra("id")) {
            agentId = getIntent().getStringExtra("id");
            from = getIntent().getStringExtra("from");
            //idAgent.setText(agentId);
        }
        assert from != null;
        if (from.equals("agent")) {
            home_content.setText("Your Account Activation is Pending...\nContact Unnathi Team");
            tv_success.setText("New Agent Registration Successful");
            tv_home_btn.setVisibility(View.VISIBLE);
            layBtns.setVisibility(View.GONE);
        } else {
            tv_success.setText("User Registration Successful");
            home_content.setText("Do you want to register another user");
            tv_home_btn.setVisibility(View.GONE);
            layBtns.setVisibility(View.VISIBLE);
        }

        btnYes.setOnClickListener(v -> {
            startActivity(new Intent(SuccessfulActivity.this, UserDetailsActivity.class));
            finish();
        });
        btnNo.setOnClickListener(v -> {
            startActivity(new Intent(SuccessfulActivity.this, MainActivity.class));
            finish();
        });


        tv_home_btn.setOnClickListener(v -> {
            startActivity(new Intent(SuccessfulActivity.this, LoginActivity.class));
           /* Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);*/
            finish();
        });
    }

    private void castingViews() {
        tv_home_btn = findViewById(R.id.tv_home_btn);
        home_content = findViewById(R.id.home_content);
        idAgent = findViewById(R.id.idAgent);
        tv_success = findViewById(R.id.tv_success);
        btnYes = findViewById(R.id.btnYes);
        btnNo = findViewById(R.id.btnNo);
        layBtns = findViewById(R.id.layBtns);
    }
}