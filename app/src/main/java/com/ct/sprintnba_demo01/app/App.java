package com.ct.sprintnba_demo01.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.ct.sprintnba_demo01.base.utils.Preference;
import com.ct.sprintnba_demo01.constant.OwnConstant;

/**
 * Created by Administrator on 2017/2/17.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Preference.init(getApplicationContext());

        //获取缓存中的主题
        if (Preference.getBoolean(OwnConstant.ISNIGHTTHEME))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


    }
}
