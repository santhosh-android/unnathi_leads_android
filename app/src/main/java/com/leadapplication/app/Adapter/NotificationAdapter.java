package com.leadapplication.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadapplication.app.Model.NotificationListModel;
import com.leadapplication.app.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context mContext;
    private List<NotificationListModel> notificationModelList;

    public NotificationAdapter(Context mContext, List<NotificationListModel> notificationModelList) {
        this.mContext = mContext;
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationListModel model = notificationModelList.get(position);
        holder.tvDate.setText(model.getDate());
        holder.tvNotificationMessage.setText(model.getMessage());
        holder.tvNtitle.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNtitle, tvNotificationMessage, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNtitle = itemView.findViewById(R.id.tvNtitle);
            tvNotificationMessage = itemView.findViewById(R.id.tvNotificationMessage);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
