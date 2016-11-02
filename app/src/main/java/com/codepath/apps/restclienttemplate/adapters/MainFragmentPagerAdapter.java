package com.codepath.apps.restclienttemplate.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.codepath.apps.restclienttemplate.fragments.TimelineFragment;

/**
 * Created on 11/1/16.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 1;
    private String tabTitles[] = new String[]{"Timeline"};
    private Context mContext;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return TimelineFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
