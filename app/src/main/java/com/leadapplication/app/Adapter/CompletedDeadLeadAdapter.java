package com.leadapplication.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leadapplication.app.Model.CompletedDeadLeadModel;
import com.leadapplication.app.R;

import java.util.List;

public class CompletedDeadLeadAdapter extends RecyclerView.Adapter<CompletedDeadLeadAdapter.ViewHolder> {
    private Context mContext;
    private List<CompletedDeadLeadModel> completedDeadLeadModelList;

    public CompletedDeadLeadAdapter(Context mContext, List<CompletedDeadLeadModel> completedDeadLeadModelList) {
        this.mContext = mContext;
        this.completedDeadLeadModelList = completedDeadLeadModelList;
    }

    public interface LeadsInterface {
        void onLeadClickListener(int position);
    }

    public LeadsInterface leadsInterface;

    public void setLeadsInterface(LeadsInterface leadsInterface) {
        this.leadsInterface = leadsInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mContext).inflate(R.layout.completed_cancel_list, parent, false);
            return new ViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            CompletedDeadLeadModel model = completedDeadLeadModelList.get(position);
            holder.tv_sno.setText(String.valueOf(position + 1));
            holder.tv_name.setText(model.getUserName());
            holder.tv_mobile.setText(model.getMobileNumber());
            holder.tv_status.setText(model.getTitle());
            holder.tv_address.setText(model.getVillageString() + " , " + model.getMandalString() + "\n" + model.getDistrictString() + " , " + model.getStateString());
        }

    }

    @Override
    public int getItemCount() {
        return completedDeadLeadModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_sno, tv_name, tv_mobile, tv_status, tv_address;
        private CardView cv_allLeads;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_address = itemView.findViewById(R.id.tv_address);
            cv_allLeads = itemView.findViewById(R.id.cv_allLeads);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leadsInterface != null) {
                        leadsInterface.onLeadClickListener(getAdapterPosition());
                    }
                }
            });

        }
    }
}
