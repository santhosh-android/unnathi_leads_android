package com.leadapplication.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.leadapplication.app.Model.HomeDashBoardRvModel;
import com.leadapplication.app.R;

import java.util.List;

public class HomeDashBoardAdapter extends RecyclerView.Adapter<HomeDashBoardAdapter.ViewHolder> {

    private Context mContext;
    private List<HomeDashBoardRvModel> dashBoardRvModel;

    public HomeDashBoardAdapter(Context mContext, List<HomeDashBoardRvModel> dashBoardRvModel) {
        this.mContext = mContext;
        this.dashBoardRvModel = dashBoardRvModel;
    }
    public interface DasboardItemListener{
        void onDashBoardItemClick(int position);
    }
    private DasboardItemListener dasboardItemListener;

    public void setDasboardItemListener(DasboardItemListener dasboardItemListener) {
        this.dasboardItemListener = dasboardItemListener;
    }

    @NonNull
    @Override
    public HomeDashBoardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_home_dash_board_adapter, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeDashBoardAdapter.ViewHolder holder, int position) {
        HomeDashBoardRvModel model = dashBoardRvModel.get(position);
        holder.txt_dash.setText(model.getName_dashBoard());
        Glide.with(mContext)
                .load(model.getImage_dashBoard())
                .into(holder.img_dash);
    }

    @Override
    public int getItemCount() {
        return dashBoardRvModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_dash;
        private ImageView img_dash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_dash = itemView.findViewById(R.id.txt_dash);
            img_dash = itemView.findViewById(R.id.img_dash);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dasboardItemListener!=null){
                        dasboardItemListener.onDashBoardItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }
}