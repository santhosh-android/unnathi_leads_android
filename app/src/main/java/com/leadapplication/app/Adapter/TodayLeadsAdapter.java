package com.leadapplication.app.Adapter;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.Model.TodayLeadsModel;
import com.leadapplication.app.R;

import java.util.List;

public class TodayLeadsAdapter extends RecyclerView.Adapter<TodayLeadsAdapter.ViewHolder> {
    private Context mContext;
    private List<TodayLeadsModel> todayLeadsModel;

    public TodayLeadsAdapter(Context mContext, List<TodayLeadsModel> todayLeadsModel) {
        this.mContext = mContext;
        this.todayLeadsModel = todayLeadsModel;
    }

    public interface LeadsViewInterface {
        void onLeadItemClickListener(int position);
    }

    public LeadsViewInterface leadsViewInterface;

    public void setLeadsViewInterface(LeadsViewInterface leadsViewInterface) {
        this.leadsViewInterface = leadsViewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_today_leads_adapter, parent, false);
        return new ViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodayLeadsModel model = todayLeadsModel.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_name.setText(model.getUser_name());
        holder.tv_mobile.setText(model.getMobile());
        holder.tv_status.setText(model.getTitle());
        holder.tv_address.setText(model.getVillage() + " , " + model.getMandal() + "\n" + model.getDistrict() + " , " + model.getState());
    }

    @Override
    public int getItemCount() {
        return todayLeadsModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_sno, tv_name, tv_mobile, tv_status, tv_address;
        private CardView cv_todayLeads;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_address = itemView.findViewById(R.id.tv_address);
            cv_todayLeads = itemView.findViewById(R.id.cv_todayLeads);

            cv_todayLeads.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leadsViewInterface != null) {
                        leadsViewInterface.onLeadItemClickListener(getAdapterPosition());
                    }
                }
            });
        }
    }
}