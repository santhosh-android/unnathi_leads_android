package com.leadapplication.app.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.leadapplication.app.Controller.CustomerRegisterController;
import com.leadapplication.app.Controller.LocationSpinnersController;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.IamSpinnerModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.SourceSpinnerModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.StatusModel;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRegisterActivity extends AppCompatActivity implements CustomerRegisterController.CustomerRegisterListener, LocationSpinnersController.LocationSpinnerListener {

    private EditText et_surname, et_name, et_number, et_email, et_password, et_re_password, et_address;
    private String surnameString, nameString, numberString, emailString, passwordString, rePasswordString, addressString, userId;
    private Spinner spinner_country, spinner_state, spinner_district, spinner_mandal, spinner_village, spinner_pincode, spinner_iam;
    private List<CountryModel> countryModelList;
    private List<StateModel> stateModelList;
    private List<DistrictModel> districtModelList;
    private List<CityModel> mandalModelList;
    private List<VillageModel> villageModelList;
    private List<PincodeModel> pincodeModelList;
    private List<IamSpinnerModel> iamSpinnerModelList;
    private TextView tv_register, tv_pass_rule;
    private SharedPreferences sharedPreferences;
    private String agent_id, selectedCountry = "", selectedState = "", selectedDistrict = "", selectedMandal = "", selectedVillage = "", selectedPincode = "", selectedIam = "";
    private ImageView img_back_customer, img_information;
    private boolean isClick = true;
    private String userMb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_register);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        agent_id = sharedPreferences.getString("agent_id", "");
        if (getIntent().hasExtra("mobile")) {
            userMb = getIntent().getStringExtra("mobile");
        }


        castingViews();
        et_number.setText(userMb);
        getCountryApiCall();
        getIamApiCall();
        LocationSpinnersController.getLocationSpinnersController().setLocationSpinnerListener(this);
        onClick();
    }

    private void getIamApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getIam();
    }

    private void getCountryApiCall() {
        LocationSpinnersController.getLocationSpinnersController().CountryApiCall();
    }

    private void getStateApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getStatesApiCall(selectedCountry);
    }

    private void getDistrictApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getDistrictsApiCall(selectedState);
    }

    private void getMandalsApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getCityApiCall(selectedDistrict);
    }

    private void getVillagesApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getVillageApiCall(selectedMandal);
    }

    private void getPinCodeApiCall() {
        LocationSpinnersController.getLocationSpinnersController().getPincodeApiCall(selectedVillage);
    }


    private void onClick() {
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateSurname() || !validateName() || !validateNumber() || !validateEmail() || !validatePassword() || !validatereenterPassword() || !validateHouse() || !validateCountry() || !validateState() || !validateDistrict() ||
                        !validateMandal() || !validateVillage() || !validatePincode() || !validateIam()) {
                    return;
                } else {
                    Map<String, String> registerMap = new HashMap<>();
                    registerMap.put("first_name", surnameString);
                    registerMap.put("last_name", nameString);
                    registerMap.put("mobile", numberString);
                    registerMap.put("email", emailString);
                    registerMap.put("password", passwordString);
                    registerMap.put("country_id", selectedCountry);
                    registerMap.put("state_id", selectedState);
                    registerMap.put("district_id", selectedDistrict);
                    registerMap.put("mandal_id", selectedMandal);
                    registerMap.put("village_id", selectedVillage);
                    registerMap.put("pincode_id", selectedPincode);
                    registerMap.put("address", addressString);
                    registerMap.put("i_am_a", selectedIam);
                    CustomerRegisterController.getCustomerRegisterController().CustomerRegistrationApiCall(registerMap);
                    CustomerRegisterController.getCustomerRegisterController().setCustomerRegisterListener(UserRegisterActivity.this);
                }
            }
        });
        img_back_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    CountryModel model = countryModelList.get(position);
                    selectedCountry = model.getCountryId();
                    getStateApiCall();
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
                    getDistrictApiCall();
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
                    getMandalsApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_mandal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    CityModel model = mandalModelList.get(position);
                    selectedMandal = model.getId();
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
                    getPinCodeApiCall();
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
        spinner_iam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    IamSpinnerModel model = iamSpinnerModelList.get(position);
                    selectedIam = model.getiId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        img_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    isClick = false;
                    tv_pass_rule.setVisibility(View.VISIBLE);
                } else {
                    isClick = true;
                    tv_pass_rule.setVisibility(View.GONE);
                }

            }
        });
    }

    private boolean validateSurname() {
        if (surnameString.isEmpty()) {
            et_surname.setError("Field Must be Filled");
            return false;
        } else {
            et_surname.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        if (nameString.isEmpty()) {
            et_name.setError("Field Must be Filled");
            return false;
        } else {
            et_name.setError(null);
            return true;
        }
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

    public boolean validateMandal() {
        if (selectedMandal.isEmpty()) {
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

    private boolean validateIam() {
        if (selectedIam.isEmpty()) {
            Toast.makeText(this, "Select Your Profession", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateNumber() {
        if (numberString.isEmpty()) {
            et_number.setError("Field Must be Filled");
            return false;
        } else {
            et_number.setError(null);
            return true;
        }
    }

    private boolean validateEmailWithRegex(String Email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    private boolean validateEmail() {
        if (emailString.isEmpty()) {
            et_email.setError("Field Must be Filled");
            return false;
        } else if (!validateEmailWithRegex(emailString)) {
            et_email.setError("Enter Valid Email");
            return false;
        } else {
            et_email.setError(null);
        }
        return true;
    }

    private boolean validatePassword() {
        if (passwordString.isEmpty()) {
            et_password.setError("Field Must be Filled");
            return false;
        } else {
            et_password.setError(null);
            return true;
        }
    }

    private boolean validatereenterPassword() {
        if (rePasswordString.isEmpty()) {
            et_re_password.setError("Field Must be Filled");
            return false;
        } else {
            et_re_password.setError(null);
            return true;
        }
    }

    private boolean validateHouse() {
        if (addressString.isEmpty()) {
            et_address.setError("Enter House Number / Near by location");
            return false;
        } else {
            et_address.setError(null);
            return true;
        }
    }

    private void gettingVariables() {
        surnameString = et_surname.getText().toString().trim();
        nameString = et_name.getText().toString().trim();
        numberString = et_number.getText().toString().trim();
        emailString = et_email.getText().toString().trim();
        passwordString = et_password.getText().toString().trim();
        rePasswordString = et_re_password.getText().toString().trim();
        addressString = et_address.getText().toString().trim();
    }

    private void castingViews() {
        et_surname = findViewById(R.id.et_surname);
        et_name = findViewById(R.id.et_name);
        et_number = findViewById(R.id.et_number);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_re_password = findViewById(R.id.et_re_password);
        et_address = findViewById(R.id.et_address);
        tv_register = findViewById(R.id.tv_register);
        img_back_customer = findViewById(R.id.img_back_customer);
        spinner_country = findViewById(R.id.spinner_country);
        spinner_state = findViewById(R.id.spinner_state);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_mandal = findViewById(R.id.spinner_mandal);
        spinner_village = findViewById(R.id.spinner_village);
        spinner_pincode = findViewById(R.id.spinner_pincode);
        spinner_iam = findViewById(R.id.spinner_iam);
        img_information = findViewById(R.id.img_information);
        tv_pass_rule = findViewById(R.id.tv_pass_rule);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCustomerRegSuccess(JSONObject jsonObject) {
        String message = null;
        try {
            message = jsonObject.getString("message");
            userId = jsonObject.getString("user_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(UserRegisterActivity.this, MainActivity.class));
    }

    @Override
    public void onCustomerRegFailure(String failure) {
        Toast.makeText(this, failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCountrySuccess(List<CountryModel> countryModelList) {
        this.countryModelList = new ArrayList<>(countryModelList);
        this.countryModelList.add(0, new CountryModel("", "Select Country", ""));
        ArrayAdapter<CountryModel> arrayAdapter = new ArrayAdapter<CountryModel>(UserRegisterActivity.this,
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
        ArrayAdapter<StateModel> arrayAdapter = new ArrayAdapter<StateModel>(UserRegisterActivity.this,
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
        ArrayAdapter<DistrictModel> arrayAdapter = new ArrayAdapter<DistrictModel>(UserRegisterActivity.this,
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
        this.mandalModelList = new ArrayList<>(cityModelList);
        this.mandalModelList.add(0, new CityModel("", "", "", "", "Select Mandal", ""));
        ArrayAdapter<CityModel> arrayAdapter = new ArrayAdapter<CityModel>(UserRegisterActivity.this,
                android.R.layout.simple_spinner_item, this.mandalModelList) {
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
        spinner_mandal.setAdapter(arrayAdapter);
    }

    @Override
    public void onCityFailure(String cityFailure) {
        Toast.makeText(this, cityFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onVillageSuccess(List<VillageModel> villageModelList) {
        this.villageModelList = new ArrayList<>(villageModelList);
        this.villageModelList.add(0, new VillageModel("", "", "", "", "", "Select Village", ""));
        ArrayAdapter<VillageModel> arrayAdapter = new ArrayAdapter<VillageModel>(UserRegisterActivity.this,
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
        ArrayAdapter<PincodeModel> arrayAdapter = new ArrayAdapter<PincodeModel>(UserRegisterActivity.this,
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
    public void onIamSuccess(List<IamSpinnerModel> iamSpinnerModelList) {
        this.iamSpinnerModelList = new ArrayList<>(iamSpinnerModelList);
        this.iamSpinnerModelList.add(0, new IamSpinnerModel("", "Select Iam"));
        ArrayAdapter<IamSpinnerModel> arrayAdapter = new ArrayAdapter<IamSpinnerModel>(UserRegisterActivity.this,
                android.R.layout.simple_spinner_item, this.iamSpinnerModelList) {
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
        spinner_iam.setAdapter(arrayAdapter);
    }

    @Override
    public void onIamFailure(String iamFailure) {
        Toast.makeText(this, iamFailure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusSuccess(List<StatusModel> statusModelList) {

    }

    @Override
    public void onStatusFailure(String statusFail) {

    }


    @Override
    public void onSourceSuccess(List<SourceSpinnerModel> sourceSpinnerModelList) {

    }

    @Override
    public void onSourceFailure(String sourceFailure) {

    }


}