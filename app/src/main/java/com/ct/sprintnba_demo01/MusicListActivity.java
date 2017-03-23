package com.ct.sprintnba_demo01;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.MusicListAdapter;
import com.ct.sprintnba_demo01.madapter.MusicListAdapterLocal;
import com.ct.sprintnba_demo01.mcontroller.MusicController;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.MusicEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.minterface.OnPlayerEventListener;
import com.ct.sprintnba_demo01.minterface.OnPlayerEventListenerAdapter;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.music.PlayOnlineMusic;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscription;
import rx.functions.Action1;

public class MusicListActivity extends BaseActivity<MusicController, BaseMusicResponse> {

    @BindView(R.id.recycler_music_list_activity)
    RecyclerView recycler;
    @BindView(R.id.img_music_play_bar_photo)
    ImageView img_music_photo;
    @BindView(R.id.tv_music_play_bar_title)
    TextView tv_music_title;
    @BindView(R.id.tv_music_play_bar_artist)
    TextView tv_music_artist;
    @BindView(R.id.img_music_play_bar_play)
    ImageView img_music_play_pause;
    @BindView(R.id.img_music_play_bar_next)
    ImageView img_music_stop;
    @BindView(R.id.progress_music_play_bar)
    ProgressBar progressBar;
    @BindView(R.id.activity_music_list)
    LinearLayout activityMusicList;
    private String type;//音乐榜单类型
    private MusicController controller;

    private int detault = 20;
    private int offset;

    private MusicListAdapterLocal adapter;
    private ArrayList<SongEntity> mList = new ArrayList<>();

    private MusicPlayService mPlayService;
    private Subscription subscription;

    private ECache eCache;


    @Override

    public int getLayoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {

        Intent intent = getIntent();
        if (intent != null) {
            setTitle(intent.getStringExtra(OwnConstant.MUSIC_NAME));
            type = intent.getStringExtra(OwnConstant.MUSIC_TYPE);
        }

        if (adapter == null)
            adapter = new MusicListAdapterLocal(this, mList, false);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        recycler.setItemAnimator(new DefaultItemAnimator());

        //绑定音乐播放服务
        bindService();
        bindRxBus();

        //开始加载数据
        if (controller == null)
            controller = new MusicController(this, this);
        controller.getMusicList(100, type, detault, offset);

        setLoadMore();

        //设置监听
        adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListenerDefualt<SongEntity>() {
            @Override
            public void onItemClick(int postion, View itemView, SongEntity songEntity) {
                //播放歌曲
                playMusic(postion, songEntity);
                if (mPlayService != null)
                    mPlayService.setOnlineMusicList(mList);

            }
        });

        //获取缓存数据
        eCache = ECache.get(this, OwnConstant.CACHE_MUSIC_BASE);
        Music music = (Music) eCache.getAsObject(OwnConstant.CACHE_MUSIC_PLAYING);
        if (music != null) {
            img_music_photo.setImageBitmap(music.cover);
            tv_music_title.setText(music.title);
            tv_music_artist.setText(music.album + " - " + music.artist);
        }

    }

    private void playMusic(final int postion, final SongEntity songEntity) {
        //设置播放栏
        Glide.with(MusicListActivity.this).load(songEntity.pic_small).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(img_music_photo);
        tv_music_title.setText(songEntity.title);
        tv_music_artist.setText(songEntity.album_title + " - " + songEntity.artist_name);
        progressBar.setMax(Integer.parseInt(songEntity.file_duration) * 1000);

        //开始播放音乐
        new PlayOnlineMusic(MusicListActivity.this, songEntity) {
            @Override
            public void onPrepare() {
            }

            @Override
            public void onSuccess(Music music) {
                mPlayService.play(postion, music);
                img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
            }

            @Override
            public void onFail(Exception e) {
            }
        }.execute();
    }

    private void init() {
        //获取当前Music
        if (mPlayService.getPlayingMusic() != null) {
            //当前正在播放音乐
            img_music_photo.setImageBitmap(mPlayService.getPlayingMusic().cover);
            tv_music_title.setText(mPlayService.getPlayingMusic().title);
            tv_music_artist.setText(mPlayService.getPlayingMusic().album + " - " + mPlayService.getPlayingMusic().artist);
            progressBar.setMax((int) mPlayService.getPlayingMusic().duration);

            img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);

        }
        mPlayService.setOnPlayerEventListener(new OnPlayerEventListenerAdapter() {
            @Override
            public void onPublish(int progress) {
                progressBar.setProgress(progress);
            }
        });
    }

    @OnClick({R.id.img_music_play_bar_play, R.id.img_music_play_bar_next, R.id.img_music_play_bar_photo, R.id.linear_music_title})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_music_play_bar_play:
                if (mPlayService != null) {
                    mPlayService.playPause();
                    img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
                }

                break;
            case R.id.img_music_play_bar_next:
                //从缓存中取出
                if (mPlayService != null)
                    mPlayService.nextOnline();
                break;
            case R.id.img_music_play_bar_photo:
            case R.id.linear_music_title:
                Intent intent = new Intent(this, MusicPlayActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 实现自动加载更多
     */
    private void setLoadMore() {

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int positon = ((LinearLayoutManager) manager).findLastVisibleItemPosition();

                if (positon == recyclerView.getAdapter().getItemCount() - 1) {
                    controller.getMusicList(100, type, detault, offset);
                }
            }
        });
    }


    @Override
    public void onUpdateView(int reqId, BaseMusicResponse response) {
        super.onUpdateView(reqId, response);

        switch (reqId) {
            case 100:
                //获取歌曲成功
                if (response.data != null) {
                    ArrayList<SongEntity> newList = (ArrayList<SongEntity>) response.data;
                    if (!newList.isEmpty()) {
                        adapter.addAll(newList);
                    }
                }
                //起始位置自增
                offset = adapter.getItemCount();
                break;

        }
    }


    private void bindService() {
        Intent intent = new Intent();
        intent.setClass(this, MusicPlayService.class);
        bindService(intent, mPlayserviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void bindRxBus() {
        subscription = RxBus.getInstance().toObserverable(RxDataEvent.class).subscribe(new Action1<RxDataEvent>() {
            @Override
            public void call(RxDataEvent rxDataEvent) {
                playMusic(rxDataEvent.type, (SongEntity) rxDataEvent.data);
            }
        });
    }


    private ServiceConnection mPlayserviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((MusicPlayService.PlayBinder) service).getService();
            init();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onDestroy() {
        mPlayService.getPlayingMusic();
        unbindService(mPlayserviceConnection);
        subscription.unsubscribe();
        super.onDestroy();
    }
}
