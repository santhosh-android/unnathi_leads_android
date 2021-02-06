package com.leadapplication.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.leadapplication.app.Adapter.ManageLeadsViewPagerAdapter;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Fragment.ToSetFragment;
import com.leadapplication.app.Fragment.TodayManagingLeadsFragment;
import com.leadapplication.app.Fragment.UpcomingManagingLeadsFragment;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.TodayLeadsModel;
import com.leadapplication.app.Model.TosetModelList;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageLeadsActivity extends AppCompatActivity implements LocationSpinnersController.LocationSpinnerListener {
    private TabLayout tab_manage_leads;
    private ViewPager viewpager_manage_leads;
    private ImageView img_add, img_back_manage, img_search;
    private SharedPreferences sharedPreferences;
    private String userId, schedule = "", allLeads = "", toset = "";
    public String selectedStatus = "";
    private Spinner status_spinner;
    private List<StatusModel> statusModelList;
    private ProgressBar pbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_leads);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        userId = sharedPreferences.getString("agent_id", "");

        tab_manage_leads = findViewById(R.id.tab_manage_leads);
        viewpager_manage_leads = findViewById(R.id.viewpager_manage_leads);
        img_back_manage = findViewById(R.id.img_back_manage);
        status_spinner = findViewById(R.id.status_spinner);
        img_search = findViewById(R.id.img_search);
        pbr = findViewById(R.id.pbr);
        img_add = findViewById(R.id.img_add);

        LocationSpinnersController.getLocationSpinnersController().getStatus();
        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(ManageLeadsActivity.this);

        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addLeadIntent = new Intent(ManageLeadsActivity.this, AddNewLeadActivity.class);
                startActivity(addLeadIntent);
            }
        });
        img_back_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageLeadsActivity.this, AllLeadsActivity.class));
            }
        });

        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StatusModel model = statusModelList.get(position);
                    selectedStatus = model.getId();
                    getLeadsCountApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLeadsCountApiCall();
    }

    private void getLeadsCountApiCall() {
        pbr.setVisibility(View.VISIBLE);
        Map<String, String> countMap = new HashMap<>();
        countMap.put("agent_id", userId);
        countMap.put("status_id", selectedStatus);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getCount(countMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    pbr.setVisibility(View.GONE);
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObj = new JSONObject(responseStr);
                        if (responseObj.getString("status").equalsIgnoreCase("valid")) {
                            schedule = responseObj.getString("schedules_leads_count");
                            allLeads = responseObj.getString("all_leads_count");
                            toset = responseObj.getString("toset_leads_count");
                            tabsetUp();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        pbr.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                pbr.setVisibility(View.GONE);
            }
        });
    }

    private void tabsetUp() {
        tab_manage_leads.addTab(tab_manage_leads.newTab().setText("To Do" + " (" + schedule + ")"));
        tab_manage_leads.addTab(tab_manage_leads.newTab().setText("Follow-ups" + " (" + allLeads + ")"));
        tab_manage_leads.addTab(tab_manage_leads.newTab().setText("To Set" + " (" + toset + ")"));

        tab_manage_leads.setTabGravity(TabLayout.GRAVITY_FILL);
        tab_manage_leads.setSelectedTabIndicatorColor(getResources().getColor(R.color.green));

        ManageLeadsViewPagerAdapter pagerAdapter = new ManageLeadsViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.AddFragment(new TodayManagingLeadsFragment(), "To Do" + " (" + schedule + ")");
        pagerAdapter.AddFragment(new UpcomingManagingLeadsFragment(), "Follow ups" + " (" + allLeads + ")");
        pagerAdapter.AddFragment(new ToSetFragment(), "To Set" + " (" + toset + ")");
        viewpager_manage_leads.setAdapter(pagerAdapter);
        tab_manage_leads.setupWithViewPager(viewpager_manage_leads);
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
        this.statusModelList.add(0, new StatusModel("", "Select Status", ""));
        this.statusModelList.add(1, new StatusModel("", "All", ""));
        ArrayAdapter<StatusModel> arrayAdapter = new ArrayAdapter<StatusModel>(ManageLeadsActivity.this,
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
        tabsetUp();
    }

    @Override
    public void onStatusFailure(String statusFail) {
        Toast.makeText(this, statusFail, Toast.LENGTH_SHORT).show();
    }
}