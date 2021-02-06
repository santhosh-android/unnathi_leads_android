package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class UserDetailsActivity extends AppCompatActivity {
    private EditText user_number_edit;
    private Button btn_details, btn_add_lead, btn_cancel;
    private TextView txt_name, txt_lastname, txt_mobile, txt_email, txt_agent_assigned, txt_agent_name, txt_assiging, txt_register;
    private String userMobile, agentId;
    private String customerId, name, lastname, mobile, email, agent_assigned, agent_name;
    private SharedPreferences sharedPreferences;
    private ImageView img_back_user;
    private CardView card_details_user;
    private AlertDialog.Builder builder;
    private String country, state, district;
    private RelativeLayout layout_pbr;
    private ProgressBar pbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agentId = sharedPreferences.getString("agent_id", "");
        if (getIntent().hasExtra("country")) {
            country = getIntent().getStringExtra("country");
            state = getIntent().getStringExtra("state");
            district = getIntent().getStringExtra("district");
        }
        castingViews();
        Spannable wordtoSpan = new SpannableString("Click Here to Register as a Unnathi Customer");
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 10, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_register.setText(wordtoSpan);

        onClickMethod();
        card_details_user.setVisibility(View.GONE);
        txt_assiging.setVisibility(View.GONE);
        txt_register.setVisibility(View.GONE);
        btn_add_lead.setVisibility(View.GONE);
        btn_cancel.setVisibility(View.GONE);
        layout_pbr.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
    }

    private void onClickMethod() {
        btn_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userMobile = user_number_edit.getText().toString();
                if (!validateMobileNumber()) {
                    return;
                } else {
                    getDetailsApiCall();
                }
            }
        });
        img_back_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        txt_assiging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, UserRegisterActivity.class);
                intent.putExtra("mobile",userMobile);
                startActivity(intent);
            }
        });
        btn_add_lead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserDetailsActivity.this, AddNewLeadActivity.class);
                intent.putExtra("cName", name);
                intent.putExtra("cEmail", email);
                intent.putExtra("cMobile", mobile);
                intent.putExtra("cUserId", customerId);
                intent.putExtra("country", country);
                intent.putExtra("state", state);
                intent.putExtra("district", district);
                startActivity(intent);
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void alertDialog() {
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure want to assign with this customer")
                .setCancelable(false)
                .setPositiveButton("Assign", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        assignApiCall();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void assignApiCall() {
        Map<String, String> assignMap = new HashMap<>();
        assignMap.put("user_id", customerId);
        assignMap.put("agent_id", agentId);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.assign(assignMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            String message = responseObject.getString("message");
                            Toast.makeText(UserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private boolean validateMobileNumber() {
        if (userMobile.isEmpty()) {
            user_number_edit.setError("Enter Customer Mobile Number");
            return false;
        } else {
            user_number_edit.setError(null);
        }
        return true;
    }

    private void getDetailsApiCall() {
        layout_pbr.setVisibility(View.VISIBLE);
        pbr.setVisibility(View.VISIBLE);
        userMobile = user_number_edit.getText().toString();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getCustomerDetails(userMobile);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    layout_pbr.setVisibility(View.GONE);
                    pbr.setVisibility(View.GONE);
                    String responseString = null;
                    try {
                        responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            String message = responseObject.getString("message");
                            Toast.makeText(UserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                            JSONObject detailsObject = responseObject.getJSONObject("details");
                            customerId = detailsObject.getString("id");
                            name = detailsObject.getString("first_name");
                            lastname = detailsObject.getString("last_name");
                            mobile = detailsObject.getString("mobile");
                            email = detailsObject.getString("email");
                            agent_assigned = detailsObject.getString("is_agent_assigned");
                            card_details_user.setVisibility(View.VISIBLE);
                            txt_assiging.setVisibility(View.VISIBLE);

                            txt_name.setText("User Name : " + name);
                            txt_lastname.setText("Last Name : " + lastname);
                            txt_mobile.setText("Mobile Number : " + mobile);
                            txt_email.setText("Registered Email : " + email);
                            txt_agent_assigned.setText("Is Assined With Agent :" + agent_assigned);
                            if (agent_assigned.equals("yes")) {
                                agent_name = detailsObject.getString("agent_name");
                                txt_agent_name.setText("Agent Name : " + agent_name);
                                txt_agent_name.setVisibility(View.VISIBLE);
                                txt_assiging.setVisibility(View.GONE);
                                txt_register.setVisibility(View.GONE);
                                btn_add_lead.setVisibility(View.VISIBLE);
                                btn_cancel.setVisibility(View.VISIBLE);
                            } else {
                                txt_agent_name.setVisibility(View.GONE);
                                btn_add_lead.setVisibility(View.GONE);
                                btn_cancel.setVisibility(View.GONE);
                            }
                            txt_register.setVisibility(View.GONE);
                        } else if (responseObject.getString("status").equalsIgnoreCase("invalid")) {
                            card_details_user.setVisibility(View.GONE);
                            txt_assiging.setVisibility(View.GONE);
                            txt_register.setVisibility(View.VISIBLE);
                            btn_add_lead.setVisibility(View.GONE);
                            btn_cancel.setVisibility(View.GONE);
                            layout_pbr.setVisibility(View.GONE);
                            pbr.setVisibility(View.GONE);
                            String message = responseObject.getString("message");
                            Toast.makeText(UserDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                        layout_pbr.setVisibility(View.GONE);
                        pbr.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                layout_pbr.setVisibility(View.GONE);
                pbr.setVisibility(View.GONE);
            }
        });
    }

    private void castingViews() {
        user_number_edit = findViewById(R.id.user_number_edit);
        btn_details = findViewById(R.id.btn_details);
        img_back_user = findViewById(R.id.img_back_user);
        txt_name = findViewById(R.id.txt_name);
        txt_lastname = findViewById(R.id.txt_lastname);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_email = findViewById(R.id.txt_email);
        txt_agent_assigned = findViewById(R.id.txt_agent_assigned);
        txt_agent_name = findViewById(R.id.txt_agent_name);
        card_details_user = findViewById(R.id.card_details_user);
        txt_assiging = findViewById(R.id.txt_assiging);
        txt_register = findViewById(R.id.txt_register);
        btn_add_lead = findViewById(R.id.btn_add_lead);
        btn_cancel = findViewById(R.id.btn_cancel);
        layout_pbr = findViewById(R.id.layout_pbr);
        pbr = findViewById(R.id.pbr);
    }
}