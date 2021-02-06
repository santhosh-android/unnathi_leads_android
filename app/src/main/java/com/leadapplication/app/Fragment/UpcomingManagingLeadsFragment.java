package com.leadapplication.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leadapplication.app.Activity.FollwUpsViewActivity;
import com.leadapplication.app.Activity.ManageLeadsActivity;
import com.leadapplication.app.Adapter.CompletedDeadLeadAdapter;
import com.leadapplication.app.Adapter.TodayLeadsAdapter;
import com.leadapplication.app.Adapter.UpcomingLeadsAdapter;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.ScheduleLeadListController;
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
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpcomingManagingLeadsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpcomingManagingLeadsFragment extends Fragment implements ScheduleLeadListController.ScheduleLeadListener, CompletedDeadLeadAdapter.LeadsInterface, LocationSpinnersController.LocationSpinnerListener {
    private RecyclerView rv_upcoming_leads;
    private List<CompletedDeadLeadModel> myleadsModelList;
    private CompletedDeadLeadAdapter upcomingLeadsAdapter;
    private SharedPreferences sharedPreferences;
    private String agent_id;
    private RelativeLayout layout_data, layout_progress;
    private ProgressBar pbr;
    private TextView tv_noleads;
    private Spinner status_spinner;
    private List<StatusModel> statusModelList;
    private String selectedStatus;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingManagingLeadsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpcomingManagingLeadsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingManagingLeadsFragment newInstance(String param1, String param2) {
        UpcomingManagingLeadsFragment fragment = new UpcomingManagingLeadsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_upcoming_managing_leads, container, false);


        sharedPreferences = getActivity().getSharedPreferences("unnathiLead", Context.MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");

        rv_upcoming_leads = mView.findViewById(R.id.rv_upcoming_leads);
        layout_data = mView.findViewById(R.id.layout_data);
        layout_progress = mView.findViewById(R.id.layout_progress);
        pbr = mView.findViewById(R.id.pbr);
        tv_noleads = mView.findViewById(R.id.tv_noleads);
        tv_noleads.setVisibility(View.GONE);
        status_spinner = mView.findViewById(R.id.status_spinner);
        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StatusModel model = statusModelList.get(position);
                    selectedStatus = model.getId();
                    getScheduledListWithStatus();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        LocationSpinnersController.getLocationSpinnersController().getStatus();
        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(this);

        layout_data.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        layout_progress.setVisibility(View.VISIBLE);
        getScheduleLeadListApiCall();
        return mView;
    }

    private void getScheduledListWithStatus() {
        layout_data.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        layout_progress.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.VISIBLE);
        Map<String, String> scheduleMap = new HashMap<>();
        scheduleMap.put("agent_id", agent_id);
        scheduleMap.put("start", "");
        scheduleMap.put("per_page", "5000");
        scheduleMap.put("status_id", ((ManageLeadsActivity) requireActivity()).selectedStatus);
        ScheduleLeadListController.getScheduleLeadListController().ScheduleLeadApiCall(scheduleMap);
        ScheduleLeadListController.getScheduleLeadListController().setScheduleLeadListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getScheduleLeadListApiCall();
    }

    private void getScheduleLeadListApiCall() {
        layout_data.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        layout_progress.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.VISIBLE);
        Map<String, String> scheduleMap = new HashMap<>();
        scheduleMap.put("agent_id", agent_id);
        scheduleMap.put("start", "");
        scheduleMap.put("per_page", "5000");
        scheduleMap.put("status_id", ((ManageLeadsActivity) requireActivity()).selectedStatus);
        scheduleMap.put("usersearch", "");
        ScheduleLeadListController.getScheduleLeadListController().ScheduleLeadApiCall(scheduleMap);
        ScheduleLeadListController.getScheduleLeadListController().setScheduleLeadListener(this);
    }

    private void upcomingLeadsRecyclerView(List<CompletedDeadLeadModel> upcomingModelList) {
        rv_upcoming_leads.setHasFixedSize(true);
        rv_upcoming_leads.setLayoutManager(new LinearLayoutManager(getActivity()));

        upcomingLeadsAdapter = new CompletedDeadLeadAdapter(getActivity(), upcomingModelList);
        upcomingLeadsAdapter.setLeadsInterface(this);
        rv_upcoming_leads.setAdapter(upcomingLeadsAdapter);
    }

    @Override
    public void onScheduleLeadSuccess(List<CompletedDeadLeadModel> todayLeadsModelList) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        layout_data.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.GONE);
        this.myleadsModelList = new ArrayList<>(todayLeadsModelList);
        upcomingLeadsRecyclerView(todayLeadsModelList);
    }

    @Override
    public void onScheduleLeadFailure(String failureMsg) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        tv_noleads.setVisibility(View.VISIBLE);
        layout_data.setVisibility(View.GONE);
    }

    @Override
    public void onLeadClickListener(int position) {
        Intent viewIntent = new Intent(getActivity(), FollwUpsViewActivity.class);
        viewIntent.putExtra("lead_id", myleadsModelList.get(position).getIdCrd());
        startActivity(viewIntent);
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
        ArrayAdapter<StatusModel> arrayAdapter = new ArrayAdapter<StatusModel>(getActivity(),
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
        Toast.makeText(getActivity(), statusFail, Toast.LENGTH_SHORT).show();
    }
}