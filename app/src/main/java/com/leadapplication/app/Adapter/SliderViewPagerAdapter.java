package com.leadapplication.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.leadapplication.app.Model.SliderViewPagerModel;
import com.leadapplication.app.R;


import java.util.List;

public class SliderViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<SliderViewPagerModel> sliderViewPagerModelList;

    public SliderViewPagerAdapter(Context mContext, List<SliderViewPagerModel> sliderViewPagerModelList) {
        this.mContext = mContext;
        this.sliderViewPagerModelList = sliderViewPagerModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_slider_view_pager_adapter, null);
        ImageView slider_image = mView.findViewById(R.id.img_sliderView);
        SliderViewPagerModel sliderModel = sliderViewPagerModelList.get(position);

        Glide.with(mContext)
                .load(sliderModel.getImgSlider())
                .into(slider_image);
        container.addView(mView);
        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return sliderViewPagerModelList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}