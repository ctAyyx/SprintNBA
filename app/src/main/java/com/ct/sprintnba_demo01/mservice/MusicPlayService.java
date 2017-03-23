package com.ct.sprintnba_demo01.mservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ct.sprintnba_demo01.MainActivity;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.minterface.OnPlayerEventListener;
import com.ct.sprintnba_demo01.mreceiver.NoisyAudioStreamReceiver;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ct on 2017/2/14.
 * <p>
 * ============================
 * 音乐播放后台服务
 * ============================
 */

public class MusicPlayService extends Service implements MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    private static final int NOTIFICATION_ID = 0x0100;
    private static final long TIME_UPDATE = 100L;

    private List<Music> mList = new ArrayList<>();//音乐播放列表
    private List<SongEntity> mSongList = new ArrayList<>();//记录在线音乐
    private MediaPlayer mPlayer = new MediaPlayer();
    private IntentFilter mNoisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private NoisyAudioStreamReceiver mNoisyReceiver = new NoisyAudioStreamReceiver();
    private Handler mHandler = new Handler();
    private AudioManager mAudioManaer;
    private NotificationManager mNotificationManager;
    private OnPlayerEventListener mListener;

    private Music mPlayingMusic;

    private int mPlayingPosition;
    private boolean isPause;
    private long qiutTimerRemain;
    private ECache eCache;
    private Gson gson = new Gson();


    @Override
    public void onCreate() {
        super.onCreate();
        mAudioManaer = (AudioManager) getSystemService(Service.AUDIO_SERVICE);
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        mPlayer.setOnCompletionListener(this);
        eCache = ECache.get(this, OwnConstant.CACHE_MUSIC_BASE);


    }

    public void setOnPlayerEventListener(OnPlayerEventListener listener) {
        this.mListener = listener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null) {
            switch (intent.getAction()) {
                case OwnConstant.ACTION_MEDIA_PLAY_PAUSE:
                    playPause();
                    break;
                case OwnConstant.ACTION_MEDIA_NEXT:
                    next();
                    break;
                case OwnConstant.ACTION_MEDIA_PREVIOUS:
                    prev();
                    break;
            }

        }

        return START_NOT_STICKY;
    }

    public void setMusicList(List<Music> list) {
        this.mList = list;
    }

    public void setOnlineMusicList(List<SongEntity> list) {
        this.mSongList = list;
        eCache.put(OwnConstant.CACHE_MUSIC_LIST, gson.toJson(mSongList));
    }

    public void setPlayingPosition(int position) {
        this.mPlayingPosition = position;
    }

    /**
     * 播放或暂停
     */
    public void playPause() {
        if (isPlaying())
            pause();
        else if (isPause)
            resume();
        else
            play(getPlayingPositon());
    }

    /**
     * 暂停播放
     */
    public int pause() {
        if (!isPlaying())
            return -1;
        mPlayer.pause();
        isPause = true;
        mHandler.removeCallbacks(mBackgroudRunnable);
        cancelNotification(mPlayingMusic);
        mAudioManaer.abandonAudioFocus(this);
        unregisterReceiver(mNoisyReceiver);
        if (mListener != null)
            mListener.onPlayerPause();
        return mPlayingPosition;

    }

    /**
     * 重新开始播放
     */
    public int resume() {
        if (isPlaying())
            return -1;
        start();
        if (mListener != null)
            mListener.onPlayerResume();
        return mPlayingPosition;

    }

    private void start() {
        mPlayer.start();
        isPause = false;
        mHandler.post(mBackgroudRunnable);
        updateNotification(mPlayingMusic);
        mAudioManaer.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        registerReceiver(mNoisyReceiver, mNoisyFilter);
    }

    public int play(int position) {
        if (mList.isEmpty())
            return -1;
        if (position < 0)
            position = mList.size() - 1;
        else if (position >= mList.size())
            position = 0;

        mPlayingPosition = position;
        mPlayingMusic = mList.get(mPlayingPosition);

        try {
            mPlayer.reset();
            mPlayer.setDataSource(mPlayingMusic.uri);
            mPlayer.prepare();
            start();
            if (mListener != null)
                mListener.onChange(mPlayingMusic);
        } catch (Exception e) {
            Log.i("TAG", "本地音乐播放_PLAY" + e.toString());
        }

        RxBus.getInstance().send(new RxDataEvent(OwnConstant.MUSIC_LOCAL_NEXT, mPlayingMusic));
        //保存最新音乐到缓存
        eCache.put(OwnConstant.CACHE_MUSIC_PLAYING, mPlayingMusic);
        return mPlayingPosition;
    }

    public void play(Music music) {
        mPlayingMusic = music;
        //保存最新音乐到缓存
        eCache.put(OwnConstant.CACHE_MUSIC_PLAYING, mPlayingMusic);
        try {
            mPlayer.reset();
            mPlayer.setDataSource(mPlayingMusic.uri);
            mPlayer.prepare();
            start();
        } catch (Exception e) {
            Log.i("TAG", "在线音乐播放_PLAY" + e.toString());
        }
    }

    public void play(int position, Music music) {
        mPlayingPosition = position;
        play(music);
    }

    public int next() {
       /* if (mPlayingMusic.type == Music.Type.LOCAL)
            return play(mPlayingPosition + 1);
        else
            return nextOnline();*/
        return nextMode();
    }

    /**
     * 当播放下一首歌曲时判断
     */
    private int onNext() {
        if (mPlayingMusic == null)
            return -1;

        if (mPlayingMusic.type == Music.Type.LOCAL)
            return play(mPlayingPosition + 1);
        else
            return nextOnline();
    }

    public int nextOnline() {
        if (mPlayingPosition >= mSongList.size() - 1)
            mPlayingPosition = -1;
        mPlayingPosition += 1;
        RxBus.getInstance().send(new RxDataEvent(mPlayingPosition, mSongList.get(mPlayingPosition)));
        return mPlayingPosition;
    }


    public int prev() {
        /*if (mPlayingMusic.type == Music.Type.LOCAL)
            return play(mPlayingPosition - 1);
        else
            return prevOnline();*/
        return prevMode();
    }

    private int onPrev() {
        if (mPlayingMusic.type == Music.Type.LOCAL)
            return play(mPlayingPosition - 1);
        else
            return prevOnline();
    }

    public int prevOnline() {
        if (mPlayingPosition <= 0)
            mPlayingPosition = mSongList.size();
        if (mPlayingPosition >= mSongList.size())
            mPlayingPosition = 1;
        mPlayingPosition -= 1;

        RxBus.getInstance().send(new RxDataEvent(mPlayingPosition, mSongList.get(mPlayingPosition)));
        return mPlayingPosition;
    }

    /**
     * 跳转到指定的时间位置
     */
    public void seekTo(int msec) {
        if (isPlaying() || isPause()) {
            mPlayer.seekTo(msec);
            if (mListener != null)
                mListener.onPublish(msec);
        }
    }

    /**
     * 获取当前播放的本地歌曲的序号
     */
    public int getPlayingPositon() {
        return mPlayingPosition;
    }

    /**
     * 更新通知栏
     */
    private void updateNotification(Music music) {
        mNotificationManager.cancel(NOTIFICATION_ID);
        startForeground(NOTIFICATION_ID, createNotification(this, music));
    }

    private void cancelNotification(Music music) {
        stopForeground(true);
        mNotificationManager.notify(NOTIFICATION_ID, createNotification(this, music));
    }

    /**
     * 判断是否在播放
     */
    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    /**
     * 判断是否暂停
     */
    public boolean isPause() {
        return mPlayer != null && isPause;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextMode();
    }

    /**
     * 根据缓存的播放模式 进行下一首个的播放
     */
    private int nextMode() {
        int num = 0;
        //获取缓存中的播放模式
        String mode = eCache.getAsString(OwnConstant.CACHE_MUSIC_PLAY_MODE);
        int currrentMode = Integer.parseInt(mode);

        //根据当前模式进行下一首的播放
        switch (currrentMode) {
            case OwnConstant.MUSIC_MODE_LOOP://顺序播放
                num = onNext();
                break;
            case OwnConstant.MUSIC_MODE_ONE://单曲循环
                mPlayingPosition -= 1;
                num = onNext();
                break;
            case OwnConstant.MUSIC_MODE_SHUFFLE://随机播放
                if (mPlayingMusic != null) {
                    if (mPlayingMusic.type == Music.Type.LOCAL)
                        mPlayingPosition = (int) (Math.random() * mList.size());
                    else
                        mPlayingPosition = (int) (Math.random() * mSongList.size());
                    num = onNext();
                }
                break;

        }
        return num;
    }


    /**
     * 根据缓存的播放模式 进行上一首个的播放
     */

    private int prevMode() {
        int num = 0;
        //获取缓存中的播放模式
        String mode = eCache.getAsString(OwnConstant.CACHE_MUSIC_PLAY_MODE);
        int currrentMode = Integer.parseInt(mode);

        //根据当前模式进行上一首的播放
        switch (currrentMode) {
            case OwnConstant.MUSIC_MODE_LOOP://顺序播放
                num = onPrev();
                break;
            case OwnConstant.MUSIC_MODE_ONE://单曲循环
                mPlayingPosition += 1;
                num = onPrev();
                break;
            case OwnConstant.MUSIC_MODE_SHUFFLE://随机播放
                if (mPlayingMusic.type == Music.Type.LOCAL)
                    mPlayingPosition = (int) (Math.random() * mList.size());
                else
                    mPlayingPosition = (int) (Math.random() * mSongList.size());
                num = onPrev();
                break;

        }
        return num;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {

    }

    public class PlayBinder extends Binder {
        public MusicPlayService getService() {
            return MusicPlayService.this;
        }
    }


    private Runnable mBackgroudRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying() && mListener != null)
                mListener.onPublish(mPlayer.getCurrentPosition());
            mHandler.postDelayed(this, TIME_UPDATE);
        }
    };

    /**
     * 创建一个Notification
     */
    private Notification createNotification(Context context, Music music) {
        String title = music.title;
        String subTitle = FileUtils.getArtistAndAlbum(music.artist, music.album);
        Bitmap cover;
        if (music.type == Music.Type.LOCAL)
            cover = null;
        else
            cover = music.cover;

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(OwnConstant.FROM_NOTIFICATION, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(subTitle)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(cover);
        return builder.build();

    }

    /**
     * 返回当前正在播放的音乐
     */
    public Music getPlayingMusic() {
        return mPlayingMusic;
    }

    /**
     * 返回MediaPlayer
     */
    public MediaPlayer getMediaPlayer() {
        return mPlayer;
    }

    @Override
    public void onDestroy() {
        Log.i("TAG", "Service onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mListener = null;
        return true;
    }
}
