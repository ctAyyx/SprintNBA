package com.ct.sprintnba_demo01.base.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/6.
 */

public class ToastUtil {

    private static Timer timer;
    private static boolean isShow = false;

    public static void showToast(Context context, String content) {
        if (context == null)
            return;

        if (timer == null)
            timer = new Timer();

        if (!isShow) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
            isShow = true;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isShow = false;
                }
            }, 500);

        }


    }
}
