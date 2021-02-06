package com.leadapplication.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.leadapplication.app.Controller.AddLeadController;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.ProductCategoryModel;
import com.leadapplication.app.Model.ServicesCategoriesModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.SubProductModel;
import com.leadapplication.app.Model.SubServicesModel;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddNewLeadActivity extends AppCompatActivity implements LocationSpinnersController.LocationSpinnerListener, AddLeadController.AddLeadListener {

    private Button btn_create;
    private ImageView img_back;
    private LinearLayout layout_venue;
    private EditText edit_name_value, et_email_value, et_number_value,
            et_desc_value, et_venue_value, et_address_value, country_name, state_name, district_name, et_number_alter_value, et_iam;
    private String nameString, email_string, numberString, descString, venueString, addressString, alterNumber;
    private Spinner category_spinner, product_spinner, sub_category_spinner,
            sub_product_spinner, spinner_country, spinner_state, select_source_spinner,
            spinner_district, spinner_city, spinner_status, spinner_village, spinner_pincode;
    private String str = "";
    private String name, email, mobile, customerId, service_id = "",
            product_id = "", sub_service_id = "", sub_product_id = "", countryName, stateName, districtName, country_id, state_id, district_id;
    private List<ServicesCategoriesModel> serviceModel;
    private List<ProductCategoryModel> productModel;
    private List<SubServicesModel> subServicesModelList;
    private List<SubProductModel> subProductModelList;
    private EditText et_date, et_time;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private int mHour;
    private int mMinute;
    private Context ctx = this;
    private List<CountryModel> countryModelList;
    private List<StateModel> stateModelList;
    private List<DistrictModel> districtModelList;
    private List<CityModel> cityModelList;
    private List<VillageModel> villageModelList;
    private List<PincodeModel> pincodeModelList;
    private List<StatusModel> statusModelList;
    private List<SourceSpinnerModel> sourceSpinnerModelList;
    private String selectedCountry = "", selectedState = "", selectedDistrict = "", selectedCity = "", selectedStatusId="",selectedStatus = "", agent_id, selectedVillage = "", selectedPincode = "", selectedSource = "";
    private SharedPreferences sharedPreferences;
    private RadioGroup radio_group_type;
    private RadioButton radio_services, radio_product, radioButton;
    private LinearLayout layout_category, layout_category_sub, layout_product, layout_sub_product;
    private ProgressDialog progressDialog;

    private int CalendarHour, CalendarMinute;
    String format;
    TimePickerDialog timepickerdialog;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_lead);

        castingViews();
        getSerivcesCategoriesApiCall();
        getProductsApiCall();
        setCalender();
        LocationSpinnersController.getLocationSpinnersController().getLeadSource();
        LocationSpinnersController.getLocationSpinnersController().getStatus();
        //getCountryApiCall();

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);

        agent_id = sharedPreferences.getString("agent_id", "");
        country_id = sharedPreferences.getString("country_id", "");
        state_id = sharedPreferences.getString("state_id", "");
        district_id = sharedPreferences.getString("district_id", "");

        getCitiesApiCall();

        countryName = sharedPreferences.getString("country", "");
        stateName = sharedPreferences.getString("state", "");
        districtName = sharedPreferences.getString("district", "");

        country_name.setText(countryName);
        state_name.setText(stateName);
        district_name.setText(districtName);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait...");

        if (getIntent().hasExtra("cName")) {
            name = getIntent().getStringExtra("cName");
            email = getIntent().getStringExtra("cEmail");
            mobile = getIntent().getStringExtra("cMobile");
            customerId = getIntent().getStringExtra("cUserId");
        }

        setText();

        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(this);
        calendar = Calendar.getInstance();
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);

        radio_group_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                str = String.valueOf(radioButton.getText());
                if (str.equalsIgnoreCase("Service Categories")) {
                    Toast.makeText(ctx, "Service", Toast.LENGTH_SHORT).show();
                    layout_category.setVisibility(View.VISIBLE);
                    layout_category_sub.setVisibility(View.VISIBLE);
                    layout_product.setVisibility(View.GONE);
                    layout_sub_product.setVisibility(View.GONE);
                } else if (str.equalsIgnoreCase("Product Categories")) {
                    Toast.makeText(ctx, "Product", Toast.LENGTH_SHORT).show();
                    layout_product.setVisibility(View.VISIBLE);
                    layout_sub_product.setVisibility(View.VISIBLE);
                    layout_category.setVisibility(View.GONE);
                    layout_category_sub.setVisibility(View.GONE);
                }
            }
        });
        onClickMethod();
    }

    private void getCountryApiCall() {
        LocationSpinnersController.getLocationSpinnersController().CountryApiCall();
    }

    private void getStatesListApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getStatesApiCall(selectedCountry);
    }

    private void getDistrictsApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getDistrictsApiCall(selectedState);
    }

    private void getCitiesApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getCityApiCall(district_id);
    }

    private void getVillagesApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getVillageApiCall(selectedCity);
    }

    private void getPincodeApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getPincodeApiCall(selectedVillage);
    }


    private void setCalender() {
        et_date.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                datePickerDialog = new DatePickerDialog(AddNewLeadActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int dayOfMonth) {
                        et_date.setText(dayOfMonth + "-" + (selectedMonth + 1) + "-" + selectedYear);
                    }

                }, day, month, year);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    private void show_Timepicker() {
        timepickerdialog = new TimePickerDialog(AddNewLeadActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        et_time.setText(hourOfDay + ":" + minute + format);
                    }
                }, CalendarHour, CalendarMinute, false);
        timepickerdialog.show();
    }

    private void onClickMethod() {
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateName() || !validateEmail() || !validateNumber() || !validateDesc() ||
                        !validateMandal() || !validateVillage() || !validatePincode() || !validateStatus()) {
                    return;
                } else {
                    addLeadApiCall();
                }
            }
        });
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ServicesCategoriesModel servicesCategoriesModel = serviceModel.get(position - 1);
                    service_id = servicesCategoriesModel.getId();
                    getSubServicesCategory();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sub_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SubServicesModel model = subServicesModelList.get(position - 1);
                    sub_service_id = model.getSubServiceId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    ProductCategoryModel productCategoryModel = productModel.get(position - 1);
                    product_id = productCategoryModel.getProductId();
                    getSubProducts();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sub_product_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SubProductModel model = subProductModelList.get(position - 1);
                    sub_product_id = model.getSubProductId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show_Timepicker();
            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    CountryModel model = countryModelList.get(position);
                    selectedCountry = model.getCountryId();
                    //getStatesListApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StateModel model = stateModelList.get(position);
                    selectedState = model.getStateId();
                    //getDistrictsApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    DistrictModel model = districtModelList.get(position);
                    selectedDistrict = model.getDistId();
                    getCitiesApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    CityModel model = cityModelList.get(position);
                    selectedCity = model.getId();
                    getVillagesApiCall();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    VillageModel model = villageModelList.get(position);
                    selectedVillage = model.getIdVillage();
                    getPincodeApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_source_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    SourceSpinnerModel sourceSpinnerModel = sourceSpinnerModelList.get(position);
                    selectedSource = sourceSpinnerModel.getSourceId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_pincode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    PincodeModel model = pincodeModelList.get(position);
                    selectedPincode = model.getPincodeId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StatusModel model = statusModelList.get(position);
                    selectedStatusId = model.getId();
                }
                selectedStatus = spinner_status.getSelectedItem().toString();
                if (selectedStatus.equalsIgnoreCase("Meeting")) {
                    layout_venue.setVisibility(View.VISIBLE);
                } else {
                    layout_venue.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void addLeadApiCall() {
        radioButton = findViewById(radio_group_type.getCheckedRadioButtonId());
        str = String.valueOf(radioButton.getText());
        progressDialog.show();
        Map<String, String> addLeadMap = new HashMap<>();
        addLeadMap.put("user_id", customerId);
        addLeadMap.put("agent_id", agent_id);
        addLeadMap.put("country_id", country_id);
        addLeadMap.put("state_id", state_id);
        addLeadMap.put("district_id", district_id);
        addLeadMap.put("mandal_id", selectedCity);
        addLeadMap.put("village_id", selectedVillage);
        addLeadMap.put("address", addressString);
        addLeadMap.put("pincode_id", selectedPincode);

        if (str.equals("Service Categories")) {
            addLeadMap.put("service_type", "service");
        } else {
            addLeadMap.put("service_type", "product");
        }

        if (str.equals("Service Categories")) {
            addLeadMap.put("category_id", service_id);
            addLeadMap.put("sub_category_id", sub_service_id);
        } else {
            addLeadMap.put("category_id", product_id);
            addLeadMap.put("sub_category_id", sub_product_id);
        }
        addLeadMap.put("status", selectedStatus);
        addLeadMap.put("status_id", selectedStatusId);
        addLeadMap.put("date", et_date.getText().toString());
        addLeadMap.put("time", et_time.getText().toString());
        addLeadMap.put("venue", venueString);
        addLeadMap.put("remarks", descString);
        addLeadMap.put("lead_source", selectedSource);
        addLeadMap.put("alternate_number", alterNumber);
        AddLeadController.getAddLeadController().AddLeadApiCall(addLeadMap);
        AddLeadController.getAddLeadController().setAddLeadListener(this);
    }

    private void getSerivcesCategoriesApiCall() {
        final ArrayList<String> serviceArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getServices();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray categoryArray = responseObject.getJSONArray("list");
                            serviceModel = new ArrayList<>();
                            serviceArrayList.add(0, "Select Service Category");
                            for (int i = 0; i < categoryArray.length(); i++) {
                                JSONObject listObject = categoryArray.getJSONObject(i);
                                String serviceId = listObject.getString("id");
                                String serviceName = listObject.getString("service_category");
                                String serviceImage = listObject.getString("image");
                                String serviceStatus = listObject.getString("status");
                                serviceModel.add(new ServicesCategoriesModel(serviceId, serviceName, serviceImage, serviceStatus));
                                serviceArrayList.add(serviceName);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddNewLeadActivity.this, android.R.layout.simple_spinner_item, serviceArrayList) {
                                @Override
                                public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                                    return position != 0;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        tv.setTextColor(Color.BLACK);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            category_spinner.setSelection(arrayAdapter.getPosition("Select Service Category"));
                            category_spinner.setAdapter(arrayAdapter);
                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(AddNewLeadActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void getSubServicesCategory() {
        final ArrayList<String> subServicesArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getSubServices(service_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            subServicesModelList = new ArrayList<>();
                            subServicesArrayList.add(0, "Select Sub Category Product");
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                String subServiceId = listObject.getString("id");
                                String service_sub_category = listObject.getString("service_sub_category");
                                String service_category_id = listObject.getString("service_category_id");
                                String image = listObject.getString("image");
                                String status = listObject.getString("status");
                                String service_category = listObject.getString("service_category");
                                subServicesModelList.add(new SubServicesModel(subServiceId, service_sub_category, service_category_id, image, status, service_category));
                                subServicesArrayList.add(service_sub_category);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddNewLeadActivity.this, android.R.layout.simple_spinner_item, subServicesArrayList) {
                                @Override
                                public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                                    return position != 0;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        tv.setTextColor(Color.BLACK);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            sub_category_spinner.setSelection(arrayAdapter.getPosition("Select Sub Category Product"));
                            sub_category_spinner.setAdapter(arrayAdapter);

                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(AddNewLeadActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void getProductsApiCall() {
        final ArrayList<String> productArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getProducts();
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray categoryArray = responseObject.getJSONArray("list");
                            productModel = new ArrayList<>();
                            productArrayList.add(0, "Select Product Category");
                            for (int i = 0; i < categoryArray.length(); i++) {
                                JSONObject listObject = categoryArray.getJSONObject(i);
                                String productId = listObject.getString("id");
                                String productName = listObject.getString("product_category");
                                String productImage = listObject.getString("image");
                                String productStatus = listObject.getString("status");
                                productModel.add(new ProductCategoryModel(productId, productName, productImage, productStatus));
                                productArrayList.add(productName);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddNewLeadActivity.this, android.R.layout.simple_spinner_item, productArrayList) {
                                @Override
                                public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                                    return position != 0;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        tv.setTextColor(Color.BLACK);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            product_spinner.setSelection(arrayAdapter.getPosition("Select Product Category"));
                            product_spinner.setAdapter(arrayAdapter);
                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(AddNewLeadActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    private void getSubProducts() {
        final ArrayList<String> subProductArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getSubProducts(product_id);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray listArray = responseObject.getJSONArray("list");
                            subProductModelList = new ArrayList<>();
                            subProductArrayList.add(0, "Select Sub Product Category");
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                String subproductId = listObject.getString("id");
                                String product_sub_category = listObject.getString("product_sub_category");
                                String product_category_id = listObject.getString("product_category_id");
                                String image = listObject.getString("image");
                                String status = listObject.getString("status");
                                String product_category = listObject.getString("product_category");
                                subProductModelList.add(new SubProductModel(subproductId, product_sub_category, product_category_id, image, status, product_category));
                                subProductArrayList.add(product_sub_category);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddNewLeadActivity.this, android.R.layout.simple_spinner_item, subProductArrayList) {
                                @Override
                                public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                                    return position != 0;
                                }

                                @Override
                                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                                    View view = super.getDropDownView(position, convertView, parent);
                                    TextView tv = (TextView) view;
                                    if (position == 0) {
                                        tv.setTextColor(Color.BLACK);
                                    } else {
                                        tv.setTextColor(Color.BLACK);
                                    }
                                    return view;
                                }
                            };
                            arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            sub_product_spinner.setSelection(arrayAdapter.getPosition("Select Sub Product Category"));
                            sub_product_spinner.setAdapter(arrayAdapter);
                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(AddNewLeadActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void setText() {
        edit_name_value.setText(name);
        et_email_value.setText(email);
        et_number_value.setText(mobile);
    }

    private void gettingVariables() {
        nameString = edit_name_value.getText().toString().trim();
        email_string = et_email_value.getText().toString().trim();
        numberString = et_number_value.getText().toString().trim();
        descString = et_desc_value.getText().toString().trim();
        venueString = et_venue_value.getText().toString().trim();
        addressString = et_address_value.getText().toString().trim();
        alterNumber = et_number_alter_value.getText().toString().trim();
    }

    private void castingViews() {
        btn_create = findViewById(R.id.btn_create);
        img_back = findViewById(R.id.img_back);
        edit_name_value = findViewById(R.id.edit_name_value);
        et_email_value = findViewById(R.id.et_email_value);
        et_number_value = findViewById(R.id.et_number_value);
        et_desc_value = findViewById(R.id.et_desc_value);
        et_venue_value = findViewById(R.id.et_venue_value);
        et_address_value = findViewById(R.id.et_address_value);
        category_spinner = findViewById(R.id.category_spinner);
        product_spinner = findViewById(R.id.product_spinner);
        sub_category_spinner = findViewById(R.id.sub_category_spinner);
        sub_product_spinner = findViewById(R.id.sub_product_spinner);
        spinner_country = findViewById(R.id.spinner_country);
        spinner_state = findViewById(R.id.spinner_state);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_city = findViewById(R.id.spinner_city);
        spinner_village = findViewById(R.id.spinner_village);
        spinner_pincode = findViewById(R.id.spinner_pincode);
        spinner_status = findViewById(R.id.spinner_status);
        select_source_spinner = findViewById(R.id.select_source_spinner);
        et_date = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time);
        radio_group_type = findViewById(R.id.radio_group_type);
        radio_services = findViewById(R.id.radio_services);
        radio_product = findViewById(R.id.radio_product);
        layout_category = findViewById(R.id.layout_category);
        layout_category_sub = findViewById(R.id.layout_category_sub);
        layout_product = findViewById(R.id.layout_product);
        layout_sub_product = findViewById(R.id.layout_sub_product);
        country_name = findViewById(R.id.country_name);
        state_name = findViewById(R.id.state_name);
        district_name = findViewById(R.id.district_name);
        et_number_alter_value = findViewById(R.id.et_number_alter_value);
        layout_venue = findViewById(R.id.layout_venue);
    }

    public boolean validateCountry() {
        if (selectedCountry.isEmpty()) {
            Toast.makeText(this, "Select Country", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateState() {
        if (selectedState.isEmpty()) {
            Toast.makeText(this, "Select State", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateDistrict() {
        if (selectedDistrict.isEmpty()) {
            Toast.makeText(this, "Select District", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateStatus() {
        if (selectedStatus.isEmpty()) {
            Toast.makeText(this, "Select Status", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateMandal() {
        if (selectedCity.isEmpty()) {
            Toast.makeText(this, "Select Mandal", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateVillage() {
        if (selectedVillage.isEmpty()) {
            Toast.makeText(this, "Select Village", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validatePincode() {
        if (selectedPincode.isEmpty()) {
            Toast.makeText(this, "Select Pincode", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateName() {
        if (nameString.isEmpty()) {
            edit_name_value.setError("Field Must be Filled");
            return false;
        } else {
            edit_name_value.setError(null);
        }
        return true;
    }

    private boolean validateEmail() {
        if (email_string.isEmpty()) {
            et_email_value.setError("Field Must be Filled");
            return false;
        } else {
            et_email_value.setError(null);
        }
        return true;
    }

    private boolean validateNumber() {
        if (numberString.isEmpty()) {
            et_number_value.setError("Field Must be Filled");
            return false;
        } else {
            et_number_value.setError(null);
        }
        return true;
    }

    private boolean validateDesc() {
        if (descString.isEmpty()) {
            et_desc_value.setError("Field Must be Filled");
            return false;
        } else {
            et_desc_value.setError(null);
        }
        return true;
    }

    @Override
    public void onCountrySuccess(List<CountryModel> countryModelList) {
        this.countryModelList = new ArrayList<>(countryModelList);
        this.countryModelList.add(0, new CountryModel("", "Select Country", ""));
        ArrayAdapter<CountryModel> arrayAdapter = new ArrayAdapter<CountryModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.countryModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_country.setAdapter(arrayAdapter);
    }

    @Override
    public void onCountryFailure(String countryFailure) {
        Toast.makeText(this, countryFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStateSuccess(List<StateModel> stateModelList) {
        this.stateModelList = new ArrayList<>(stateModelList);
        this.stateModelList.add(0, new StateModel("", "", "Select State", ""));
        ArrayAdapter<StateModel> arrayAdapter = new ArrayAdapter<StateModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.stateModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_state.setAdapter(arrayAdapter);

    }

    @Override
    public void onStatreFailure(String stateFailure) {
        Toast.makeText(this, stateFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDistrictSuccess(List<DistrictModel> districtModelList) {
        this.districtModelList = new ArrayList<>(districtModelList);
        this.districtModelList.add(0, new DistrictModel("", "", "", "Select District", ""));
        ArrayAdapter<DistrictModel> arrayAdapter = new ArrayAdapter<DistrictModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.districtModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_district.setAdapter(arrayAdapter);

    }

    @Override
    public void onDistrictFailure(String districtFailure) {
        Toast.makeText(this, districtFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCitySuccess(List<CityModel> cityModelList) {
        this.cityModelList = new ArrayList<>(cityModelList);
        this.cityModelList.add(0, new CityModel("", "", "", "", "Select Mandal", ""));
        ArrayAdapter<CityModel> arrayAdapter = new ArrayAdapter<CityModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.cityModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_city.setAdapter(arrayAdapter);
    }

    @Override
    public void onCityFailure(String cityFailure) {
        Toast.makeText(this, cityFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVillageSuccess(List<VillageModel> villageModelList) {
        this.villageModelList = new ArrayList<>(villageModelList);
        this.villageModelList.add(0, new VillageModel("", "", "", "", "", "Select Village", ""));
        ArrayAdapter<VillageModel> arrayAdapter = new ArrayAdapter<VillageModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.villageModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_village.setAdapter(arrayAdapter);
    }

    @Override
    public void onVillageFailure(String villageFailure) {
        Toast.makeText(this, villageFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPincodeSuccess(List<PincodeModel> pincodeModelList) {
        this.pincodeModelList = new ArrayList<>(pincodeModelList);
        this.pincodeModelList.add(0, new PincodeModel("", "", "", "", "", "", "Select Pincode", ""));
        ArrayAdapter<PincodeModel> arrayAdapter = new ArrayAdapter<PincodeModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.pincodeModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_pincode.setAdapter(arrayAdapter);
    }

    @Override
    public void onPincodeFailure(String picodeFailure) {
        Toast.makeText(this, picodeFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSourceSuccess(List<SourceSpinnerModel> sourceSpinnerModelList) {
        this.sourceSpinnerModelList = new ArrayList<>(sourceSpinnerModelList);
        this.sourceSpinnerModelList.add(0, new SourceSpinnerModel("", "Select Lead Source"));
        ArrayAdapter<SourceSpinnerModel> arrayAdapter = new ArrayAdapter<SourceSpinnerModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.sourceSpinnerModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        select_source_spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onSourceFailure(String sourceFailure) {
        Toast.makeText(this, sourceFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIamSuccess(List<IamSpinnerModel> iamSpinnerModelList) {

    }

    @Override
    public void onIamFailure(String iamFailure) {

    }

    @Override
    public void onStatusSuccess(List<StatusModel> statusModelList) {
        this.statusModelList = new ArrayList<>(statusModelList);
        this.statusModelList.add(0, new StatusModel("", "Select Status", ""));
        ArrayAdapter<StatusModel> arrayAdapter = new ArrayAdapter<StatusModel>(AddNewLeadActivity.this,
                android.R.layout.simple_spinner_item, this.statusModelList) {
            @Override
            public boolean isEnabled(int position) {
// Disable the first item from Spinner
// First item will be use for hint
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(Color.BLACK);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_status.setAdapter(arrayAdapter);
    }

    @Override
    public void onStatusFailure(String statusFail) {
        Toast.makeText(this, statusFail, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAddLeadSuccess(JSONObject addLeadObject) {
        progressDialog.hide();
        try {
            String message = addLeadObject.getString("message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent leadIntent = new Intent(AddNewLeadActivity.this, ManageLeadsActivity.class);
        leadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        leadIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        leadIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(leadIntent);
        finish();
    }

    @Override
    public void onAddLeadFailure(String failureMsg) {
        progressDialog.hide();
        Toast.makeText(this, failureMsg, Toast.LENGTH_SHORT).show();
    }
}