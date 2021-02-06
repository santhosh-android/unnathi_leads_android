package com.leadapplication.app.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageLeadsViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList =new ArrayList<>();
    private final List<String>FragmentListTitle= new ArrayList<>();

    public ManageLeadsViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return FragmentListTitle.size();
    }
    public CharSequence getPageTitle(int position){
        return  FragmentListTitle.get(position);
    }
    public  void AddFragment(Fragment fragment,String Title){
        fragmentList.add(fragment);
        FragmentListTitle.add(Title);
    }
}
