package com.leadapplication.app.Controller;

import com.leadapplication.app.Model.SliderViewPagerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeBannersApiController {

    private static HomeBannersApiController homeBannersApiController;

    public static HomeBannersApiController getHomeBannersApiController() {
        if (homeBannersApiController == null) {
            homeBannersApiController = new HomeBannersApiController();
        }
        return homeBannersApiController;
    }

    public interface HomeBannersInreface {
        void onBannersSuccess(List<SliderViewPagerModel> sliderViewPagerModelList);

        void onbannersFailure(String failure);
    }

    public HomeBannersInreface homeBannersInreface;

    public void setHomeBannersInreface(HomeBannersInreface homeBannersInreface) {
        this.homeBannersInreface = homeBannersInreface;
    }

    public void homeBannersApiCall() {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getBanners();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            List<SliderViewPagerModel> sliderViewPagerModels = new ArrayList<>();
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                sliderViewPagerModels.add(new SliderViewPagerModel(
                                        listObject.getString("id"),
                                        listObject.getString("image"),
                                        listObject.getString("status"),
                                        listObject.getString("created_at"),
                                        listObject.getString("updated_at")
                                ));
                            }
                            if (homeBannersInreface != null) {
                                homeBannersInreface.onBannersSuccess(sliderViewPagerModels);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (homeBannersInreface != null) {
                            homeBannersInreface.onbannersFailure(e.getLocalizedMessage());
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (homeBannersInreface != null) {
                    homeBannersInreface.onbannersFailure(t.getLocalizedMessage());
                }

            }
        });
    }

}
