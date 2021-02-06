package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadapplication.app.Adapter.CompletedDeadLeadAdapter;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompletedAndCancelActivity extends AppCompatActivity {

    private ImageView img_back_list;
    private RecyclerView rv_completedLeads;
    private SharedPreferences sharedPreferences;
    private String agent_id;
    private List<CompletedDeadLeadModel> completedDeadLeadModelList;
    private ProgressBar pbr;
    private RelativeLayout layout_progress;
    private TextView txt_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_and_cancel);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        castingViews();
        getCompletedandCancelLeads();
        rv_completedLeads.setHasFixedSize(true);
        rv_completedLeads.setLayoutManager(new LinearLayoutManager(this));
        layout_progress.setVisibility(View.VISIBLE);
        pbr.setVisibility(View.VISIBLE);
        rv_completedLeads.setVisibility(View.GONE);
        txt_no.setVisibility(View.GONE);
        click();
    }

    private void click() {
        img_back_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getCompletedandCancelLeads() {
        layout_progress.setVisibility(View.VISIBLE);
        pbr.setVisibility(View.VISIBLE);
        rv_completedLeads.setVisibility(View.GONE);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getCompletedleads(agent_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    layout_progress.setVisibility(View.GONE);
                    pbr.setVisibility(View.GONE);
                    rv_completedLeads.setVisibility(View.VISIBLE);
                    txt_no.setVisibility(View.GONE);
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            completedDeadLeadModelList = new ArrayList<>();
                            JSONArray listArray = responseObject.getJSONArray("list");
                            /*for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                completedDeadLeadModelList.add(new CompletedDeadLeadModel(listObject.getString("id"),
                                        listObject.getString("lead_id"),
                                        listObject.getString("user_id"),
                                        listObject.getString("agent_id"),
                                        listObject.getString("lead_created_by"),
                                        listObject.getString("country_id"),
                                        listObject.getString("state_id"),
                                        listObject.getString("district_id"),
                                        listObject.getString("mandal_id"),
                                        listObject.getString("village_id"),
                                        listObject.getString("pincode_id"),
                                        listObject.getString("service_type"),
                                        listObject.getString("category_id"),
                                        listObject.getString("sub_category_id"),
                                        listObject.getString("status"),
                                        listObject.getString("date"),
                                        listObject.getString("time"),
                                        listObject.getString("venue"),
                                        listObject.getString("remarks"),
                                        listObject.getString("created_at"),
                                        listObject.getString("user_name"),
                                        listObject.getString("mobile"),
                                        listObject.getString("email"),
                                        listObject.getString("country"),
                                        listObject.getString("state"),
                                        listObject.getString("district"),
                                        listObject.getString("mandal"),
                                        listObject.getString("village"),
                                        listObject.getString("pincode"),
                                        listObject.getString("category_name"),
                                        listObject.getString("sub_category_name")));
                            }*/
                            CompletedDeadLeadAdapter adapter = new CompletedDeadLeadAdapter(CompletedAndCancelActivity.this, completedDeadLeadModelList);
                            rv_completedLeads.setAdapter(adapter);
                        } else {
                            layout_progress.setVisibility(View.GONE);
                            pbr.setVisibility(View.GONE);
                            rv_completedLeads.setVisibility(View.GONE);
                            txt_no.setVisibility(View.VISIBLE);
                            String failureMsg = responseObject.getString("message");
                            Toast.makeText(CompletedAndCancelActivity.this, failureMsg, Toast.LENGTH_SHORT).show();
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
        img_back_list = findViewById(R.id.img_back_list);
        rv_completedLeads = findViewById(R.id.rv_completedLeads);
        layout_progress = findViewById(R.id.layout_progress);
        pbr = findViewById(R.id.pbr);
        txt_no = findViewById(R.id.txt_no);
    }

}