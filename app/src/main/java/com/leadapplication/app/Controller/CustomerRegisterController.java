package com.leadapplication.app.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRegisterController {
    public static CustomerRegisterController customerRegisterController;

    public static CustomerRegisterController getCustomerRegisterController() {
        if (customerRegisterController == null) {
            customerRegisterController = new CustomerRegisterController();
        }
        return customerRegisterController;
    }

    public interface CustomerRegisterListener {
        void onCustomerRegSuccess(JSONObject jsonObject);

        void onCustomerRegFailure(String failure);
    }

    public CustomerRegisterListener customerRegisterListener;

    public void setCustomerRegisterListener(CustomerRegisterListener customerRegisterListener) {
        this.customerRegisterListener = customerRegisterListener;
    }

    public void CustomerRegistrationApiCall(Map<String, String> customerMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.customerRegister(customerMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            if (customerRegisterListener != null) {
                                customerRegisterListener.onCustomerRegSuccess(responseObject);
                            }
                        }else{
                            if (customerRegisterListener != null) {
                                customerRegisterListener.onCustomerRegFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (customerRegisterListener != null) {
                            customerRegisterListener.onCustomerRegFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (customerRegisterListener != null) {
                    customerRegisterListener.onCustomerRegFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}

