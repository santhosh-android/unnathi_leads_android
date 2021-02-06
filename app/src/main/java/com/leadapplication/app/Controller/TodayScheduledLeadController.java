package com.leadapplication.app.Controller;

import android.widget.Toast;

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

public class TodayScheduledLeadController {
    public static TodayScheduledLeadController todayScheduledLeadController;

    public static TodayScheduledLeadController getTodayScheduledLeadController() {
        if (todayScheduledLeadController == null) {
            todayScheduledLeadController = new TodayScheduledLeadController();
        }
        return todayScheduledLeadController;
    }

    public interface TodayScheduledListener {
        void onTodayScheduleSuccess(List<TodayLeadsModel> todayLeadsModelList);

        void onTodayScheduleFailure(String failureMsg);
    }

    public TodayScheduledListener todayScheduledListener;

    public void setTodayScheduledListener(TodayScheduledListener todayScheduledListener) {
        this.todayScheduledListener = todayScheduledListener;
    }

    public void TodayScheduleApiCall(Map<String, String> tScheduleMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getTodaySchedule(tScheduleMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            List<TodayLeadsModel> todayLeadsModelList = new ArrayList<>();
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                todayLeadsModelList.add(new TodayLeadsModel(listObject.getString("id"),
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
                                        listObject.getString("sub_category_name"),
                                        listObject.getString("title")));
                            }
                            if (todayScheduledListener != null) {
                                todayScheduledListener.onTodayScheduleSuccess(todayLeadsModelList);
                            }
                        }else {
                            if (todayScheduledListener != null) {
                                todayScheduledListener.onTodayScheduleFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (todayScheduledListener != null) {
                            todayScheduledListener.onTodayScheduleFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (todayScheduledListener != null) {
                    todayScheduledListener.onTodayScheduleFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}