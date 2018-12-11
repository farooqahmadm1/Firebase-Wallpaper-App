package com.example.farooq.wallpaperapp.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.farooq.wallpaperapp.Fragment.CategoryFragment;
import com.example.farooq.wallpaperapp.Fragment.TrendingFragment;
import com.example.farooq.wallpaperapp.Fragment.RecentFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MyFragmentPagerAdapter(FragmentManager fm,Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return CategoryFragment.getInstance();
            case 1:
                return TrendingFragment.getInstance();
            case 2:
                return RecentFragment.getInstance(context);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Category";
            case 1:
                return "Trending";
            case 2:
                return "Recents";
        }
        return "";
    }
}
