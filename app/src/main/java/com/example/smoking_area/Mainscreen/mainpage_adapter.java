package com.example.smoking_area.Mainscreen;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.List;

public class mainpage_adapter extends FragmentStateAdapter {
    private List<Fragment> fragmentList;

    public mainpage_adapter(AppCompatActivity activity, List<Fragment> fragments) {
        super(activity);
        this.fragmentList = fragments;
    }

    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
