package com.leadapplication.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.leadapplication.app.Adapter.LogsDetailsAdapter;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.LogsListModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollwUpsViewActivity extends AppCompatActivity implements LocationSpinnersController.LocationSpinnerListener {

    private DatePickerDialog datePickerDialog;
    private String date, selectedStatus = "";
    private String leadId;
    private List<LogsListModel> logsModelList;
    private RecyclerView rv_logs;
    private int mHour;
    private int mMinute;
    private Context ctx = this;
    private TextView client_name, mobileNumber, email_client, category_type,
            categoryName, subCategoryName, city_lead, txtRemaks;
    private String remarksString = "", venueString = "", dateString = "", timeString = "", amountString = "";
    private List<StatusModel> statusModelList;
    private SharedPreferences sharedPreferences;
    private String agent_id, selectedStatusId = "";
    private ProgressDialog progressDialog;
    private ImageView img_back_view, img_update;
    private int CalendarHour, CalendarMinute;
    String format;
    TimePickerDialog timepickerdialog;
    Spinner status_spinner;
    private ProgressBar pbr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follw_ups_view);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        if (getIntent().hasExtra("lead_id")) {
            leadId = getIntent().getStringExtra("lead_id");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Updating Details...");
        castingViews();
        getFollwUpDetails();
        img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateAlertDialog();
            }
        });
        img_back_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getFollwUpDetails();
    }

    private void updateAlertDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.update_status_alert);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView img_wrong;
        EditText et_date, et_time, et_remarks, edit_venue, edit_amount;
        Button btn_add;
        LinearLayout layout_time;
        TextView tv_spinnerError;

        et_date = dialog.findViewById(R.id.date_value);
        et_time = dialog.findViewById(R.id.et_time_value);
        img_wrong = dialog.findViewById(R.id.img_wrong);
        btn_add = dialog.findViewById(R.id.btn_add);
        status_spinner = dialog.findViewById(R.id.status_spinner);
        et_remarks = dialog.findViewById(R.id.et_remarks);
        edit_venue = dialog.findViewById(R.id.edit_venue);
        edit_amount = dialog.findViewById(R.id.edit_amount);
        layout_time = dialog.findViewById(R.id.layout_time);
        tv_spinnerError = dialog.findViewById(R.id.tv_spinnerError);
        getStatusApiCall(dialog);
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                final Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(FollwUpsViewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        newCalendar.set(dayOfMonth, month, year);
                        date = dayOfMonth + "-" + (month + 1) + "-" + year;
                        et_date.setText(date);
                        dateString = et_date.getText().toString();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timepickerdialog = new TimePickerDialog(FollwUpsViewActivity.this,
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
        });
        status_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedStatus = status_spinner.getSelectedItem().toString();
                if (selectedStatus.equals("Meeting")) {
                    edit_venue.setVisibility(View.VISIBLE);
                    edit_amount.setVisibility(View.GONE);
                    layout_time.setVisibility(View.VISIBLE);
                } else if (selectedStatus.equals("Completed")) {
                    edit_amount.setVisibility(View.VISIBLE);
                    edit_venue.setVisibility(View.GONE);
                    layout_time.setVisibility(View.GONE);
                } else if (selectedStatus.equals("Canceled")) {
                    edit_amount.setVisibility(View.GONE);
                    edit_venue.setVisibility(View.GONE);
                    layout_time.setVisibility(View.GONE);
                } else if (selectedStatus.equals("Sale Lost") || selectedStatus.equals("XX") || selectedStatus.equals("YY")) {
                    edit_amount.setVisibility(View.GONE);
                    edit_venue.setVisibility(View.GONE);
                    layout_time.setVisibility(View.GONE);
                } else {
                    edit_amount.setVisibility(View.GONE);
                    edit_venue.setVisibility(View.GONE);
                    layout_time.setVisibility(View.VISIBLE);
                }
                if (position > 0) {
                    StatusModel model = statusModelList.get(position);
                    selectedStatusId = model.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        img_wrong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remarksString = et_remarks.getText().toString();
                timeString = et_time.getText().toString();
                dateString = et_date.getText().toString();
                if (selectedStatus.equals("Action Plan")) {
                    tv_spinnerError.setVisibility(View.VISIBLE);
                } else if (remarksString.isEmpty()) {
                    Toast.makeText(ctx, "Enter Remarks", Toast.LENGTH_SHORT).show();
                } else if (selectedStatus.equals("Meeting") || selectedStatus.equals("Customer Visit")) {
                    if (!validateDate() || !validateTime()) {
                        return;
                    } else {
                        tv_spinnerError.setVisibility(View.GONE);
                        remarksString = et_remarks.getText().toString();
                        venueString = edit_venue.getText().toString();
                        amountString = edit_amount.getText().toString();
                        timeString = et_time.getText().toString();
                        dateString = et_date.getText().toString();
                        editLeadApiCall(dialog);
                    }
                } else {
                    tv_spinnerError.setVisibility(View.GONE);
                    remarksString = et_remarks.getText().toString();
                    venueString = edit_venue.getText().toString();
                    amountString = edit_amount.getText().toString();
                    timeString = et_time.getText().toString();
                    dateString = et_date.getText().toString();
                    editLeadApiCall(dialog);
                }
            }
        });
        dialog.show();
    }

    public boolean validateDate() {
        if (dateString.isEmpty()) {
            Toast.makeText(this, "Enter Date", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateTime() {
        if (timeString.isEmpty()) {
            Toast.makeText(this, "Enter Time", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    private void getStatusApiCall(Dialog dialog) {
        LocationSpinnersController.getLocationSpinnersController().getStatus();
        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(this);
    }

    private void editLeadApiCall(Dialog dialog) {
        progressDialog.show();
        Map<String, String> editLeadMap = new HashMap<>();
        editLeadMap.put("lead_id", leadId);
        editLeadMap.put("agent_id", agent_id);
        editLeadMap.put("status", selectedStatus);
        editLeadMap.put("status_id", selectedStatusId);
        if (selectedStatus.equals("Completed") || selectedStatus.equals("Canceled") ||
                selectedStatus.equals("XX") || selectedStatus.equals("YY") || selectedStatus.equals("Sale Lost")) {
            editLeadMap.put("date", "");
            editLeadMap.put("time", "");
            editLeadMap.put("venue", "");
        }
        editLeadMap.put("date", dateString);
        editLeadMap.put("time", timeString);
        editLeadMap.put("remarks", remarksString);
        editLeadMap.put("venue", venueString);
        editLeadMap.put("amount", amountString);

        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.editLead(editLeadMap);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    progressDialog.cancel();
                    try {
                        String resposneString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(resposneString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            String message = responseObject.getString("message");
                            Toast.makeText(FollwUpsViewActivity.this, message, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            getFollwUpDetails();
                        } else {
                            if (responseObject.getString("status").equalsIgnoreCase("invalid")) {
                                String message = responseObject.getString("message");
                                Toast.makeText(FollwUpsViewActivity.this, message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        progressDialog.cancel();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                progressDialog.cancel();
            }
        });
    }

    private void castingViews() {
        rv_logs = findViewById(R.id.rv_logs);
        client_name = findViewById(R.id.client_name);
        mobileNumber = findViewById(R.id.mobileNumber);
        email_client = findViewById(R.id.email_client);
        category_type = findViewById(R.id.category_type);
        categoryName = findViewById(R.id.category_name);
        subCategoryName = findViewById(R.id.sub_category_name);
        city_lead = findViewById(R.id.lead_address);
        txtRemaks = findViewById(R.id.txt_remaks);
        img_update = findViewById(R.id.img_update);
        img_back_view = findViewById(R.id.img_back_view);
        pbr = findViewById(R.id.pbr);
    }

    private void setLogsRecyclerView() {
        rv_logs.setHasFixedSize(true);
        rv_logs.setLayoutManager(new LinearLayoutManager(this));
        LogsDetailsAdapter logsDetailsAdapter = new LogsDetailsAdapter(this, logsModelList);
        rv_logs.setAdapter(logsDetailsAdapter);
    }

    private void getFollwUpDetails() {
        pbr.setVisibility(View.VISIBLE);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getLeadDetails(leadId);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    pbr.setVisibility(View.GONE);
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONObject dataObject = responseObject.getJSONObject("data");
                            String id = dataObject.getString("id");
                            String lead_id = dataObject.getString("lead_id");
                            String user_id = dataObject.getString("user_id");
                            String agent_id = dataObject.getString("agent_id");
                            String lead_created_by = dataObject.getString("lead_created_by");
                            String country_id = dataObject.getString("country_id");
                            String state_id = dataObject.getString("state_id");
                            String district_id = dataObject.getString("district_id");
                            String mandal_id = dataObject.getString("mandal_id");
                            String village_id = dataObject.getString("village_id");
                            String pincode_id = dataObject.getString("pincode_id");
                            String service_type = dataObject.getString("service_type");
                            String category_id = dataObject.getString("category_id");
                            String sub_category_id = dataObject.getString("sub_category_id");
                            String status = dataObject.getString("status");
                            String date = dataObject.getString("date");
                            String time = dataObject.getString("time");
                            String venue = dataObject.getString("venue");
                            String remarks = dataObject.getString("remarks");
                            String created_at = dataObject.getString("created_at");
                            String user_name = dataObject.getString("user_name");
                            String mobile = dataObject.getString("mobile");
                            String email = dataObject.getString("email");
                            String country = dataObject.getString("country");
                            String state = dataObject.getString("state");
                            String district = dataObject.getString("district");
                            String mandal = dataObject.getString("mandal");
                            String village = dataObject.getString("village");
                            String pincode = dataObject.getString("pincode");
                            String category_name = dataObject.getString("category_name");
                            String sub_category_name = dataObject.getString("sub_category_name");

                            client_name.setText(user_name);
                            mobileNumber.setText(mobile);
                            email_client.setText(email);
                            category_type.setText(service_type);
                            categoryName.setText(category_name);
                            subCategoryName.setText(sub_category_name);
                            city_lead.setText(district + ", " + mandal + ", " + village + ", " + pincode);
                            txtRemaks.setText(remarks);

                            JSONArray logsArray = dataObject.getJSONArray("logs");
                            logsModelList = new ArrayList<>();
                            for (int i = 0; i < logsArray.length(); i++) {
                                JSONObject logsObject = logsArray.getJSONObject(i);
                                logsModelList.add(new LogsListModel(logsObject.getString("id"),
                                        logsObject.getString("lead_id"),
                                        logsObject.getString("status"),
                                        logsObject.getString("date"),
                                        logsObject.getString("time"),
                                        logsObject.getString("agent_id"),
                                        logsObject.getString("old_agent_id"),
                                        logsObject.getString("remarks"),
                                        logsObject.getString("created_at"),
                                        logsObject.getString("name"),
                                        logsObject.getString("old_agent_name"),
                                        logsObject.getString("title"),
                                        logsObject.getString("is_agent_changed")

                                ));
                            }
                            setLogsRecyclerView();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        pbr.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                pbr.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCountrySuccess(List<CountryModel> countryModelList) {

    }

    @Override
    public void onCountryFailure(String countryFailure) {

    }

    @Override
    public void onStateSuccess(List<StateModel> stateModelList) {

    }

    @Override
    public void onStatreFailure(String stateFailure) {

    }

    @Override
    public void onDistrictSuccess(List<DistrictModel> districtModelList) {

    }

    @Override
    public void onDistrictFailure(String districtFailure) {

    }

    @Override
    public void onCitySuccess(List<CityModel> cityModelList) {

    }

    @Override
    public void onCityFailure(String cityFailure) {

    }

    @Override
    public void onVillageSuccess(List<VillageModel> villageModelList) {

    }

    @Override
    public void onVillageFailure(String villageFailure) {

    }

    @Override
    public void onPincodeSuccess(List<PincodeModel> pincodeModelList) {

    }

    @Override
    public void onPincodeFailure(String picodeFailure) {

    }

    @Override
    public void onSourceSuccess(List<SourceSpinnerModel> sourceSpinnerModelList) {

    }

    @Override
    public void onSourceFailure(String sourceFailure) {

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
        this.statusModelList.add(0, new StatusModel("", "Action Plan", ""));
        ArrayAdapter<StatusModel> arrayAdapter = new ArrayAdapter<StatusModel>(FollwUpsViewActivity.this,
                R.layout.status_spinner_item, this.statusModelList) {
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
        arrayAdapter.setDropDownViewResource(R.layout.status_spinner_item);
        status_spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onStatusFailure(String statusFail) {
        Toast.makeText(this, statusFail, Toast.LENGTH_SHORT).show();
    }
}