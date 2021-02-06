package com.leadapplication.app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.leadapplication.app.Model.LogsListModel;
import com.leadapplication.app.R;

import java.util.List;

public class LogsDetailsAdapter extends RecyclerView.Adapter<LogsDetailsAdapter.ViewHolder> {
    private Context mContext;
    private List<LogsListModel> logsModelList;

    public LogsDetailsAdapter(Context mContext, List<LogsListModel> logsModelList) {
        this.mContext = mContext;
        this.logsModelList = logsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.logs_model_list, parent, false);
        return new ViewHolder(mView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogsListModel model = logsModelList.get(position);
        holder.lead_agent_name.setText(model.getName());
        holder.lead_status.setText(model.getTitle());
        holder.followup_date_view.setText(model.getDate() + "  " + model.getTime());
        String date = model.getDate();
        String time = model.getTime();
        if (date.isEmpty() || date.equalsIgnoreCase(null) && time.isEmpty() ||time.equalsIgnoreCase(null)){
            holder.lay_date.setVisibility(View.GONE);
        }
        holder.lead_remarks.setText(model.getRemarks());
        String is_agent_changed = model.getIsAgentChanged();
        if (is_agent_changed.equalsIgnoreCase("no")) {
            holder.old_agent_name.setVisibility(View.GONE);
            holder.lay_agent.setVisibility(View.GONE);
        }else {
            holder.old_agent_name.setText(model.getOld_agent_name());
            holder.lay_agent.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return logsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView lead_status, followup_date_view, lead_remarks, old_agent_name, lead_agent_name;
        private LinearLayout lay_agent,lay_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lead_status = itemView.findViewById(R.id.lead_status);
            followup_date_view = itemView.findViewById(R.id.followup_date_view);
            lead_remarks = itemView.findViewById(R.id.lead_remarks);
            old_agent_name = itemView.findViewById(R.id.old_agent_name);
            lead_agent_name = itemView.findViewById(R.id.lead_agent_name);
            lay_agent = itemView.findViewById(R.id.lay_agent);
            lay_date = itemView.findViewById(R.id.lay_date);
        }
    }
}
