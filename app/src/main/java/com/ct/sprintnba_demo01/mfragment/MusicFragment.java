package com.ct.sprintnba_demo01.mfragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.MusicPlayActivity;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.fragment.BaseFragment;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.Column_Music;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.VPNewsAdapter;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mfragment.musicfragment.MusicListFragment;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.music.PlayOnlineMusic;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by ct on 2017/2/9.
 * <p>
 * ===================================
 * <p>
 * Music
 * ===================================
 */

public class MusicFragment extends BaseLazyFragment implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.tab_layout_fragment_music)
    TabLayout tabLayout;
    @BindView(R.id.vp_fragment_music)
    ViewPager vp;
    @BindView(R.id.img_music_play_bar_photo)
    ImageView img_music_photo;
    @BindView(R.id.tv_music_play_bar_title)
    TextView tv_music_title;
    @BindView(R.id.tv_music_play_bar_artist)
    TextView tv_music_artist;
    @BindView(R.id.img_music_play_bar_play)
    ImageView img_music_play_pause;
    @BindView(R.id.img_music_play_bar_next)
    ImageView img_music_next;
    @BindView(R.id.progress_music_play_bar)
    ProgressBar progress_music;
    @BindView(R.id.linear_music_title)
    LinearLayout linearLayout;

    private VPNewsAdapter adapter;
    private ArrayList<BaseFragment> mList;
    private String[] tabName = {"本地音乐", "在线音乐"};
    private MusicPlayService mPlayService;
    private Subscription subscription;

    private ECache eCache;
    private Music music;//上一次播放的音乐
    private List<Music> localList = new ArrayList<>();     //缓存的本地歌曲目录
    private List<SongEntity> onlineList = new ArrayList<>();//缓存的在线歌曲目录

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_music);
        initEvent();

    }

    private void initEvent() {
        //获取缓存信息
        eCache = ECache.get(mActivity, OwnConstant.CACHE_MUSIC_BASE);
        music = (Music) eCache.getAsObject(OwnConstant.CACHE_MUSIC_PLAYING);
        if (music != null)
            showMusic(music);


        if (mList == null) {
            mList = new ArrayList<>();
            //为每个Fragment添加标记

            for (String name : tabName) {
                Bundle bundle = new Bundle();
                if ("在线音乐".equals(name))
                    bundle.putSerializable(OwnConstant.COLUMN_MUSIC, Column_Music.ONLINE);
                else
                    bundle.putSerializable(OwnConstant.COLUMN_MUSIC, Column_Music.LOCAL);

                if (music != null)
                    bundle.putSerializable(OwnConstant.MUSIC_TYPE, music.type);

                MusicListFragment fragment = new MusicListFragment();
                fragment.setArguments(bundle);
                mList.add(fragment);
            }

        }

        if (adapter == null)
            adapter = new VPNewsAdapter(getFragmentManager(), mList, tabName);

        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(mList.size());
        tabLayout.setupWithViewPager(vp);

        //绑定服务 和RxBus
        bindService();
        bindRxBus();
    }

    @OnClick({R.id.img_music_play_bar_photo, R.id.linear_music_title, R.id.img_music_play_bar_play, R.id.img_music_play_bar_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_music_play_bar_photo:
            case R.id.linear_music_title:
                //点击跳转到音乐播放界面
                // showPlayingFragment();
                Intent intent = new Intent(mActivity, MusicPlayActivity.class);
                startActivity(intent);
                break;
            case R.id.img_music_play_bar_play:
                //播放或暂停
                if (mPlayService != null) {
                    if (mPlayService.getPlayingMusic() != null) {
                        mPlayService.playPause();
                        img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
                    } else {
                        if (music != null) {
                            mPlayService.play(music);
                            img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
                        }


                    }

                }

                break;
            case R.id.img_music_play_bar_next:
                //下一首
                if (mPlayService != null)
                    next();
                break;

        }
    }

    /**
     * 播放下一首歌曲
     */
    private void next() {
        mPlayService.next();
        //  showMusic();
    }

    private void bindService() {
        Intent intent = new Intent(mActivity, MusicPlayService.class);
        mActivity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((MusicPlayService.PlayBinder) service).getService();

            showMusic();

            initServiceList();


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("TAG", "service connect down!");
        }
    };

    /**
     * 初始化Service 播放队列
     */
    private void initServiceList() {
        if (music == null)
            return;


        if (music.type == Music.Type.LOCAL) {

            //判断SDK 是否大于23 大于则动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (EasyPermissions.hasPermissions(mActivity, permissions)) {
                    //有权限
                    scanLocalMusic();
                } else {
                    //没有权限 申请权限
                    EasyPermissions.requestPermissions(this, "扫描本地歌曲需要的权限！", 0, permissions);
                }

            } else {
                scanLocalMusic();
            }


        } else {
            //获取缓存中的网络歌曲列表
            String jsonList = eCache.getAsString(OwnConstant.CACHE_MUSIC_LIST);
            onlineList = new Gson().fromJson(jsonList, new TypeToken<List<SongEntity>>() {
            }.getType());
            mPlayService.setOnlineMusicList(onlineList);

            for (int i = 0; i < onlineList.size(); i++) {
                if (onlineList.get(i).album_title.equals(music.album)) {
                    mPlayService.setPlayingPosition(i);
                    return;
                }
            }

        }
    }

    /**
     * 扫描本地歌曲
     */
    private void scanLocalMusic() {
        //扫描本地歌曲 并加入Service
        FileUtils.scanMusic(mActivity, localList);
        mPlayService.setMusicList(localList);
        //更新service 中的Position
        for (int i = 0; i < localList.size(); i++) {
            if (localList.get(i).album.equals(music.album)) {
                mPlayService.setPlayingPosition(i);
                return;
            }

        }

        //当循环结束
    }

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        //当重新展示时
        showMusic();

    }

    private void showMusic() {
        if (mPlayService != null) {
            //获取当前播放音乐
            if (mPlayService.getPlayingMusic() != null) {
                showMusic(mPlayService.getPlayingMusic());
                img_music_play_pause.setSelected(mPlayService.isPlaying() ? true : false);
            }
        }
    }

    private void showMusic(Music music) {
        img_music_photo.setImageBitmap(music.cover);
        tv_music_title.setText(music.title);
        tv_music_artist.setText(music.album + " - " + music.artist);
        progress_music.setMax((int) music.duration);

    }

    private void bindRxBus() {

        subscription = RxBus.getInstance().toObserverable(RxDataEvent.class).subscribe(new Action1<RxDataEvent>() {
            @Override
            public void call(RxDataEvent rxDataEvent) {
                if (rxDataEvent.type == OwnConstant.MUSIC_LOCAL_NEXT)
                    showMusic();
                else
                    playMusic(rxDataEvent.type, (SongEntity) rxDataEvent.data);


            }
        });
    }

    @Override
    protected void onDestroyViewLazy() {
        subscription.unsubscribe();
        super.onDestroyViewLazy();
    }

    private void playMusic(final int postion, final SongEntity songEntity) {
        //设置播放栏
        Glide.with(mActivity).load(songEntity.pic_small).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(img_music_photo);
        tv_music_title.setText(songEntity.title);
        tv_music_artist.setText(songEntity.album_title + " - " + songEntity.artist_name);
        progress_music.setMax(Integer.parseInt(songEntity.file_duration) * 1000);

        //开始播放音乐
        new PlayOnlineMusic(mActivity, songEntity) {
            @Override
            public void onPrepare() {
            }

            @Override
            public void onSuccess(Music music) {
                mPlayService.play(postion, music);
            }

            @Override
            public void onFail(Exception e) {
            }
        }.execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //当权限获取成功
        scanLocalMusic();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //当权限获取失败
        Log.i("TAG", "权限申请失败" + perms.toString());
    }
}
