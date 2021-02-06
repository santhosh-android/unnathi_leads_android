package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private ImageView img_back_change;
    private Button btn_update;
    private TextInputLayout edit_old, et_new, et_reenter;
    private String oldString, newString, reEnterString;
    private SharedPreferences sharedPreferences;
    private String agent_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        castingViews();
        img_back_change = findViewById(R.id.img_back_change);
        img_back_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateOld() || !validateNew() || !validateRe()) {
                    return;
                } else {
                    passwordChangeApiCall();
                }
            }
        });
    }

    private void passwordChangeApiCall() {
        gettingVariables();
        Map<String, String> passwordMap = new HashMap<>();
        passwordMap.put("agent_id", agent_id);
        passwordMap.put("old_password", oldString);
        passwordMap.put("new_password", newString);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.passwordChange(passwordMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            String message = responseObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                            Intent profileIntent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                            startActivity(profileIntent);
                        } else {
                            String failureMsg = responseObject.getString("message");
                            Toast.makeText(ChangePasswordActivity.this, failureMsg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void castingViews() {
        btn_update = findViewById(R.id.btn_update);
        edit_old = findViewById(R.id.edit_old);
        et_new = findViewById(R.id.et_new);
        et_reenter = findViewById(R.id.et_reenter);

    }

    private void gettingVariables() {
        oldString = edit_old.getEditText().getText().toString();
        newString = et_new.getEditText().getText().toString();
        reEnterString = et_reenter.getEditText().getText().toString();
    }

    public boolean validateOld() {
        if (oldString.isEmpty()) {
            edit_old.setError("Enter Old Password");
            return false;
        } else {
            edit_old.setError(null);
        }
        return true;
    }

    public boolean validateNew() {
        if (newString.isEmpty()) {
            et_new.setError("Enter New Password");
            return false;
        } else {
            et_new.setError(null);
        }
        return true;
    }

    public boolean validateRe() {
        if (reEnterString.isEmpty()) {
            et_reenter.setError("Re Enter Password");
            return false;
        } else if (!reEnterString.equals(newString)) {
            et_reenter.setError("Password Not Matched");
            return false;
        } else {
            et_reenter.setError(null);
        }
        return true;
    }
}