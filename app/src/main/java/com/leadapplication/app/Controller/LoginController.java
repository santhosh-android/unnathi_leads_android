package com.leadapplication.app.Controller;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    private static LoginController loginController;

    public static LoginController getLoginController() {
        if (loginController == null) {
            loginController = new LoginController();
        }
        return loginController;
    }

    public interface LoginControlListner {
        void onLoginSuccess(JSONObject jsonObject);

        void onLoginFailure(String failureString);
    }

    public LoginControlListner loginControlListner;

    public void setLoginControlListner(LoginControlListner loginControlListner) {
        this.loginControlListner = loginControlListner;
    }

    public void LoginApiCall(Map<String, String> loginMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.loginApi(loginMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            // String message = responseObject.getString("message");
                            //JSONObject userListObject = responseObject.getJSONObject("user_id");
                            if (loginControlListner != null) {
                                loginControlListner.onLoginSuccess(responseObject);
                            } else {
                                if (loginControlListner != null) {
                                    loginControlListner.onLoginFailure(responseObject.getString("message"));
                                }
                            }
                        }else {
                            if (loginControlListner != null) {
                                loginControlListner.onLoginFailure(responseObject.getString("message"));
                            }
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (loginControlListner != null) {
                            loginControlListner.onLoginFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (loginControlListner != null) {
                    loginControlListner.onLoginFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
