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

import com.leadapplication.app.Activity.AllLeadsActivity;
import com.leadapplication.app.Activity.FollwUpsViewActivity;
import com.leadapplication.app.Activity.ManageLeadsActivity;
import com.leadapplication.app.Adapter.ToSetListAdapter;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.TosetLeadsController;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.TosetModelList;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToSetFragment extends Fragment implements TosetLeadsController.TosetInterfaceListener, ToSetListAdapter.TosetListener {
    private RelativeLayout layout_progress, layout_data;
    private ProgressBar pbr;
    private TextView tv_noleads;
    private RecyclerView rv_today_leads;
    private SharedPreferences sharedPreferences;
    private String agentId;
    private List<TosetModelList> tosetModelListList;
    private ToSetListAdapter toSetListAdapter;

    //pagination
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    public ToSetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_to_set, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("unnathiLead", Context.MODE_PRIVATE);
        agentId = sharedPreferences.getString("agent_id", "");

        layout_progress = view.findViewById(R.id.layout_progress);
        layout_data = view.findViewById(R.id.layout_data);
        pbr = view.findViewById(R.id.pbr);
        tv_noleads = view.findViewById(R.id.tv_noleads);
        rv_today_leads = view.findViewById(R.id.rv_today_leads);
        getTosetLeadsApiCall();
    }

    @Override
    public void onResume() {
        super.onResume();
        getTosetLeadsApiCall();
    }

    private void getTosetLeadsApiCall() {
        layout_data.setVisibility(View.GONE);
        layout_progress.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.GONE);
        pbr.setVisibility(View.VISIBLE);
        Map<String, String> toSetMap = new HashMap<>();
        toSetMap.put("agent_id", agentId);
        toSetMap.put("start", "");
        toSetMap.put("per_page", "5000");
        toSetMap.put("status_id", ((ManageLeadsActivity) requireActivity()).selectedStatus);
        toSetMap.put("usersearch", "");
        TosetLeadsController.getTosetLeadsController().getTosetLeads(toSetMap);
        TosetLeadsController.getTosetLeadsController().setTosetInterfaceListener(this);
    }

    @Override
    public void onTosetSuccess(List<TosetModelList> tosetModelList) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        tv_noleads.setVisibility(View.GONE);
        layout_data.setVisibility(View.VISIBLE);
        this.tosetModelListList = new ArrayList<>(tosetModelList);
        toSetLeadsRecyclerView(tosetModelList);
    }

    private void toSetLeadsRecyclerView(List<TosetModelList> tosetModelList) {
        rv_today_leads.setHasFixedSize(true);
        rv_today_leads.setLayoutManager(new LinearLayoutManager(getActivity()));
        toSetListAdapter = new ToSetListAdapter(getActivity(), tosetModelListList);
        toSetListAdapter.setTosetListener(this);
        rv_today_leads.setAdapter(toSetListAdapter);
    }

    @Override
    public void onTosetFailure(String tosetFailure) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        layout_data.setVisibility(View.GONE);
        tv_noleads.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemListener(int position) {
        Intent intent = new Intent(getActivity(), FollwUpsViewActivity.class);
        intent.putExtra("lead_id", tosetModelListList.get(position).getId());
        startActivity(intent);
    }

}