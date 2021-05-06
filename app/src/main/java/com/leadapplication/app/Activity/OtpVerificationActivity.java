package com.leadapplication.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.leadapplication.app.Controller.OtpController;
import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OtpVerificationActivity extends AppCompatActivity implements OtpController.OtpVerifyListener {
    private EditText otp_first_digit, otp_second_digit, otp_thired_digit, otp_four_digit;
    private LinearLayout otp_digits_layout;
    private String firstDigit, secondDigit, thirdDigit, fourthDigit, from, register, otp, userId;
    private TextView tv_verify, txt_resend;
    private String createString, confirmString;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);

        getSupportActionBar().setTitle("OTP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait Untill Verify...");
        progressDialog.setCancelable(false);

        sharedPreferences = getSharedPreferences("leadUnnathi", MODE_PRIVATE);
        userId = sharedPreferences.getString("agent_id", "");

        countdown();

       /* if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
        }
        if (getIntent().hasExtra("from")) {
            register = getIntent().getStringExtra("from");
        }*/
        castingViews();
        onClick();
        otpBack();
        firstDigitListener();
        secondDigitListener();
        thirdDigitListener();
    }

    private void otpBack() {
        otp_four_digit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (otp_four_digit.getText().length() == 0) {
                    otp_thired_digit.requestFocus();
                }
                return false;
            }
        });

        otp_thired_digit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (otp_thired_digit.getText().length() == 0) {
                    otp_second_digit.requestFocus();
                }
                return false;
            }
        });

        otp_second_digit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (otp_second_digit.getText().length() == 0) {
                    otp_first_digit.requestFocus();
                }
                return false;
            }
        });
    }

    private void thirdDigitListener() {
        otp_thired_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_thired_digit.length() == 1) {
                    otp_four_digit.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void secondDigitListener() {
        otp_second_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_second_digit.length() == 1) {
                    otp_thired_digit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void firstDigitListener() {
        otp_first_digit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (otp_first_digit.length() == 1) {
                    otp_second_digit.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void gettingVariables() {
        firstDigit = otp_first_digit.getText().toString().trim();
        secondDigit = otp_second_digit.getText().toString().trim();
        thirdDigit = otp_thired_digit.getText().toString().trim();
        fourthDigit = otp_four_digit.getText().toString().trim();
        otp = firstDigit + secondDigit + thirdDigit + fourthDigit;
    }

    private boolean validateFirstDigit() {
        if (otp_first_digit.getText().toString().isEmpty()) {
            otp_first_digit.setError("");
            return false;
        } else {
            otp_first_digit.setError(null);
            return true;
        }
    }

    private boolean validateSecondDigit() {
        if (otp_second_digit.getText().toString().isEmpty()) {
            otp_second_digit.setError("");
            return false;
        } else {
            otp_second_digit.setError(null);
            return true;
        }
    }

    private boolean validateThirdDigit() {
        if (otp_thired_digit.getText().toString().isEmpty()) {
            otp_thired_digit.setError("");
            return false;
        } else {
            otp_thired_digit.setError(null);
            return true;
        }
    }

    private boolean validateFourthDigit() {
        if (otp_four_digit.getText().toString().isEmpty()) {
            otp_four_digit.setError("");
            return false;
        } else {
            otp_four_digit.setError(null);
            return true;
        }
    }

    private void onClick() {
        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateFirstDigit() | !validateSecondDigit() | !validateThirdDigit() | !validateFourthDigit()) {
                    return;
                } else {
                    gettingVariables();
                    otpVerifyApiCall();
                }
                 /*else if (from.equals("forgot")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OtpVerificationActivity.this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.custom_alert_password, null);
                    builder.setView(customLayout);
                    EditText et_create_password, et_confirm_password;
                    TextView tv_reset;

                    et_create_password = customLayout.findViewById(R.id.et_create_password);
                    et_confirm_password = customLayout.findViewById(R.id.et_create_password);
                    tv_reset = customLayout.findViewById(R.id.tv_reset);

                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.show();

                    tv_reset.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createString = et_create_password.getText().toString();
                            confirmString = et_confirm_password.getText().toString();
                            if (!confirmString.isEmpty() && !confirmString.equals(null)) {
                                Toast.makeText(com.unnathi.app.Activity.OtpVerificationActivity.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                startActivity(new Intent(com.unnathi.app.Activity.OtpVerificationActivity.this, MainActivity.class));
                            } else {
                                Toast.makeText(com.unnathi.app.Activity.OtpVerificationActivity.this, "Please Enter Required Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }*/
            }
        });
        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OtpVerificationActivity.this, "Password Sended Successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void otpVerifyApiCall() {
        progressDialog.show();
        Map<String, String> otpMap = new HashMap<>();
        otpMap.put("agent_id", userId);
        otpMap.put("otp", otp);
        OtpController.getOtpController().OtpApiCall(otpMap);
        OtpController.getOtpController().setOtpVerifyListener(this);
    }

    private void castingViews() {
        otp_first_digit = findViewById(R.id.otp_first_digit);
        otp_second_digit = findViewById(R.id.otp_second_digit);
        otp_thired_digit = findViewById(R.id.otp_thired_digit);
        otp_four_digit = findViewById(R.id.otp_four_digit);
        otp_digits_layout = findViewById(R.id.otp_digits_layout);
        tv_verify = findViewById(R.id.tv_verify);
        txt_resend = findViewById(R.id.txt_resend);
    }

    private void countdown() {
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 != 0) {
                    txt_resend.setText(String.format("Resend Code in %s sec", millisUntilFinished / 1000));
                }
            }
            @Override
            public void onFinish() {
                txt_resend.setText("Resend OTP");
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onOtpSuccess(JSONObject jsonObject) {
        progressDialog.cancel();
        try {
            String message = jsonObject.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(OtpVerificationActivity.this, MainActivity.class));

    }

    @Override
    public void onOtpFailure(String failureMsg) {
        progressDialog.cancel();
        Toast.makeText(this, failureMsg, Toast.LENGTH_SHORT).show();
    }
}