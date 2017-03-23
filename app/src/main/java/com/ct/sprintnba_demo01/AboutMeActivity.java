package com.ct.sprintnba_demo01;

import android.Manifest;
import android.os.Bundle;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.mfragment.AboutMeFragment;


/**
 * Created by ct on 2017/3/2.
 * =================================
 * 关于我界面
 * ================================
 */

public class AboutMeActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_me;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        setTitle("关于");
        //将布局中的Fragment替换
        getFragmentManager().beginTransaction().replace(R.id.fragment_about_me, new AboutMeFragment()).commit();
    }

}

