package com.leadapplication.app.Controller;

import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpController {
    public static OtpController otpController;

    public static OtpController getOtpController() {
        if (otpController == null) {
            otpController = new OtpController();
        }
        return otpController;
    }

    public interface OtpVerifyListener {
        void onOtpSuccess(JSONObject jsonObject);

        void onOtpFailure(String failureMsg);
    }

    public OtpVerifyListener otpVerifyListener;

    public void setOtpVerifyListener(OtpVerifyListener otpVerifyListener) {
        this.otpVerifyListener = otpVerifyListener;
    }

    public void OtpApiCall(Map<String, String> otpMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.otpVerify(otpMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            if (otpVerifyListener != null) {
                                otpVerifyListener.onOtpSuccess(responseObject);
                            } else {
                                if (otpVerifyListener != null) {
                                    otpVerifyListener.onOtpFailure(responseObject.getString("message"));
                                }
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (otpVerifyListener != null) {
                            otpVerifyListener.onOtpFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (otpVerifyListener != null) {
                    otpVerifyListener.onOtpFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
