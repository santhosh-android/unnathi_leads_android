package com.leadapplication.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leadapplication.app.BuildConfig;
import com.leadapplication.app.Controller.RetrofitInstance;
import com.leadapplication.app.Controller.UnnathiLeadJsonPlaceHolder;
import com.leadapplication.app.Model.CityModel;
import com.leadapplication.app.Model.CountryModel;
import com.leadapplication.app.Model.DistrictModel;
import com.leadapplication.app.Model.PincodeModel;
import com.leadapplication.app.Model.StateModel;
import com.leadapplication.app.Model.VillageModel;
import com.leadapplication.app.Utils.ImageUtils;
import com.leadapplication.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leadapplication.app.Utils.Constants.OPEN_ADD_CAMERA_CODE;
import static com.leadapplication.app.Utils.Constants.OPEN_ADD_GALLERY_CODE;
import static com.leadapplication.app.Utils.Constants.REQUEST_CAMERA_PERMISSION_CODE;
import static com.leadapplication.app.Utils.Constants.REQUEST_GALLERY_PERMISSION_CODE;

public class RegisterActivity extends AppCompatActivity {
    private ImageView profileChangeImageBtn, profileImageView, img_aadhar, img_back_reg;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private EditText name_edit, email_edit, number_edit, address_edit, aadhar_edit, password_edit;
    private String name, emailString, numberString, addressString, pincodeString, passwordString;
    private String phototype = "";
    private String imageFilePath, selectedCountry = "", selectedState = "", selectedDistrict = "",
            selectedCity = "", selectedVillage = "", selectedPincode = "";
    private Button btn_register;
    private Spinner spinner_country, spinner_state, spinner_city, spinner_district, spinner_village, spinner_pincode;
    private List<CountryModel> countryModel;
    private List<StateModel> stateModelList;
    private List<DistrictModel> districtModelList;
    private List<CityModel> cityModelList;
    private List<VillageModel> villageModelList;
    private List<PincodeModel> pincodeMandalList;
    private String aadhar = "", profile = "", agent_id, country_id, country, state_id, state, district_id, district;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering..Please Wait...");
        progressDialog.setCancelable(false);

        castingViews();
        getCountries();
        onClick();
    }

    private void gettingVariables() {
        name = name_edit.getText().toString().trim();
        emailString = email_edit.getText().toString();
        numberString = number_edit.getText().toString();
        addressString = address_edit.getText().toString();
        passwordString = password_edit.getText().toString();
    }

    private void castingViews() {
        profileChangeImageBtn = findViewById(R.id.profileChangeImageBtn);
        profileImageView = findViewById(R.id.profileImageView);
        email_edit = findViewById(R.id.email_edit);
        number_edit = findViewById(R.id.number_edit);
        address_edit = findViewById(R.id.address_edit);
        password_edit = findViewById(R.id.password_edit);
        aadhar_edit = findViewById(R.id.aadhar_edit);
        img_aadhar = findViewById(R.id.img_aadhar);
        btn_register = findViewById(R.id.btn_register);
        img_back_reg = findViewById(R.id.img_back_reg);
        name_edit = findViewById(R.id.name_edit);
        spinner_country = findViewById(R.id.spinner_country);
        spinner_state = findViewById(R.id.spinner_state);
        spinner_city = findViewById(R.id.spinner_city);
        spinner_district = findViewById(R.id.spinner_district);
        spinner_village = findViewById(R.id.spinner_village);
        spinner_pincode = findViewById(R.id.spinner_pincode);
    }

    private void onClick() {
        profileChangeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phototype = "profile";
                alertDialog();
            }
        });
        aadhar_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phototype = "aadhar";
                alertDialog();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gettingVariables();
                if (!validateName() || !validatePassword() || !validateEmail() ||
                        !validateaddress() || !validateMobile() || !validateaadhar()
                        || !validateProfile() || !validateCountry() || !validateState() || !validateDistrict()
                        || !validateMandal() || !validateVillage() || !validatePincode()) {
                    return;
                } else {
                    registerApiCall();
                }
            }
        });
        img_back_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    CountryModel model = countryModel.get(position - 1);
                    selectedCountry = model.getCountryId();
                    getStatesApiCall();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //   Toast.makeText(RegisterActivity.this, "Select Country", Toast.LENGTH_SHORT).show();
            }
        });
        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    StateModel stateModel = stateModelList.get(position - 1);
                    selectedState = stateModel.getStateId();
                    getDistrictsApiCall();
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
                    DistrictModel districtModel = districtModelList.get(position - 1);
                    selectedDistrict = districtModel.getDistId();
                    getMandalApiCall();
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
                    CityModel cityModel = cityModelList.get(position - 1);
                    selectedCity = cityModel.getId();
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
                    VillageModel villageModel = villageModelList.get(position - 1);
                    selectedVillage = villageModel.getIdVillage();
                    getPincode();
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
                    PincodeModel pincodeModel = pincodeMandalList.get(position - 1);
                    selectedPincode = pincodeModel.getPincodeId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

    public boolean validateName() {
        if (name.isEmpty()) {
            name_edit.setError("Enter Name");
            return false;
        } else {
            name_edit.setError(null);
            return true;
        }
    }

    private boolean validateEmailWithRegex(String Email) {
        String EMAIL_PATTERN = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    public boolean validatePasswordPattren(String password) {
        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean validatePassword() {
        if (passwordString.isEmpty()) {
            password_edit.setError("Enter Password");
            return false;
        } else if (!validatePasswordPattren(passwordString)) {
            password_edit.setError("Password Should contain Upper Case Character,Lower Case Character,Special Character and Number");
            return false;
        } else {
            password_edit.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        if (emailString.isEmpty()) {
            email_edit.setError("Field Must be Filled");
            return false;
        } else if (!validateEmailWithRegex(emailString)) {
            email_edit.setError("Enter Valid Email");
            return false;
        } else {
            email_edit.setError(null);
            return true;
        }
    }

    public boolean validateMobile() {
        if (numberString.isEmpty()) {
            number_edit.setError("Enter Mobile Number");
            return false;
        } else {
            number_edit.setError(null);
            return true;
        }
    }

    public boolean validateaddress() {
        if (addressString.isEmpty()) {
            address_edit.setError("Enter Address");
            return false;
        } else {
            address_edit.setError(null);
            return true;
        }
    }

   /* public boolean validatePincode() {
        if (pincodeString.isEmpty()) {
            pincode_edit.setError("Enter Area Pincode");
            return false;
        } else {
            pincode_edit.setError(null);
            return true;
        }
    }*/

    public boolean validateaadhar() {
        if (aadhar.isEmpty()) {
            Toast.makeText(this, "Upload Aadhar", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    public boolean validateProfile() {
        if (profile.isEmpty()) {
            Toast.makeText(this, "Upload Profile", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void alertDialog() {
        final CharSequence[] options = {"Choose Camera", "Choose Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Upload Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Choose Camera")) {
                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                            Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(RegisterActivity.this.getPackageManager()) != null) {
                            File file = null;
                            try {
                                file = createImageFile();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (file != null) {
                                Uri photoUri = FileProvider.getUriForFile(RegisterActivity.this,
                                        BuildConfig.APPLICATION_ID + ".fileprovider", file);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, OPEN_ADD_CAMERA_CODE);
                            }
                        }
                    } else {
                        requestCameraPermission();
                    }
                } else if (options[item].equals("Choose Gallery")) {
                    if (ActivityCompat.checkSelfPermission(RegisterActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, OPEN_ADD_GALLERY_CODE);
                    } else {
                        requestGalleryPermission();
                    }
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() {
        @SuppressLint("SimpleDateFormat")
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPG_" + timeStamp + "_";
        File storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,
                    ".jpg",
                    storageDir
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case OPEN_ADD_CAMERA_CODE:
                    if (resultCode == RESULT_OK) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath, ImageUtils.getBitmapOptions());
                        Bitmap rotateBitmap = ImageUtils.rotatedImageBitmap(imageFilePath, bitmap);
                        File file = new File(imageFilePath);
                        if (phototype.equals("profile")) {
                            profileImageView.setImageBitmap(rotateBitmap);
                            profile = imageFilePath;
                        } else if (phototype.equals("aadhar")) {
                            img_aadhar.setVisibility(View.VISIBLE);
                            img_aadhar.setImageBitmap(rotateBitmap);
                            aadhar = imageFilePath;
                        } else {
                            img_aadhar.setVisibility(View.GONE);
                        }
                    }
                    break;
                case OPEN_ADD_GALLERY_CODE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                            String path = saveImage(bitmap);
                            if (phototype.equals("profile")) {
                                profileImageView.setImageBitmap(bitmap);
                                profile = saveImage(bitmap);
                            } else if (phototype.equals("aadhar")) {
                                img_aadhar.setVisibility(View.VISIBLE);
                                img_aadhar.setImageBitmap(bitmap);
                                aadhar = saveImage(bitmap);
                            } else {
                                img_aadhar.setVisibility(View.GONE);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    private boolean shouldRequestPermissionRationale(String permissionName) {
        return ActivityCompat.shouldShowRequestPermissionRationale(this, permissionName);
    }

    private void requestProvidedPermissions(String[] permissionsArray, int PERMISSION_CODE) {
        ActivityCompat.requestPermissions(this, permissionsArray, PERMISSION_CODE);
    }

    private void requestGalleryPermission() {
        if (shouldRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Storage Permissions are needed to upload Pictures or Videos")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegisterActivity.this.requestProvidedPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            requestProvidedPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_GALLERY_PERMISSION_CODE);
        }
    }

    private void requestCameraPermission() {
        if (shouldRequestPermissionRationale(Manifest.permission.CAMERA)) {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Permission Info")
                    .setMessage("Camera Permissions are needed to upload Pictures or Videos")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RegisterActivity.this.requestProvidedPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();
        } else {
            requestProvidedPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION_CODE);
        }
    }

    private void registerApiCall() {
        progressDialog.show();
        MultipartBody.Part aadhar_pic = prepareFilePart("aadhar", aadhar);
        MultipartBody.Part profile_pic = prepareFilePart("profile", profile);
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.userRegister(
                RequestBody.create(MediaType.parse("text/plain"), name),
                RequestBody.create(MediaType.parse("text/plain"), emailString),
                RequestBody.create(MediaType.parse("text/plain"), numberString),
                RequestBody.create(MediaType.parse("text/plain"), selectedCountry),
                RequestBody.create(MediaType.parse("text/plain"), selectedState),
                RequestBody.create(MediaType.parse("text/plain"), selectedDistrict),
                RequestBody.create(MediaType.parse("text/plain"), selectedCity),
                RequestBody.create(MediaType.parse("text/plain"), selectedVillage),
                RequestBody.create(MediaType.parse("text/plain"), addressString),
                RequestBody.create(MediaType.parse("text/plain"), selectedPincode),
                RequestBody.create(MediaType.parse("text/plain"), passwordString),
                aadhar_pic, profile_pic);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    progressDialog.cancel();
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            String message = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                            /*String otp = responseObject.getString("otp");
                            Toast.makeText(RegisterActivity.this, otp, Toast.LENGTH_SHORT).show();*/
                            agent_id = responseObject.getString("user_id");

                            JSONObject userObject = responseObject.getJSONObject("user_data");

                            country_id = userObject.getString("country");
                            state_id = userObject.getString("state");
                            district_id = userObject.getString("district");

                            country = userObject.getString("country_name");
                            state = userObject.getString("state_name");
                            district = userObject.getString("district_name");

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("agent_id", agent_id);
                            editor.putString("country_id", country_id);
                            editor.putString("state_id", state_id);
                            editor.putString("district_id", district_id);

                            editor.putString("country", country);
                            editor.putString("state", state);
                            editor.putString("district", district);
                            editor.apply();
                            editor.commit();
                            startActivity(new Intent(RegisterActivity.this, SuccessfulActivity.class));

                        } else {
                            progressDialog.cancel();
                            String failureMsg = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, failureMsg, Toast.LENGTH_SHORT).show();
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

    @NonNull
    private MultipartBody.Part prepareFilePart(String viewname, String fileName) {
        File file = new File(fileName);
// create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/*"),
                        file
                );
// MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(viewname, file.getName(), requestFile);
    }

    private void getCountries() {
        final ArrayList<String> arrayList = new ArrayList<>();
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
                            countryModel = new ArrayList<>();
                            arrayList.add(0, "Select Country");
                            for (int i = 0; i < listArray.length(); i++) {
                                JSONObject listObject = listArray.getJSONObject(i);
                                String id = listObject.getString("id");
                                String countryName = listObject.getString("country");
                                String countryStatus = listObject.getString("status");
                                countryModel.add(new CountryModel(id, countryName, countryStatus));
                                arrayList.add(countryName);
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, arrayList) {
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
                            spinner_country.setSelection(arrayAdapter.getPosition("Select Country"));
                            spinner_country.setAdapter(arrayAdapter);
                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void getStatesApiCall() {
        final ArrayList<String> stateArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getStates(selectedCountry);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray stateListArray = responseObject.getJSONArray("user_id");
                            stateModelList = new ArrayList<>();
                            stateArrayList.add(0, "Select State");
                            for (int i = 0; i < stateListArray.length(); i++) {
                                JSONObject stateObject = stateListArray.getJSONObject(i);
                                String stateId = stateObject.getString("id");
                                String countryId = stateObject.getString("country_id");
                                String stateName = stateObject.getString("state");
                                String status = stateObject.getString("status");
                                stateModelList.add(new StateModel(stateId, countryId, stateName, status));
                                stateArrayList.add(stateName);
                            }
                            ArrayAdapter<String> stateAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, stateArrayList) {
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
                            stateAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_state.setSelection(stateAdapter.getPosition("Select State"));
                            spinner_state.setAdapter(stateAdapter);

                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void getDistrictsApiCall() {
        final ArrayList<String> districtArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getDistricts(selectedState);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray districtArray = responseObject.getJSONArray("user_id");
                            districtModelList = new ArrayList<>();
                            districtArrayList.add(0, "Select District");
                            for (int i = 0; i < districtArray.length(); i++) {
                                JSONObject listObject = districtArray.getJSONObject(i);
                                String id = listObject.getString("id");
                                String country_id = listObject.getString("country_id");
                                String state_id = listObject.getString("state_id");
                                String district = listObject.getString("district");
                                String status = listObject.getString("status");
                                districtModelList.add(new DistrictModel(id, country_id, state_id, district, status));
                                districtArrayList.add(district);
                            }
                            ArrayAdapter<String> districtAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, districtArrayList) {
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
                            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                            spinner_district.setAdapter(districtAdapter);
                        } else {
                            String message = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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

    private void getMandalApiCall() {
        final ArrayList<String> cityArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getMandal(selectedDistrict);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray cityArray = responseObject.getJSONArray("user_id");
                            cityModelList = new ArrayList<>();
                            cityArrayList.add(0, "Select Mandal");
                            for (int i = 0; i < cityArray.length(); i++) {
                                JSONObject cityObject = cityArray.getJSONObject(i);
                                String id = cityObject.getString("id");
                                String country_id = cityObject.getString("country_id");
                                String state_id = cityObject.getString("state_id");
                                String district_id = cityObject.getString("district_id");
                                String city = cityObject.getString("mandal");
                                String status = cityObject.getString("status");
                                cityModelList.add(new CityModel(id, country_id, state_id, district_id, city, status));
                                cityArrayList.add(city);
                            }
                            ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, cityArrayList) {
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
                            cityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_city.setAdapter(cityAdapter);
                        } else {
                            String failureMessage = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, failureMessage, Toast.LENGTH_SHORT).show();
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

    private void getVillagesApiCall() {
        final ArrayList<String> villagesArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getVillages(selectedCity);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray cityArray = responseObject.getJSONArray("user_id");
                            villageModelList = new ArrayList();
                            villagesArrayList.add(0, "Select Village");
                            for (int i = 0; i < cityArray.length(); i++) {
                                JSONObject cityObject = cityArray.getJSONObject(i);
                                String id = cityObject.getString("id");
                                String country_id = cityObject.getString("country_id");
                                String state_id = cityObject.getString("state_id");
                                String district_id = cityObject.getString("district_id");
                                String mandal_id = cityObject.getString("mandal_id");
                                String village = cityObject.getString("village");
                                String status = cityObject.getString("status");
                                villageModelList.add(new VillageModel(id, country_id, state_id, district_id, mandal_id, village, status));
                                villagesArrayList.add(village);
                            }

                            ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, villagesArrayList) {
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
                            villageAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_village.setAdapter(villageAdapter);
                        } else {
                            String failureMessage = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, failureMessage, Toast.LENGTH_SHORT).show();
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

    private void getPincode() {
        final ArrayList<String> picodeArrayList = new ArrayList<>();
        UnnathiLeadJsonPlaceHolder jsonPlaceHolder = RetrofitInstance.getRetrofit().create(UnnathiLeadJsonPlaceHolder.class);
        Call<ResponseBody> responseBodyCall = jsonPlaceHolder.getPincode(selectedVillage);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    try {
                        String responseString = new String(response.body().bytes());
                        JSONObject responseObject = new JSONObject(responseString);
                        if (responseObject.getString("status").equalsIgnoreCase("valid")) {
                            JSONArray pincodeArray = responseObject.getJSONArray("user_id");
                            pincodeMandalList = new ArrayList<>();
                            picodeArrayList.add(0, "Select Pincode");
                            for (int i = 0; i < pincodeArray.length(); i++) {
                                JSONObject cityObject = pincodeArray.getJSONObject(i);
                                String id = cityObject.getString("id");
                                String country_id = cityObject.getString("country_id");
                                String state_id = cityObject.getString("state_id");
                                String district_id = cityObject.getString("district_id");
                                String mandal_id = cityObject.getString("mandal_id");
                                String village_id = cityObject.getString("village_id");
                                String pincode = cityObject.getString("pincode");
                                String status = cityObject.getString("status");
                                pincodeMandalList.add(new PincodeModel(id, country_id, state_id, district_id, mandal_id, village_id, pincode, status));
                                picodeArrayList.add(pincode);
                            }
                            ArrayAdapter<String> villageAdapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, picodeArrayList) {
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
                            villageAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            spinner_pincode.setAdapter(villageAdapter);
                        } else {
                            String failureMessage = responseObject.getString("message");
                            Toast.makeText(RegisterActivity.this, failureMessage, Toast.LENGTH_SHORT).show();
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
}
