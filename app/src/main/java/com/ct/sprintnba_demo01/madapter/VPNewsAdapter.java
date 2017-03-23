package com.ct.sprintnba_demo01.madapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ct.sprintnba_demo01.base.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by ct on 2017/1/13.
 */

public class VPNewsAdapter extends FragmentPagerAdapter {

    private ArrayList<BaseFragment> mList;
    private String[] titles;


    public VPNewsAdapter(FragmentManager fm,ArrayList<BaseFragment> list,String[] titles) {
        super(fm);
        this.mList=list;
        this.titles=titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles == null ? "" : titles[position];
    }
}
