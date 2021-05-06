package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.leadapplication.app.Controller.CheckAgentStatusController;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.R;
import com.leadapplication.app.UserSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity implements CheckAgentStatusController.CheckAgentListener {
    private SharedPreferences sharedPreferences;
    private String otp_verify, user_id, status = "";
    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        user_id = sharedPreferences.getString("agent_id", "");
        otp_verify = sharedPreferences.getString("otp_verified", "");
        isLogin = sharedPreferences.getBoolean("Login_check", false);

        agentStatusApiCall();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                if (status.equalsIgnoreCase("valid")) {
                    boolean isLoggedIn = UserSessionManagement.getUserSessionManagement(SplashActivity.this).isUserLoggedIn();
                    if (isLoggedIn) {
                        isLogin = false;
                   /* if (otp_verify.equals("yes")) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    }*/ /*else {
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }*/
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        isLogin = true;
                        intent = new Intent(SplashActivity.this, LoginActivity.class);
                    }
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void agentStatusApiCall() {
        CheckAgentStatusController.getCheckAgentStatusController().CheckAgentStatus(user_id);
        CheckAgentStatusController.getCheckAgentStatusController().setCheckAgentListener(this);
    }

    @Override
    public void onAgentSucces(JSONObject jsonObject) {
        try {
            status = jsonObject.getString("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAgentFailure(String failureString) {
        //Toast.makeText(this, failureString, Toast.LENGTH_SHORT).show();
    }
}