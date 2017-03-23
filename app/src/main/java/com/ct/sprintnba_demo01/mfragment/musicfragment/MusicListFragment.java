package com.ct.sprintnba_demo01.mfragment.musicfragment;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ct.sprintnba_demo01.MusicListActivity;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.Column_Music;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.MusicListAdapter;
import com.ct.sprintnba_demo01.madapter.MusicListAdapterLocal;
import com.ct.sprintnba_demo01.mcontroller.MusicController;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.MusicEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mfragment.mmfragment.MMListFragment;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by ct on 2017/2/9.
 */

public class MusicListFragment extends BaseLazyFragment<BaseMusicResponse> implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.recycler_music_list)
    RecyclerView recyclerView;
    @BindView(R.id.swip_music)
    SwipeRefreshLayout swipeRefreshLayout;

    private Column_Music column_music = Column_Music.LOCAL;

    private String[] musicTitle;
    private String[] musicType;


    private ArrayList<MusicEntity> mList = new ArrayList<>();
    private List<Music> musicList = new ArrayList<>();//扫描本地过去音乐
    private List<SongEntity> localList = new ArrayList<>();//本地音乐列表
    ;
    private MusicListAdapter adapter;              //网络
    private MusicListAdapterLocal adapterLocal;  //本地

    private MusicController controller;
    private Intent mIntent;

    private MusicPlayService mPlayService;
    private Music.Type type;
    private ECache eCache;
    private Gson gson = new Gson();

    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_music_list);

        Bundle bundle = getArguments();
        if (bundle != null) {
            column_music = (Column_Music) bundle.getSerializable(OwnConstant.COLUMN_MUSIC);
            type = (Music.Type) bundle.getSerializable(OwnConstant.MUSIC_TYPE);
        }
        eCache = ECache.get(mActivity, OwnConstant.CACHE_MUSIC_BASE);

        //如果type不为空
       /* if (type != null)
            getCacheList();*/


        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        swipeRefreshLayout.setEnabled(false);

        if (Column_Music.ONLINE == column_music) {
            //判断如果是在线
            adapter = new MusicListAdapter(mActivity, mList);
            recyclerView.setAdapter(adapter);
            getOnlineMusic();
            //添加点击事件
            adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListenerDefualt<MusicEntity>() {
                @Override
                public void onItemClick(int postion, View itemView, MusicEntity musicEntity) {
                    if (mIntent == null)
                        mIntent = new Intent(mActivity, MusicListActivity.class);

                    mIntent.putExtra(OwnConstant.MUSIC_TYPE, musicEntity.type);
                    mIntent.putExtra(OwnConstant.MUSIC_NAME, musicEntity.name);
                    startActivity(mIntent);

                }
            });
        } else {
            //判断如果是本地
            adapterLocal = new MusicListAdapterLocal(mActivity, localList, true);
            recyclerView.setAdapter(adapterLocal);

            //添加点击事件
            adapterLocal.setOnItemClickListener(new BaseRVAdapter.OnItemClickListenerDefualt<SongEntity>() {
                @Override
                public void onItemClick(int postion, View itemView, SongEntity entity) {
                    //开始播放音乐
                    if (mPlayService != null) {
                        mPlayService.play(postion, musicList.get(postion));
                        mPlayService.setMusicList(musicList);
                        RxBus.getInstance().send(new RxDataEvent(OwnConstant.MUSIC_LOCAL_NEXT, musicList.get(postion)));
                    }
                }
            });

            bindService();
        }


    }

    /**
     * 在每次重新展示给用户时
     * 扫描本地歌曲
     */
    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
        //扫描本地歌曲
        if (column_music == Column_Music.LOCAL) {
            //判断SDK 是否大于23 大于则动态申请权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (EasyPermissions.hasPermissions(mActivity, permissions))
                    //有权限
                    scanMusic();
                else
                    //没有权限 申请权限
                    EasyPermissions.requestPermissions(this, "扫描本地歌曲需要的权限！", 0, permissions);


            } else
                scanMusic();

        }

    }

    private void bindService() {
        Intent intent = new Intent(mActivity, MusicPlayService.class);
        mActivity.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mPlayService = ((MusicPlayService.PlayBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    /**
     * 扫描本地歌曲
     */
    private void scanMusic() {
        //扫描本地歌曲
        FileUtils.scanMusic(mActivity, musicList);
        if (musicList != null && !musicList.isEmpty()) {
            //转换Music 为 SongEntity
            localList.clear();
            for (Music music : musicList) {
                SongEntity entity = new SongEntity();
                entity.title = music.title;
                entity.artist_name = music.artist;
                entity.album_title = music.album;
                entity.pic_small = music.coverUri;
                entity.author = music.artist;
                entity.url = music.uri;

                localList.add(entity);
            }
            adapterLocal.notifyDataSetChanged();

        }
    }

    /**
     * 获取网络音乐列表
     */
    private void getOnlineMusic() {
        //获取榜单名
        musicTitle = mActivity.getResources().getStringArray(R.array.music_title);
        //获取榜单类型
        musicType = mActivity.getResources().getStringArray(R.array.music_type);


        for (int i = 0; i < musicType.length; i++) {
            MusicEntity entity = new MusicEntity();
            entity.name = musicTitle[i];
            entity.type = musicType[i];
            mList.add(entity);
            getOnlineMusic(entity.type);
        }


    }

    //获取网络榜单
    public void getOnlineMusic(String type) {
        if (controller == null)
            controller = new MusicController(mActivity, this);
        controller.getTop3Music(100, type, 3);

    }

    @Override
    public void onUpdateView(int reqId, BaseMusicResponse response) {
        super.onUpdateView(reqId, response);

        switch (reqId) {
            case NetConstant.NO_NET_NO_CACHE:
            case NetConstant.NET_ERROR:
                Toast.makeText(mActivity, "网络连接异常，请重试！", Toast.LENGTH_LONG).show();
                break;
            case 100:
                if (response.billboard == null)
                    return;
                for (MusicEntity entity : mList) {
                    if (entity.type.equals(response.billboard.billboard_type)) {
                        entity.billboard = response.billboard;
                        entity.songList = (ArrayList<SongEntity>) response.data;
                        adapter.notifyDataSetChanged();
                        return;
                    }

                }

                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.i("TAG", "权限申请成功！");
        scanMusic();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
