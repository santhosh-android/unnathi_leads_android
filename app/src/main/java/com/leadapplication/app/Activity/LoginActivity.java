package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.leadapplication.app.Controller.CheckAgentStatusController;
import com.leadapplication.app.Controller.LoginController;
import com.leadapplication.app.R;
import com.leadapplication.app.UserSessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements LoginController.LoginControlListner, CheckAgentStatusController.CheckAgentListener {
    private Button btn_login;
    private TextInputEditText edit_email_value, edit_password_value;
    private String emailString, passwordString, agent_id, otp_verified, status;
    private TextView tv_register_btn;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;
    private boolean check_login;
    private String country_id, country, state_id, state, district_id, district, profile_image, name;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Login Please Wait...");
        castingViews();

        tv_register_btn = (TextView) findViewById(R.id.tv_register_btn);
        Spannable wordtoSpan = new SpannableString("Don't have an account? Register");
        wordtoSpan.setSpan(new ForegroundColorSpan(R.color.colorPrimary), 23, 31, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_register_btn.setText(wordtoSpan);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateEmail() | !validatePassword()) {
                    return;
                } else {
                    loginApiCall();
                }
            }
        });
        tv_register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void loginApiCall() {
        progressDialog.show();
        Map<String, String> loginMap = new HashMap<>();
        loginMap.put("username", emailString);
        loginMap.put("password", passwordString);
        LoginController.getLoginController().LoginApiCall(loginMap);
        LoginController.getLoginController().setLoginControlListner(this);
    }

    private void gettingVariables() {
        emailString = edit_email_value.getText().toString().trim();
        passwordString = edit_password_value.getText().toString().trim();
    }

    private boolean validateEmail() {
        if (emailString.isEmpty()) {
            edit_email_value.setError("Enter Email or Mobile Number");
            return false;
        } else {
            edit_email_value.setError(null);
        }
        return true;
    }

    private boolean validatePassword() {
        if (passwordString.isEmpty()) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            edit_password_value.setError(null);
        }
        return true;
    }

    private void castingViews() {
        btn_login = findViewById(R.id.btn_login);
        edit_email_value = findViewById(R.id.edit_email_value);
        edit_password_value = findViewById(R.id.edit_password_value);
        tv_register_btn = findViewById(R.id.tv_register_btn);
    }

    @Override
    public void onLoginSuccess(JSONObject jsonObject) {
        progressDialog.cancel();
        try {
            String message = jsonObject.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            JSONObject userListObject = jsonObject.getJSONObject("details");
            agent_id = userListObject.getString("id");
            country_id = userListObject.getString("country");
            state_id = userListObject.getString("state");
            district_id = userListObject.getString("district");
            name = userListObject.getString("name");
            profile_image = userListObject.getString("profile_image");
            country = userListObject.getString("country_name");
            state = userListObject.getString("state_name");
            district = userListObject.getString("district_name");
            otp_verified = userListObject.getString("otp_verified");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        check_login = sharedPreferences.getBoolean("Login_check", false);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("agent_id", agent_id);
        editor.putString("otp_verified", otp_verified);

        editor.putString("country_id", country_id);
        editor.putString("state_id", state_id);
        editor.putString("district_id", district_id);
        editor.putString("name", name);
        editor.putString("image", profile_image);

        editor.putString("country", country);
        editor.putString("state", state);
        editor.putString("district", district);
        editor.putBoolean("login_check", true);
        editor.apply();
        editor.commit();

        UserSessionManagement.getUserSessionManagement(LoginActivity.this).saveUser(agent_id);
        checkAgentStatus();
    }

    private void checkAgentStatus() {
        CheckAgentStatusController.getCheckAgentStatusController().CheckAgentStatus(agent_id);
        CheckAgentStatusController.getCheckAgentStatusController().setCheckAgentListener(this);
    }

    @Override
    public void onLoginFailure(String failureString) {
        progressDialog.cancel();
        progressDialog.hide();
        Toast.makeText(this, failureString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAgentSucces(JSONObject jsonObject) {
        try {
            status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("invalid")) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            } else {
                Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                loginIntent.putExtra("country", country);
                loginIntent.putExtra("state", state);
                loginIntent.putExtra("district", district);
                loginIntent.putExtra("profile_image", profile_image);
                loginIntent.putExtra("name", name);

                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginIntent);
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAgentFailure(String failureString) {
        Toast.makeText(this, failureString, Toast.LENGTH_SHORT).show();
    }
}