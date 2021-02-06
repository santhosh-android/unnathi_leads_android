package com.leadapplication.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.leadapplication.app.Adapter.HomeDashBoardAdapter;
import com.leadapplication.app.Adapter.SliderViewPagerAdapter;
import com.leadapplication.app.Controller.CheckAgentStatusController;
import com.leadapplication.app.Controller.HomeBannersApiController;
import com.leadapplication.app.Model.HomeDashBoardRvModel;
import com.leadapplication.app.Model.SliderViewPagerModel;
import com.leadapplication.app.R;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, HomeBannersApiController.HomeBannersInreface, HomeDashBoardAdapter.DasboardItemListener, CheckAgentStatusController.CheckAgentListener {
    private RecyclerView recyclerView_dash;
    private HomeDashBoardAdapter homeDashBoardAdapter;
    private List<HomeDashBoardRvModel> modelList;
    private ImageView img_navOpen;
    private DrawerLayout drawer_home;
    private ViewPager vp_slider;
    private NavigationView navigation_home;
    private List<SliderViewPagerModel> sliderViewPagerModelsList;
    private CardView card_get;
    private ProgressBar progressBar;
    private SpringDotsIndicator spring_dots_indicator;
    private SharedPreferences sharedPreferences;
    private TabLayout tab_home;
    private ViewPager viewpager_home;
    private String countryName, stateName, districtName, userId, status;
    private TextView wish_text, name_text;
    private String profileImg, name_user;
    private CircleImageView profile_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("unnathiLead", MODE_PRIVATE);
        userId = sharedPreferences.getString("agent_id", "");

        castingViews();
        checkAgentStatus();

        if (getIntent().hasExtra("country")) {
            countryName = getIntent().getStringExtra("country");
            stateName = getIntent().getStringExtra("state");
            districtName = getIntent().getStringExtra("district");
            profileImg = getIntent().getStringExtra("profile_image");
        }
        Glide.with(this)
                .load(sharedPreferences.getString("image", ""))
                .placeholder(R.drawable.profile)
                .into(profile_img);
        name_text.setText(name_user = sharedPreferences.getString("name", ""));

        recyclerViewInitilization();
        getHomeBannerApiCall();
        //homeTablayout();

        img_navOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNav();
            }
        });
        navigation_home.setNavigationItemSelectedListener(this);

        card_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, UserDetailsActivity.class));
            }
        });
        //setupTabIcons();

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            //Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
            wish_text.setText("Good Morning");
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            // Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
            wish_text.setText("Good Afternoon");
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            wish_text.setText("Good Evening");
            //Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            wish_text.setText("Good Night");
            //Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAgentStatus() {
        CheckAgentStatusController.getCheckAgentStatusController().CheckAgentStatus(userId);
        CheckAgentStatusController.getCheckAgentStatusController().setCheckAgentListener(this);
    }

    private void getHomeBannerApiCall() {
        progressBar.setVisibility(View.VISIBLE);
        HomeBannersApiController.getHomeBannersApiController().homeBannersApiCall();
        HomeBannersApiController.getHomeBannersApiController().setHomeBannersInreface(this);
    }

    private void viewPagerSlider(List<SliderViewPagerModel> sliderViewList) {
        SliderViewPagerAdapter adapter = new SliderViewPagerAdapter(this, sliderViewList);
        vp_slider.setOffscreenPageLimit(adapter.getCount());
        vp_slider.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHomeBannerApiCall();
    }

    private void openNav() {
        if (drawer_home.isDrawerVisible(GravityCompat.START)) {
            drawer_home.closeDrawer(GravityCompat.START);
        } else {
            drawer_home.openDrawer(GravityCompat.START);
        }
    }

    private void castingViews() {
        recyclerView_dash = findViewById(R.id.recyclerView_dash);
        img_navOpen = findViewById(R.id.img_navOpen);
        vp_slider = findViewById(R.id.vp_slider);
        navigation_home = findViewById(R.id.navigation_home);
        drawer_home = findViewById(R.id.drawer_home);
        card_get = findViewById(R.id.card_get);
        progressBar = findViewById(R.id.progressBar);
        spring_dots_indicator = findViewById(R.id.spring_dots_indicator);
        wish_text = findViewById(R.id.wish_text);
        profile_img = findViewById(R.id.profile_img);
        name_text = findViewById(R.id.name_text);
        /*viewpager_home = findViewById(R.id.viewpager_home);
        tab_home = findViewById(R.id.tab_home);*/
    }


    private void recyclerViewInitilization() {
        recyclerView_dash.setHasFixedSize(true);
        int spanCount = 2;
        recyclerView_dash.setLayoutManager(new GridLayoutManager(this, spanCount));
        modelList = new ArrayList<>();
        modelList.add(new HomeDashBoardRvModel(R.drawable.add, "Add Leads"));
        modelList.add(new HomeDashBoardRvModel(R.drawable.mange_leads, "Manage Leads"));
        modelList.add(new HomeDashBoardRvModel(R.drawable.complete_dead_ledas, "My Leads"));
        modelList.add(new HomeDashBoardRvModel(R.drawable.profile, "Profile"));
        homeDashBoardAdapter = new HomeDashBoardAdapter(this, modelList);
        homeDashBoardAdapter.setDasboardItemListener(this);
        recyclerView_dash.setAdapter(homeDashBoardAdapter);
    }

    @Override
    public void onDashBoardItemClick(int position) {
        if (position == 1) {
            startActivity(new Intent(MainActivity.this, ManageLeadsActivity.class));
        } else if (position == 3) {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
        }  else if (position == 0) {
            Intent intent = new Intent(MainActivity.this, UserDetailsActivity.class);
            intent.putExtra("country", countryName);
            intent.putExtra("state", stateName);
            intent.putExtra("district", districtName);
            startActivity(intent);
        } else if (position == 2) {
            startActivity(new Intent(MainActivity.this, TotalLeads.class));
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        int id = item.getItemId();
        if (id == R.id.dash) {
            startActivity(new Intent(this, MainActivity.class));
            drawer_home.closeDrawer(GravityCompat.START);
        } else if (id == R.id.profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            drawer_home.closeDrawer(GravityCompat.START);
        } else if (id == R.id.manage_leads) {
            startActivity(new Intent(this, ManageLeadsActivity.class));
            drawer_home.closeDrawer(GravityCompat.START);
        } else if (id == R.id.add_lead) {
            startActivity(new Intent(this, UserDetailsActivity.class));
            drawer_home.closeDrawer(GravityCompat.START);
        } else if (id == R.id.log_out) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are sure want to exit")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            drawer_home.closeDrawer(GravityCompat.START);
        } else if (id == R.id.completed) {
            startActivity(new Intent(this, TotalLeads.class));
            drawer_home.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBannersSuccess(List<SliderViewPagerModel> sliderViewPagerModelList) {
        progressBar.setVisibility(View.GONE);
        this.sliderViewPagerModelsList = new ArrayList<>(sliderViewPagerModelList);
        viewPagerSlider(sliderViewPagerModelList);
        spring_dots_indicator.setViewPager(vp_slider);
    }

    @Override
    public void onbannersFailure(String failure) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, failure, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onAgentSucces(JSONObject jsonObject) {
        try {
            status = jsonObject.getString("status");
            if (status.equalsIgnoreCase("invalid")) {
                String message = jsonObject.getString("message");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAgentFailure(String failureString) {
        Toast.makeText(this, failureString, Toast.LENGTH_SHORT).show();
    }
}