package com.leadapplication.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.leadapplication.app.Adapter.CompletedDeadLeadAdapter;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLeadsActivity extends AppCompatActivity implements CompletedDeadLeadAdapter.LeadsInterface, LocationSpinnersController.LocationSpinnerListener {
    private RecyclerView rv_allLeads;
    private TextView txt_no, tv_search;
    private ImageView img_back_leads;
    private String agent_id, spinnerId, selectedSpinner;
    private RelativeLayout layout_progress;
    private SharedPreferences sharedPreferences;
    private ProgressBar pbr;
    private List<StatusModel> statusModelList;
    private List<CompletedDeadLeadModel> completedDeadLeadModelList;
    private Spinner status_spinner;
    private EditText et_searchTxt;
    private CompletedDeadLeadAdapter adapter;
    private int startInt = 0, perpage = 10;
    private boolean arePostsLoading;
    private boolean firstApiCall = true;
    private NestedScrollView scroll_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_leads);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        castingViews();

        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        rv_allLeads.setVisibility(View.GONE);
        txt_no.setVisibility(View.GONE);

        img_back_leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        LocationSpinnersController.getLocationSpinnersController().getStatus();
        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(AllLeadsActivity.this);

        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StatusModel model = statusModelList.get(position);
                    spinnerId = model.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatemobile()) {
                    return;
                } else {
                    layout_progress.setVisibility(View.VISIBLE);
                    getAllLeadsApiCall();
                }
            }
        });
    }

    private void getAllLeadsApiCall() {
        Map<String, String> allMap = new HashMap<>();
        allMap.put("agent_id", agent_id);
        allMap.put("start", "");
        allMap.put("per_page", "5000");
        allMap.put("status_id", "");
        allMap.put("usersearch", et_searchTxt.getText().toString().trim());
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getAllLeads(allMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    layout_progress.setVisibility(View.GONE);
                    pbr.setVisibility(View.GONE);
                    rv_allLeads.setVisibility(View.VISIBLE);
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObj = new JSONObject(responseStr);
                        if (responseObj.getString("status").equalsIgnoreCase("valid")) {
                            completedDeadLeadModelList = new ArrayList<>();
                            JSONArray listArray = responseObj.getJSONArray("list");
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                completedDeadLeadModelList.add(new CompletedDeadLeadModel(listObject.getString("id"),
                                        listObject.getString("lead_id"),
                                        listObject.getString("user_id"),
                                        listObject.getString("agent_id"),
                                        listObject.getString("alternate_number"),
                                        listObject.getString("lead_created_by"),
                                        listObject.getString("country_id"),
                                        listObject.getString("state_id"),
                                        listObject.getString("district_id"),
                                        listObject.getString("mandal_id"),
                                        listObject.getString("village_id"),
                                        listObject.getString("pincode_id"),
                                        listObject.getString("address"),
                                        listObject.getString("service_type"),
                                        listObject.getString("category_id"),
                                        listObject.getString("sub_category_id"),
                                        listObject.getString("status"),
                                        listObject.getString("date"),
                                        listObject.getString("time"),
                                        listObject.getString("venue"),
                                        listObject.getString("remarks"),
                                        listObject.getString("lead_type"),
                                        listObject.getString("lead_source"),
                                        listObject.getString("amount"),
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
                                        listObject.getString("sub_category_name"),
                                        listObject.getString("title")));
                            }
                            rv_allLeads.setHasFixedSize(true);
                            rv_allLeads.setLayoutManager(new LinearLayoutManager(AllLeadsActivity.this));
                            adapter = new CompletedDeadLeadAdapter(AllLeadsActivity.this, completedDeadLeadModelList);
                            adapter.setLeadsInterface(AllLeadsActivity.this);
                            rv_allLeads.setAdapter(adapter);
                        } else {
                            layout_progress.setVisibility(View.GONE);
                            pbr.setVisibility(View.GONE);
                            rv_allLeads.setVisibility(View.GONE);
                            txt_no.setVisibility(View.VISIBLE);
                            String failureMsg = responseObj.getString("message");
                            Toast.makeText(AllLeadsActivity.this, failureMsg, Toast.LENGTH_SHORT).show();
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
        rv_allLeads = findViewById(R.id.rv_allLeads);
        txt_no = findViewById(R.id.txt_no);
        img_back_leads = findViewById(R.id.img_back_leads);
        layout_progress = findViewById(R.id.layout_progress);
        pbr = findViewById(R.id.pbr);
        tv_search = findViewById(R.id.tv_search);
        status_spinner = findViewById(R.id.status_spinner);
        et_searchTxt = findViewById(R.id.et_searchTxt);
        scroll_view = findViewById(R.id.scroll_view);
    }

    @Override
    public void onLeadClickListener(int position) {
        Intent intent = new Intent(AllLeadsActivity.this, FollwUpsViewActivity.class);
        intent.putExtra("lead_id", completedDeadLeadModelList.get(position).getIdCrd());
        startActivity(intent);
    }

    public boolean validatemobile() {
        if (et_searchTxt.getText().length() == 0) {
            et_searchTxt.setError("Enter Email or Mobile");
            return false;
        } else {
            et_searchTxt.setError(null);
            return true;
        }
    }

    @Override
    public void onCountrySuccess(List<CountryModel> countryModelList) {

    }

    @Override
    public void onCountryFailure(String countryFailure) {

    }

    @Override
    public void onStateSuccess(List<StateModel> stateModelList) {

    }

    @Override
    public void onStatreFailure(String stateFailure) {

    }

    @Override
    public void onDistrictSuccess(List<DistrictModel> districtModelList) {

    }

    @Override
    public void onDistrictFailure(String districtFailure) {

    }

    @Override
    public void onCitySuccess(List<CityModel> cityModelList) {

    }

    @Override
    public void onCityFailure(String cityFailure) {

    }

    @Override
    public void onVillageSuccess(List<VillageModel> villageModelList) {

    }

    @Override
    public void onVillageFailure(String villageFailure) {

    }

    @Override
    public void onPincodeSuccess(List<PincodeModel> pincodeModelList) {

    }

    @Override
    public void onPincodeFailure(String picodeFailure) {

    }

    @Override
    public void onSourceSuccess(List<SourceSpinnerModel> sourceSpinnerModelList) {

    }

    @Override
    public void onSourceFailure(String sourceFailure) {

    }

    @Override
    public void onIamSuccess(List<IamSpinnerModel> iamSpinnerModelList) {

    }

    @Override
    public void onIamFailure(String iamFailure) {

    }

    @Override
    public void onStatusSuccess(List<StatusModel> statusModelList) {
        this.statusModelList = new ArrayList<>(statusModelList);
        this.statusModelList.add(0, new StatusModel("", "Status", ""));
        ArrayAdapter<StatusModel> arrayAdapter = new ArrayAdapter<StatusModel>(AllLeadsActivity.this,
                R.layout.status_spinner_item, this.statusModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(R.layout.status_spinner_item);
        status_spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onStatusFailure(String statusFail) {
        Toast.makeText(this, statusFail, Toast.LENGTH_SHORT).show();
    }
}