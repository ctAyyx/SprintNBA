package com.ct.sprintnba_demo01;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.VPMusicPlayAdapter;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.minterface.OnPlayerEventListener;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.music.PlayOnlineMusic;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.ct.sprintnba_demo01.mview.AlbumCoverView;
import com.ct.sprintnba_demo01.mview.IndicatorLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import me.wcy.lrcview.LrcView;
import rx.Subscription;
import rx.functions.Action1;

public class MusicPlayActivity extends BaseActivity implements OnPlayerEventListener, ViewPager.OnPageChangeListener, SeekBar.OnSeekBarChangeListener {
    @BindView(R.id.img_music_play_backgroud)
    ImageView img_backgroud;
    @BindView(R.id.vp_music_play)
    ViewPager vp;
    @BindView(R.id.indicator_music_play)
    IndicatorLayout indicator;
    @BindView(R.id.tv_music_play_time_current)
    TextView tv_time_current;
    @BindView(R.id.seek_music_play_progress)
    SeekBar seekbar;
    @BindView(R.id.tv_music_play_time_total)
    TextView tv_time_total;
    @BindView(R.id.img_music_play_mode)
    ImageView img_mode;
    @BindView(R.id.img_music_play_prev)
    ImageView img_prev;
    @BindView(R.id.img_music_play_pause_play)
    ImageView img_play_pause;
    @BindView(R.id.img_music_play_next)
    ImageView img_next;
    private AlbumCoverView albumCoverView;
    private LrcView lrc_img, lrc;

    private MusicPlayService mPlayService;
    private VPMusicPlayAdapter adapter;
    private ArrayList<View> mList = new ArrayList<>();

    private Subscription subscription;
    private ECache eCache; //缓存类


    @Override
    public int getLayoutId() {
        return R.layout.activity_music_play;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        indicator.onCreate(2);
        getToolBar().setBackgroundColor(Color.TRANSPARENT);

        //绑定Playservice
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, mPlayserviceConnection, Context.BIND_AUTO_CREATE);

        //绑定Rxbus
        bindRxBus();

        adapter = new VPMusicPlayAdapter(mList);
        vp.setAdapter(adapter);

        vp.addOnPageChangeListener(this);
        seekbar.setOnSeekBarChangeListener(this);

        //获取缓存
        initCache();


    }

    /**
     * 获取缓存
     */
    private void initCache() {
        if (eCache == null)
            eCache = ECache.get(this, OwnConstant.CACHE_MUSIC_BASE);//从手机内存中取出\

        //获取缓存的播放模式
        String mode = eCache.getAsString(OwnConstant.CACHE_MUSIC_PLAY_MODE);
        if (TextUtils.isEmpty(mode))
            eCache.put(OwnConstant.CACHE_MUSIC_PLAY_MODE, OwnConstant.MUSIC_MODE_LOOP + "");
        else
            img_mode.setImageLevel(Integer.parseInt(mode));

        //获取缓存的播放状态
        String state = eCache.getAsString(OwnConstant.CACHE_MUSIC_STATE);
        if (!TextUtils.isEmpty(state)) {

        }

    }

    /**
     * 在当前方法中保存的播放状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);


    }

    private void bindRxBus() {
        subscription = RxBus.getInstance().toObserverable(RxDataEvent.class).subscribe(new Action1<RxDataEvent>() {
            @Override
            public void call(RxDataEvent rxDataEvent) {
                if (rxDataEvent.type == OwnConstant.MUSIC_LOCAL_NEXT)//本地音乐
                {
                    if (rxDataEvent.data != null)
                        initView((Music) rxDataEvent.data);
                } else //网络音乐
                    nextOnline(rxDataEvent.type, (SongEntity) rxDataEvent.data);

            }
        });
    }

    private void nextOnline(final int position, SongEntity songEntity) {
        //开始播放音乐
        new PlayOnlineMusic(MusicPlayActivity.this, songEntity) {
            @Override
            public void onPrepare() {
            }

            @Override
            public void onSuccess(Music music) {
                mPlayService.play(position, music);
                initView(music);
            }

            @Override
            public void onFail(Exception e) {
            }
        }.execute();
    }

    private void init(Music music) {
        //给Viewpager添加界面
        View musicPalyImg = View.inflate(this, R.layout.fragment_music_play_img, null);
        View musicPalyLry = View.inflate(this, R.layout.fragment_music_play_lrc, null);
        //初始化专辑界面的控件
        albumCoverView = (AlbumCoverView) musicPalyImg.findViewById(R.id.album_music_play_img);
        lrc_img = (LrcView) musicPalyImg.findViewById(R.id.lrc_music_play_img);
        albumCoverView.initNeedle(mPlayService.isPlaying());
        //初始化状态
        if (mPlayService.isPlaying()) {
            albumCoverView.start();
            img_play_pause.setSelected(true);
        }


        //初始化歌词界面
        lrc = (LrcView) musicPalyLry.findViewById(R.id.lrc_music_play_lrc);

        mList.add(musicPalyImg);
        mList.add(musicPalyLry);
        adapter.notifyDataSetChanged();
        initView(music);

    }

    private void initView(Music music) {
        //在专辑界面
        setTitle(music == null ? "未知" : music.title);
        albumCoverView.setCoverBitmap(music == null ? null : music.cover);
        seekbar.setMax(music == null ? 0 : (int) music.duration);
        if (music == null)
            img_play_pause.setSelected(false);
        else
            img_play_pause.setSelected(mPlayService.isPlaying() ? true : false);

        seekbar.setProgress(mPlayService.getMediaPlayer().getCurrentPosition());

        //加载歌词
        setLrc(music);
    }

    /**
     * 设置歌词
     *
     * @param music 当前播放的歌曲
     */
    private void setLrc(Music music) {
        if (music == null)
            return;
        String lrcPath = FileUtils.getLrcDir() + FileUtils.getLrcFileName(music.artist, music.title);
        loadLrc(lrcPath);
    }

    private void loadLrc(String lrcPath) {
        File file = new File(lrcPath);
        lrc_img.loadLrc(file);
        lrc.loadLrc(file);


        new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (lrc.hasLrc()) {
                        Log.i("TAG", "当前进度" + mPlayService.getMediaPlayer().getCurrentPosition());
                        lrc.updateTime(mPlayService.getMediaPlayer().getCurrentPosition());
                        lrc_img.updateTime(mPlayService.getMediaPlayer().getCurrentPosition());
                        break;
                    }
                }
            }
        }.start();


    }

    private ServiceConnection mPlayserviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((MusicPlayService.PlayBinder) service).getService();
            mPlayService.setOnPlayerEventListener(MusicPlayActivity.this);
            init(mPlayService.getPlayingMusic());

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    @OnClick({R.id.img_music_play_mode,
            R.id.img_music_play_prev,
            R.id.img_music_play_pause_play,
            R.id.img_music_play_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_music_play_mode:
                String mode = eCache.getAsString(OwnConstant.CACHE_MUSIC_PLAY_MODE);
                if (TextUtils.isEmpty(mode)) {
                    mode = OwnConstant.MUSIC_MODE_LOOP + "";
                    eCache.put(OwnConstant.CACHE_MUSIC_PLAY_MODE, mode);
                }
                checkMode(mode);

                break;
            case R.id.img_music_play_prev:
                if (mPlayService != null)
                    mPlayService.prev();
                break;
            case R.id.img_music_play_next:
                if (mPlayService != null) {
                    mPlayService.next();
                }
                break;
            case R.id.img_music_play_pause_play:
                if (mPlayService != null) {
                    mPlayService.playPause();
                    onPlayOrPause();
                }
                break;
        }
    }

    private void checkMode(String mode) {
        //当前模式改变
        int oldmode = Integer.parseInt(mode);
        int newMode = (oldmode + 1) % 3;

        switch (newMode) {
            case OwnConstant.MUSIC_MODE_ONE:
                img_mode.setImageLevel(2);
                break;
            case OwnConstant.MUSIC_MODE_LOOP:
                img_mode.setImageLevel(0);
                break;
            case OwnConstant.MUSIC_MODE_SHUFFLE:
                img_mode.setImageLevel(1);
                break;
        }
        eCache.put(OwnConstant.CACHE_MUSIC_PLAY_MODE, newMode + "");

    }

    /**
     * 播放或暂停 UI修改
     */
    private void onPlayOrPause() {
        img_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
        if (!mPlayService.isPlaying())
            albumCoverView.pause();
        else
            albumCoverView.start();
    }


    /*******************************
     * 音乐播放监听
     *******************************/
    @Override
    public void onPublish(int progress) {
        if (seekbar.getMax() != 0)
            seekbar.setProgress(progress);

        if (lrc_img.hasLrc())
            lrc_img.updateTime(progress);
        if (lrc.hasLrc())
            lrc.updateTime(progress);

        timeFormate(progress);
    }

    @Override
    public void onChange(Music music) {

    }

    @Override
    public void onPlayerPause() {
        Log.i("TAG", "播放暂停");
    }

    @Override
    public void onPlayerResume() {
        Log.i("TAG", "播放重新开始");
    }

    @Override
    public void onTimer(long remain) {

    }

    @Override
    protected void onDestroy() {
        unbindService(mPlayserviceConnection);
        subscription.unsubscribe();
        super.onDestroy();
    }


    /**************************
     * ViewPager事件
     **************************/

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        indicator.setCurrentSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**************************************
     * SeekBar 事件                      ==
     **************************************/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mPlayService != null) {
                mPlayService.seekTo(progress);
                lrc_img.onDrag(progress);
                lrc.onDrag(progress);

            } else {
                seekbar.setProgress(0);
            }

        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /******************
     * 时间格式
     *******************/
    private Date date;
    private SimpleDateFormat format;

    private void timeFormate(long time) {
        if (date == null)
            date = new Date(time);
        if (format == null)
            format = new SimpleDateFormat("mm:ss");
        String currentTime = format.format(date);
    }
}
