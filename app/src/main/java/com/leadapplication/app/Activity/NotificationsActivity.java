package com.leadapplication.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.leadapplication.app.Adapter.NotificationAdapter;
import com.leadapplication.app.Model.NotificationListModel;
import com.leadapplication.app.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private ImageView imgBackNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        castingViews();

        List<NotificationListModel> modelList = new ArrayList<>();
        modelList.add(new NotificationListModel("Debit", "testing notification list", "30-05-2021"));
        modelList.add(new NotificationListModel("Credit", "testing notification list", "29-04-2021"));
        modelList.add(new NotificationListModel("Debit", "testing notification list", "28-04-2021"));
        modelList.add(new NotificationListModel("Debit", "testing notification list", "27-04-2021"));

        NotificationAdapter adapter = new NotificationAdapter(this, modelList);
        rvNotifications.setAdapter(adapter);

        imgBackNotifications.setOnClickListener(v -> onBackPressed());

    }

    private void castingViews() {
        rvNotifications = findViewById(R.id.rvNotifications);
        imgBackNotifications = findViewById(R.id.imgBackNotifications);
    }
}