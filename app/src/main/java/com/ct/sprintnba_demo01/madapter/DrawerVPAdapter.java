package com.ct.sprintnba_demo01.madapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ct.sprintnba_demo01.base.fragment.BaseFragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/1/13.
 */

public class DrawerVPAdapter extends FragmentPagerAdapter {
  private ArrayList<BaseFragment> mList;

  public DrawerVPAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

}
