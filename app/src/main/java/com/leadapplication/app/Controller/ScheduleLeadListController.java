package com.leadapplication.app.Controller;

import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.Model.TodayLeadsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleLeadListController {
    public static ScheduleLeadListController scheduleLeadListController;

    public static ScheduleLeadListController getScheduleLeadListController() {
        if (scheduleLeadListController == null) {
            scheduleLeadListController = new ScheduleLeadListController();
        }
        return scheduleLeadListController;
    }

    public interface ScheduleLeadListener {
        void onScheduleLeadSuccess(List<CompletedDeadLeadModel> myLeadsModelList);

        void onScheduleLeadFailure(String failureMsg);
    }

    public ScheduleLeadListener scheduleLeadListener;

    public void setScheduleLeadListener(ScheduleLeadListener scheduleLeadListener) {
        this.scheduleLeadListener = scheduleLeadListener;
    }

    public void ScheduleLeadApiCall(Map<String, String> scheduleMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getAllLeads(scheduleMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            List<CompletedDeadLeadModel> todayLeadsModelList = new ArrayList<>();
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                todayLeadsModelList.add(new CompletedDeadLeadModel(listObject.getString("id"),
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
                            if (scheduleLeadListener != null) {
                                scheduleLeadListener.onScheduleLeadSuccess(todayLeadsModelList);
                            }
                        } else {
                            if (scheduleLeadListener != null) {
                                scheduleLeadListener.onScheduleLeadFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (scheduleLeadListener != null) {
                            scheduleLeadListener.onScheduleLeadFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (scheduleLeadListener != null) {
                    scheduleLeadListener.onScheduleLeadFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
