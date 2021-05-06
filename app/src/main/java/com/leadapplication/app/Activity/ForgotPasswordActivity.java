package com.leadapplication.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText et_mobile_number;
    private String userId, message;
    private ProgressBar pbr;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sharedPreferences = getSharedPreferences("unnathi", MODE_PRIVATE);
       /* getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        TextView tv_send = findViewById(R.id.tv_send);
        pbr = findViewById(R.id.pbr);
        et_mobile_number = findViewById(R.id.et_mobile_number);
        tv_send.setOnClickListener(v -> {
            if (!validateNumber()) {
            } else {
                pbr.setVisibility(View.VISIBLE);
                sendOtpApiCall();
            }

        });

    }

    private boolean validateNumber() {
        if (et_mobile_number.getText().toString().equals("0")) {
            et_mobile_number.setError("Enter Registred Mobile Number");
            return false;
        } else if (et_mobile_number.getText().toString().length() < 10) {
            et_mobile_number.setError("Enter Valid Mobile Number");
            return false;
        } else {
            et_mobile_number.setError(null);
            return true;
        }
    }

    /*
     * Purpose : Implement the send OTP service call.
     *
     * @success : getting OTP and redirect to verfyOtp screen of the application.
     * @failure : Showing failure toast message.
     *
     */
    private void sendOtpApiCall() {
        UnnathiLeadJsonPlaceHolder serviceInterface = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = serviceInterface.forgotPassword(et_mobile_number.getText().toString());
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    pbr.setVisibility(View.GONE);
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject resposeObj = new JSONObject(responseStr);
                        if (resposeObj.getString("status").equalsIgnoreCase("valid")) {
                            message = resposeObj.getString("message");
                            userId = resposeObj.getString("user_id");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("user_id", userId);
                            editor.apply();
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent otpIntent = new Intent(ForgotPasswordActivity.this, OtpVerificationActivity.class);
                            otpIntent.putExtra("from", "forgot");
                            otpIntent.putExtra("userId", userId);
                            startActivity(otpIntent);
                            finish();
                        } else {
                            message = resposeObj.getString("message");
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        pbr.setVisibility(View.GONE);
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                pbr.setVisibility(View.GONE);
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            ForgotPasswordActivity.this.overridePendingTransition(R.anim.pop_enter, R.anim.pop_exit);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}