package com.leadapplication.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leadapplication.app.Activity.FollwUpsViewActivity;
import com.leadapplication.app.Activity.ManageLeadsActivity;
import com.leadapplication.app.Adapter.TodayLeadsAdapter;
import com.leadapplication.app.Controller.TodayScheduledLeadController;
import com.leadapplication.app.Model.TodayLeadsModel;
import com.leadapplication.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TodayManagingLeadsFragment extends Fragment implements TodayLeadsAdapter.LeadsViewInterface, TodayScheduledLeadController.TodayScheduledListener {

    private RecyclerView rv_today_leads;
    private TodayLeadsAdapter todayLeadsAdapter;
    private List<TodayLeadsModel> todayLeadsModelList;
    private SharedPreferences sharedPreferences;
    private String agent_id;
    private RelativeLayout layout_progress, layout_data;
    private ProgressBar pbr;
    private TextView tv_noleads;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TodayManagingLeadsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TodayManagingLeadsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodayManagingLeadsFragment newInstance(String param1, String param2) {
        TodayManagingLeadsFragment fragment = new TodayManagingLeadsFragment();
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

        View mView = inflater.inflate(R.layout.fragment_today_managing_leads, container, false);

        rv_today_leads = mView.findViewById(R.id.rv_today_leads);
        layout_progress = mView.findViewById(R.id.layout_progress);
        layout_data = mView.findViewById(R.id.layout_data);
        pbr = mView.findViewById(R.id.pbr);
        tv_noleads = mView.findViewById(R.id.tv_noleads);

        sharedPreferences = getActivity().getSharedPreferences("unnathiLead", Context.MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        layout_data.setVisibility(View.GONE);
        layout_progress.setVisibility(View.VISIBLE);
        getTodayScheduleList();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTodayScheduleList();
    }


    private void getTodayScheduleList() {
        layout_progress.setVisibility(View.VISIBLE);
        pbr.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.GONE);
        Map<String, String> todayScheduleMap = new HashMap<>();
        todayScheduleMap.put("agent_id", agent_id);
        todayScheduleMap.put("start", "");
        todayScheduleMap.put("per_page", "5000");
        todayScheduleMap.put("status_id",  ((ManageLeadsActivity) requireActivity()).selectedStatus);
        todayScheduleMap.put("usersearch", "");
        TodayScheduledLeadController.getTodayScheduledLeadController().TodayScheduleApiCall(todayScheduleMap);
        TodayScheduledLeadController.getTodayScheduledLeadController().setTodayScheduledListener(this);
    }

    private void recyclerViewInitilization(List<TodayLeadsModel> todayLeadsModelList) {
        rv_today_leads.setHasFixedSize(true);
        rv_today_leads.setLayoutManager(new LinearLayoutManager(getActivity()));

        todayLeadsAdapter = new TodayLeadsAdapter(getActivity(), todayLeadsModelList);
        todayLeadsAdapter.setLeadsViewInterface(this);
        todayLeadsAdapter.notifyDataSetChanged();
        rv_today_leads.setAdapter(todayLeadsAdapter);
    }


    @Override
    public void onTodayScheduleSuccess(List<TodayLeadsModel> todayLeadsModelList) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        layout_data.setVisibility(View.VISIBLE);
        tv_noleads.setVisibility(View.GONE);
        this.todayLeadsModelList = new ArrayList<>(todayLeadsModelList);
        recyclerViewInitilization(todayLeadsModelList);
    }

    @Override
    public void onTodayScheduleFailure(String failureMsg) {
        layout_progress.setVisibility(View.GONE);
        pbr.setVisibility(View.GONE);
        tv_noleads.setVisibility(View.VISIBLE);
        layout_data.setVisibility(View.GONE);
    }

    @Override
    public void onLeadItemClickListener(int position) {
        Intent viewIntent = new Intent(getActivity(), FollwUpsViewActivity.class);
        viewIntent.putExtra("lead_id", todayLeadsModelList.get(position).getId());
        startActivity(viewIntent);
    }
}