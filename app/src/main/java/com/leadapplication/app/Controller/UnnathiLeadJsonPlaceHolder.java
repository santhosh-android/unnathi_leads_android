package com.leadapplication.app.Controller;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UnnathiLeadJsonPlaceHolder {

    //String BASE_URL = "http://demoworks.in/php/unnathi/api/";
    String BASE_URL = "http://unnathiagro.com/api/";
    String IMAGE_URL = "http://demoworks.in/php/unnathi/uploads/";

    @GET("countries")
    Call<ResponseBody> getCountries();

    @GET("service_categories")
    Call<ResponseBody> getServices();

    @GET("product_categories")
    Call<ResponseBody> getProducts();

    @GET("banners")
    Call<ResponseBody> getBanners();

    @GET("lead_source")
    Call<ResponseBody> getLeadSource();

    @GET("i_am_a_list")
    Call<ResponseBody> getIam();

    @GET("status_list")
    Call<ResponseBody> getLeadStatus();

    @FormUrlEncoded
    @POST("states")
    Call<ResponseBody> getStates(@Field("country_id") String states);

    @FormUrlEncoded
    @POST("districts")
    Call<ResponseBody> getDistricts(@Field("state_id") String districts);

    @FormUrlEncoded
    @POST("mandals")
    Call<ResponseBody> getMandal(@Field("district_id") String mandal);

    @FormUrlEncoded
    @POST("villages")
    Call<ResponseBody> getVillages(@Field("mandal_id") String village);

    @FormUrlEncoded
    @POST("pincode")
    Call<ResponseBody> getPincode(@Field("village_id") String pincode);

    @FormUrlEncoded
    @POST("leads_count")
    Call<ResponseBody> getCount(@FieldMap Map<String, String> count);

    @Multipart
    @POST("agent_register")
    Call<ResponseBody> userRegister(@Part("name") RequestBody name,
                                    @Part("email") RequestBody email,
                                    @Part("mobile") RequestBody mobile,
                                    @Part("country") RequestBody country,
                                    @Part("state") RequestBody state,
                                    @Part("district") RequestBody district,
                                    @Part("mandal") RequestBody city,
                                    @Part("village") RequestBody village,
                                    @Part("address") RequestBody address,
                                    @Part("pincode") RequestBody pincode,
                                    @Part("password") RequestBody password,
                                    @Part MultipartBody.Part profile,
                                    @Part MultipartBody.Part aadhar);

    @FormUrlEncoded
    @POST("agent_otp_verify")
    Call<ResponseBody> otpVerify(@FieldMap Map<String, String> otp);

    @FormUrlEncoded
    @POST("agent_login")
    Call<ResponseBody> loginApi(@FieldMap Map<String, String> login);

    @FormUrlEncoded
    @POST("get_customer_details")
    Call<ResponseBody> getCustomerDetails(@Field("username") String detaisl);

    @FormUrlEncoded
    @POST("link_customer_with_agent")
    Call<ResponseBody> assign(@FieldMap Map<String, String> assigning);

    @FormUrlEncoded
    @POST("user_register")
    Call<ResponseBody> customerRegister(@FieldMap Map<String, String> registerCustomer);

    @FormUrlEncoded
    @POST("service_sub_categories")
    Call<ResponseBody> getSubServices(@Field("service_category_id") String subServices);

    @FormUrlEncoded
    @POST("product_sub_categories")
    Call<ResponseBody> getSubProducts(@Field("product_category_id") String subProducts);

    @FormUrlEncoded
    @POST("add_lead")
    Call<ResponseBody> addLead(@FieldMap Map<String, String> leadAdd);

    @FormUrlEncoded
    @POST("today_schedules_leads_list")
    Call<ResponseBody> getTodaySchedule(@FieldMap Map<String, String> todayList);

    @FormUrlEncoded
    @POST("schedules_leads_list")
    Call<ResponseBody> getScheduleList(@FieldMap Map<String, String> scheduleLIst);

    @FormUrlEncoded
    @POST("lead_details")
    Call<ResponseBody> getLeadDetails(@Field("lead_id") String leadId);

    @FormUrlEncoded
    @POST("update_lead")
    Call<ResponseBody> editLead(@FieldMap Map<String, String> updateLead);

    @FormUrlEncoded
    @POST("completed_and_dead_leads")
    Call<ResponseBody> getCompletedleads(@Field("agent_id") String completedCancelLeads);

    @FormUrlEncoded
    @POST("all_leads")
    Call<ResponseBody> getAllLeads(@FieldMap Map<String, String> allLeads);

    @FormUrlEncoded
    @POST("check_agent_status")
    Call<ResponseBody> getStatus(@Field("agent_id") String status);

    @FormUrlEncoded
    @POST("toset_leads")
    Call<ResponseBody> getToset(@FieldMap Map<String, String> toSetLeads);

    @FormUrlEncoded
    @POST("agent_change_password")
    Call<ResponseBody> passwordChange(@FieldMap Map<String, String> changePassword);

}
