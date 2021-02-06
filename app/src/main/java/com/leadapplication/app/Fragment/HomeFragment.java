package com.leadapplication.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.leadapplication.app.Activity.CompletedAndCancelActivity;
import com.leadapplication.app.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private PieChart pieChart;
    private TextView tv_followup, tv_completed, tv_cancelled;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        castingViews(view);
        setPieChart();
        clickMethods();

    }

    private void clickMethods() {
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });

        tv_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CompletedAndCancelActivity.class));
            }
        });

    }

    private void castingViews(@NonNull View view) {
        pieChart = view.findViewById(R.id.piechart_1);
        tv_followup = view.findViewById(R.id.tv_followup);
        tv_completed = view.findViewById(R.id.tv_completed);
        tv_cancelled = view.findViewById(R.id.tv_cancelled);
    }

    public void setPieChart() {
        this.pieChart = pieChart;
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);

        //pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(34f, "FollwUps"));
        yValues.add(new PieEntry(56f, "Completed"));
        yValues.add(new PieEntry(66f, "Meeting"));

        /* ArrayList pieChartData = new ArrayList();
        pieChartData.add(new PieEntry(34f));
        pieChartData.add(new PieEntry(56f));
        pieChartData.add(new PieEntry(66f));

        ArrayList pieLabel = new ArrayList();
        pieLabel.add("FollwUps");
        pieLabel.add("Completed");
        pieLabel.add("Meeting");*/

        PieDataSet dataSet = new PieDataSet(yValues, "Leads");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        /*int[] colors = new int[] {Color.GREEN, Color.YELLOW, Color.GREEN, Color.BLUE, Color.GRAY, Color.BLACK};
        dataSet.setColors(colors);*/
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.YELLOW);
        pieChart.setData(pieData);
        //PieChart Ends Here
    }
}