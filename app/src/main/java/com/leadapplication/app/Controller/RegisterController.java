package com.leadapplication.app.Controller;

import org.json.JSONObject;

public class RegisterController {
    private static RegisterController registerController;

    public static RegisterController getRegisterController() {
        if (registerController == null) {
            registerController = new RegisterController();
        }
        return registerController;
    }

    public interface RegisterControlListener {
        void onRegisterSuccess(JSONObject successObject);

        void onRegisterFailure(String failureReason);
    }

    public RegisterControlListener registerControlListener;

    public void setRegisterControlListener(RegisterControlListener registerControlListener) {
        this.registerControlListener = registerControlListener;
    }

   /* public void RegisterApiCall(final Map<String, String> registerMap) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.registerApi(registerMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            if (registerControlListener != null) {
                                registerControlListener.onRegisterSuccess(responseObject);
                            } else {
                                if (registerControlListener != null) {
                                    registerControlListener.onRegisterFailure(responseObject.getString("message"));
                                }
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (registerControlListener != null) {
                            registerControlListener.onRegisterFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (registerControlListener != null) {
                    registerControlListener.onRegisterFailure(t.getLocalizedMessage());
                }
            }
        });
    }*/
}
