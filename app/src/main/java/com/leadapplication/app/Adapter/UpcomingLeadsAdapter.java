package com.leadapplication.app.Adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadapplication.app.Model.TodayLeadsModel;
import com.leadapplication.app.R;

import java.util.List;

public class UpcomingLeadsAdapter extends RecyclerView.Adapter<UpcomingLeadsAdapter.ViewHolder> {
    private Context mContext;
    private List<TodayLeadsModel> upcomingModel;

    public UpcomingLeadsAdapter(Context mContext, List<TodayLeadsModel> upcomingModel) {
        this.mContext = mContext;
        this.upcomingModel = upcomingModel;
    }
    public interface UpcomingLeadsViewInterface{
        void onUpcomingListener(int position);
    }
    public UpcomingLeadsViewInterface upcomingLeadsViewInterface;

    public void setUpcomingLeadsViewInterface(UpcomingLeadsViewInterface upcomingLeadsViewInterface) {
        this.upcomingLeadsViewInterface = upcomingLeadsViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_upcoming_leads_adapter,parent,false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodayLeadsModel model = upcomingModel.get(position);
    }

    @Override
    public int getItemCount() {
        return upcomingModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_client,apppoint_time,lead_type,branch_name,apppoint_name;
        private CardView cv_upcomingLeads;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name_client=itemView.findViewById(R.id.name_client);
            apppoint_time=itemView.findViewById(R.id.apppoint_time);
            lead_type=itemView.findViewById(R.id.lead_type);
            branch_name=itemView.findViewById(R.id.branch_name);
            apppoint_name=itemView.findViewById(R.id.apppoint_name);
            cv_upcomingLeads=itemView.findViewById(R.id.cv_upcomingLeads);
            cv_upcomingLeads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (upcomingLeadsViewInterface!=null){
                        upcomingLeadsViewInterface.onUpcomingListener(getAdapterPosition());
                    }
                }
            });
        }
    }
}