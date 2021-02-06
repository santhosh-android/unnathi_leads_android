package com.leadapplication.app.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckAgentStatusController {
    public static CheckAgentStatusController checkAgentStatusController;

    public static CheckAgentStatusController getCheckAgentStatusController() {
        if (checkAgentStatusController == null) {
            checkAgentStatusController = new CheckAgentStatusController();
        }
        return checkAgentStatusController;
    }

    public interface CheckAgentListener {
        void onAgentSucces(JSONObject jsonObject);

        void onAgentFailure(String failureString);
    }

    public CheckAgentListener checkAgentListener;

    public void setCheckAgentListener(CheckAgentListener checkAgentListener) {
        this.checkAgentListener = checkAgentListener;
    }

    public void CheckAgentStatus(String userId) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getStatus(userId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObje = new JSONObject(responseStr);
                        if (responseObje.getString("status").equalsIgnoreCase("valid")) {
                            if (checkAgentListener != null) {
                                checkAgentListener.onAgentSucces(responseObje);
                            }
                        } else {
                            if (checkAgentListener != null) {
                                checkAgentListener.onAgentFailure(responseObje.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (checkAgentListener != null) {
                            checkAgentListener.onAgentFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (checkAgentListener != null) {
                    checkAgentListener.onAgentFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
