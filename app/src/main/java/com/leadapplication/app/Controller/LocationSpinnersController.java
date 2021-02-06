package com.leadapplication.app.Controller;

import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.VillageModel;

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

public class LocationSpinnersController {
    public static LocationSpinnersController locationSpinnersController;

    public static LocationSpinnersController getLocationSpinnersController() {
        if (locationSpinnersController == null) {
            locationSpinnersController = new LocationSpinnersController();
        }
        return locationSpinnersController;
    }

    public interface LocationSpinnerListener {
        void onCountrySuccess(List<CountryModel> countryModelList);

        void onCountryFailure(String countryFailure);

        void onStateSuccess(List<StateModel> stateModelList);

        void onStatreFailure(String stateFailure);

        void onDistrictSuccess(List<DistrictModel> districtModelList);

        void onDistrictFailure(String districtFailure);

        void onCitySuccess(List<CityModel> cityModelList);

        void onCityFailure(String cityFailure);

        void onVillageSuccess(List<VillageModel> villageModelList);

        void onVillageFailure(String villageFailure);

        void onPincodeSuccess(List<PincodeModel> pincodeModelList);

        void onPincodeFailure(String picodeFailure);

        void onSourceSuccess(List<SourceSpinnerModel> sourceSpinnerModelList);

        void onSourceFailure(String sourceFailure);

        void onIamSuccess(List<IamSpinnerModel> iamSpinnerModelList);

        void onIamFailure(String iamFailure);

        void onStatusSuccess(List<StatusModel> statusModelList);

        void onStatusFailure(String statusFail);
    }

    public LocationSpinnerListener locationSpinnerListener;

    public void setLocationSpinnerListener(LocationSpinnerListener locationSpinnerListener) {
        this.locationSpinnerListener = locationSpinnerListener;
    }

    public void CountryApiCall() {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getCountries();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            List<CountryModel> countryModelsList = new ArrayList<>();
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                countryModelsList.add(new CountryModel(listObject.getString("id"),
                                        listObject.getString("country"),
                                        listObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onCountrySuccess(countryModelsList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onCountryFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onCountryFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onCountryFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getStatesApiCall(String countryId) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getStates(countryId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            List<StateModel> stateModelList = new ArrayList<>();
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject stateObject = stateListArray.getJSONObject(i);
                                stateModelList.add(new StateModel(stateObject.getString("id"),
                                        stateObject.getString("country_id"),
                                        stateObject.getString("state"),
                                        stateObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onStateSuccess(stateModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onStatreFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onStatreFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onStatreFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getDistrictsApiCall(String stateId) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getDistricts(stateId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            List<DistrictModel> districtModelList = new ArrayList<>();
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject districtObject = stateListArray.getJSONObject(i);
                                districtModelList.add(new DistrictModel(districtObject.getString("id"),
                                        districtObject.getString("country_id"),
                                        districtObject.getString("state_id"),
                                        districtObject.getString("district"),
                                        districtObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onDistrictSuccess(districtModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onDistrictFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onDistrictFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onDistrictFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getCityApiCall(String districtId) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getMandal(districtId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            List<CityModel> cityModelList = new ArrayList<>();
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject districtObject = stateListArray.getJSONObject(i);
                                cityModelList.add(new CityModel(districtObject.getString("id"),
                                        districtObject.getString("country_id"),
                                        districtObject.getString("state_id"),
                                        districtObject.getString("district_id"),
                                        districtObject.getString("mandal"),
                                        districtObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onCitySuccess(cityModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onCityFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onCityFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onCityFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getVillageApiCall(String villageID) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getVillages(villageID);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            List<VillageModel> villageModelList = new ArrayList<>();
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject villageObject = stateListArray.getJSONObject(i);
                                villageModelList.add(new VillageModel(villageObject.getString("id"),
                                        villageObject.getString("country_id"),
                                        villageObject.getString("state_id"),
                                        villageObject.getString("district_id"),
                                        villageObject.getString("mandal_id"),
                                        villageObject.getString("village"),
                                        villageObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onVillageSuccess(villageModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onVillageFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onVillageFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onVillageFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getPincodeApiCall(String villageId) {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getPincode(villageId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            List<PincodeModel> pincodeModelList = new ArrayList<>();
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject pincodeObject = stateListArray.getJSONObject(i);
                                pincodeModelList.add(new PincodeModel(pincodeObject.getString("id"),
                                        pincodeObject.getString("country_id"),
                                        pincodeObject.getString("state_id"),
                                        pincodeObject.getString("district_id"),
                                        pincodeObject.getString("mandal_id"),
                                        pincodeObject.getString("village_id"),
                                        pincodeObject.getString("pincode"),
                                        pincodeObject.getString("status")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onPincodeSuccess(pincodeModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onPincodeFailure(responseObject.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onPincodeFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onPincodeFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getLeadSource() {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getLeadSource();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObje = new JSONObject(responseStr);
                        if (responseObje.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray sourceArray = responseObje.getJSONArray("user_id");
                            List<SourceSpinnerModel> sourceSpinnerModelList = new ArrayList<>();
                            for (int i = 0; i < sourceArray.length(); i++) {
                                JSONObject sourceObj = sourceArray.getJSONObject(i);
                                sourceSpinnerModelList.add(new SourceSpinnerModel(
                                        sourceObj.getString("id"),
                                        sourceObj.getString("source")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onSourceSuccess(sourceSpinnerModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onSourceFailure(responseObje.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onSourceFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onSourceFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getIam() {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getIam();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObje = new JSONObject(responseStr);
                        if (responseObje.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray iamArray = responseObje.getJSONArray("user_id");
                            List<IamSpinnerModel> iamModelList = new ArrayList<>();
                            for (int i = 0; i < iamArray.length(); i++) {
                                JSONObject iamObj = iamArray.getJSONObject(i);
                                iamModelList.add(new IamSpinnerModel(
                                        iamObj.getString("id"),
                                        iamObj.getString("title")
                                ));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onIamSuccess(iamModelList);
                            }

                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onIamFailure(responseObje.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onIamFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onIamFailure(t.getLocalizedMessage());
                }
            }
        });
    }

    public void getStatus() {
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getLeadStatus();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseStr = new String(response.body().bytes());
                        JSONObject responseObje = new JSONObject(responseStr);
                        if (responseObje.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray statusArray = responseObje.getJSONArray("user_id");
                            List<StatusModel> statusModelList = new ArrayList<>();
                            for (int i = 0; i < statusArray.length(); i++) {
                                JSONObject statusObj = statusArray.getJSONObject(i);
                                statusModelList.add(new StatusModel(
                                        statusObj.getString("id"),
                                        statusObj.getString("title"),
                                        statusObj.getString("priority")));
                            }
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onStatusSuccess(statusModelList);
                            }
                        } else {
                            if (locationSpinnerListener != null) {
                                locationSpinnerListener.onStatusFailure(responseObje.getString("message"));
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        if (locationSpinnerListener != null) {
                            locationSpinnerListener.onStatusFailure(e.getLocalizedMessage());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                if (locationSpinnerListener != null) {
                    locationSpinnerListener.onStatusFailure(t.getLocalizedMessage());
                }
            }
        });
    }
}
