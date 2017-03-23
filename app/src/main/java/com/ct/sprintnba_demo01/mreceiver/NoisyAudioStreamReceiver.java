package com.ct.sprintnba_demo01.mreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.media.session.PlaybackStateCompat;

import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;

/**
 * Created by ct on 2017/2/15.
 * ============================
 * 来电 或 拔出耳机暂停播放
 * ============================
 */

public class NoisyAudioStreamReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, MusicPlayService.class);
        serviceIntent.setAction(OwnConstant.ACTION_MEDIA_PLAY_PAUSE);
        context.startService(serviceIntent);
    }
}
