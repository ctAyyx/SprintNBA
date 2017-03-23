package com.ct.sprintnba_demo01;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.response.QueryMusicResponse;
import com.ct.sprintnba_demo01.madapter.MusicSearchAdapter;
import com.ct.sprintnba_demo01.mcontroller.QueryMusicController;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.QuerySongEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mservice.MusicPlayService;
import com.ct.sprintnba_demo01.music.PlayOnlineMusic;
import com.ct.sprintnba_demo01.music.PlaySearchMusic;
import com.example.searchview_library.SearchALG;
import com.example.searchview_library.SearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 音乐搜索界面
 * ================================
 * created by ct
 * ================================
 */
public class MusicSearchActivity extends BaseActivity<QueryMusicController, QueryMusicResponse> implements SearchView.OnSearchListener {

    @BindView(R.id.search_music)
    SearchView searchView;
    @BindView(R.id.recycler_music_search)
    RecyclerView recyclerView;
    @BindView(R.id.progress_music)
    ProgressBar progressBar;


    private List<String> changedHintDatas;
    private SearchALG searchALG;

    private List<String> hotList;//热搜提示集合
    private List<String> hintList;//搜索提示集合

    private List<QuerySongEntity> mList = new ArrayList<>();
    private List<SongEntity> onlineList = new ArrayList<>();
    private MusicSearchAdapter adapter;

    private MusicPlayService mPlayService;

    @Override
    public int getLayoutId() {
        return R.layout.activity_music_search;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        //初始化搜索栏
        searchALG = new SearchALG(this);
        initHotData();
        searchView.setOnSearchListener(this);

        controller = new QueryMusicController(this, this);

        adapter = new MusicSearchAdapter(this, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                searchView.goneSearch();
            }
        });


        adapter.setOnItemCliokListener(new BaseRVAdapter.OnItemClickListenerDefualt<QuerySongEntity>() {
            @Override
            public void onItemClick(int postion, View itemView, QuerySongEntity querySongEntity) {
                if (mPlayService != null) {
                    playMusic(postion);
                }
            }
        });


        bindService();

    }

    private void playMusic(final int posiition) {
        new PlaySearchMusic(this, mList.get(posiition)) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onSuccess(Music music) {
                mPlayService.play(posiition, music);
                mPlayService.setOnlineMusicList(onlineList);
            }

            @Override
            public void onFail(Exception e) {
                Log.i("TAG", "失败");
            }
        }.execute();
    }


    private void bindService() {
        Intent intent = new Intent(this, MusicPlayService.class);
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
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
     * 创建热搜数据
     * <p>
     * 因为没有接口 所以数据为假数据
     */
    private void initHotData() {
        if (hotList == null)
            hotList = new ArrayList<>();

        hotList.add("好心分手");
        hotList.add("i Like");
        hotList.add("遇");
        hotList.add("逆流成河");
        hotList.add("帝都");

        searchView.setHotNumColumns(2);
        searchView.setHotSearchDatas(hotList);

        //设置保存搜索记录
        searchView.setKeepScreenOn(true);
        //设置最大提示数
        searchView.setMaxHintLines(8);
        //设置最大保存数
        searchView.setMaxHistoryRecordCount(6);


        if (hintList == null)
            hintList = new ArrayList<>();
        //a
        hintList.add("A");
        hintList.add("As");
        hintList.add("爱");
        hintList.add("爱囚");
        hintList.add("爱情转移");

        //t
        hintList.add("听");
        hintList.add("听海");
        hintList.add("听妈妈的话");

        //歌手
        hintList.add("张杰");
        hintList.add("周杰伦");
        hintList.add("莫文蔚");
        hintList.add("李玉刚");


    }


    @Override
    public void onUpdateView(int reqId, QueryMusicResponse response) {
        super.onUpdateView(reqId, response);
        progressBar.setVisibility(View.GONE);

        switch (reqId) {
            case 100:
                if (response.data != null) {
                    mList.clear();
                    mList.addAll(response.data);
                    adapter.notifyDataSetChanged();
                    toSong();
                }

                break;
        }

    }

    /**
     * 将获取的QueryMusicEntiyt 转成SongEntity
     */
    private void toSong() {
        if (mList.size() == 0)
            return;
        onlineList.clear();
        for (QuerySongEntity entity : mList) {
            SongEntity songEntity = new SongEntity();
            songEntity.song_id = entity.songid;
            songEntity.title = entity.songname;
            songEntity.artist_name = entity.artistname;
            songEntity.album_title = entity.songname;

            onlineList.add(songEntity);
        }

    }

    @Override
    public void onSearch(String searchText) {
        if (TextUtils.isEmpty(searchText))
            Toast.makeText(MusicSearchActivity.this, "搜索类容不能为空！", Toast.LENGTH_SHORT).show();
        else {
            controller.getQueryMusic(100, searchText);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRefreshHintList(String changedText) {

        if (changedHintDatas == null) {
            changedHintDatas = new ArrayList<>();
        } else {
            changedHintDatas.clear();
        }
        if (TextUtils.isEmpty(changedText)) {
            return;
        }
        for (int i = 0; i < hintList.size(); i++) {
            String hint_data = hintList.get(i);
            boolean isAdd = searchALG.isAddToHintList(hint_data, changedText);
            if (isAdd) {
                changedHintDatas.add(hintList.get(i));
            }
        }

        /**
         * 根据搜索框文本的变化，动态的改变提示的listView
         */
        searchView.updateHintList(changedHintDatas);
    }


    @Override
    protected void onDestroy() {

        unbindService(mConnection);
        super.onDestroy();
    }
}
