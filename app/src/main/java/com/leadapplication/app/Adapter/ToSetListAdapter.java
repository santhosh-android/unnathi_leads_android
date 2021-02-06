package com.leadapplication.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.leadapplication.app.Model.TosetModelList;
import com.leadapplication.app.R;

import java.util.List;

public class ToSetListAdapter extends RecyclerView.Adapter<ToSetListAdapter.ViewHolder> {
    private Context mContext;
    private List<TosetModelList> tosetModelListList;

    public ToSetListAdapter(Context mContext, List<TosetModelList> tosetModelListList) {
        this.mContext = mContext;
        this.tosetModelListList = tosetModelListList;
    }

    public interface TosetListener {
        void onItemListener(int position);
    }

    public TosetListener tosetListener;

    public void setTosetListener(TosetListener tosetListener) {
        this.tosetListener = tosetListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.toset_leads_list, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TosetModelList modelList = tosetModelListList.get(position);
        holder.tv_sno.setText(String.valueOf(position + 1));
        holder.tv_name.setText(modelList.getUserName());
        holder.tv_mobile.setText(modelList.getMobileNumber());
        holder.tv_status.setText(modelList.getTitle());
        holder.tv_address.setText(modelList.getVillageString() + " , " + modelList.getMandalString() + "\n" + modelList.getDistrictString() + " , " + modelList.getStateString());

    }

    @Override
    public int getItemCount() {
        return tosetModelListList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_sno, tv_name, tv_mobile, tv_status, tv_address;
        private CardView cv_toset;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_sno = itemView.findViewById(R.id.tv_sno);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_status = itemView.findViewById(R.id.tv_status);
            tv_address = itemView.findViewById(R.id.tv_address);
            cv_toset = itemView.findViewById(R.id.cv_todayLeads);

            cv_toset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tosetListener != null) {
                        tosetListener.onItemListener(getAdapterPosition());
                    }
                }
            });
        }
    }
}
