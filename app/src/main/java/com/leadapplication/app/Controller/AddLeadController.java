package com.leadapplication.app.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLeadController {
    public static AddLeadController addLeadController;

    public static AddLeadController getAddLeadController() {
        if (addLeadController == null) {
            addLeadController = new AddLeadController();
        }
        return addLeadController;
    }

    public interface AddLeadListener {
        void onAddLeadSuccess(JSONObject addLeadObject);

        void onAddLeadFailure(String failureMsg);
    }

    public AddLeadListener addLeadListener;

    public void setAddLeadListener(AddLeadListener addLeadListener) {
        this.addLeadListener = addLeadListener;
    }

    public void AddLeadApiCall(Map<String, String> addLeadMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.addLead(addLeadMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            if (addLeadListener != null) {
                                addLeadListener.onAddLeadSuccess(responseObject);
                            }
                        } else {
                            if (addLeadListener != null) {
                                addLeadListener.onAddLeadFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (addLeadListener != null) {
                            addLeadListener.onAddLeadFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (addLeadListener != null) {
                    addLeadListener.onAddLeadFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
