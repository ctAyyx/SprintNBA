package com.ct.sprintnba_demo01.mfragment.newfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.ShowBNAActivity;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.listener.OnRvItemTouchListener;
import com.ct.sprintnba_demo01.base.adapter.listener.OnRvItemTouchListenerAdapter;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.constant.Column_NBA;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.NewsListAdapter;
import com.ct.sprintnba_demo01.mcontroller.NewsController;
import com.ct.sprintnba_demo01.mentity.NewsEntity;
import com.ct.sprintnba_demo01.mentity.NewsIndexEntity;
import com.ct.sprintnba_demo01.mutils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by ct on 2017/1/16.
 * ====================================
 * 今日头条
 * ====================================
 */

public class NewsListFragment extends BaseLazyFragment<BaseNBAResponse> {


    @BindView(R.id.recycler_news_list)
    RecyclerView recycler;
    @BindView(R.id.swip_news_list)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsController controller;
    private Column_NBA columnNba = Column_NBA.BANNER;

    private List<NewsIndexEntity> newsIndexs = new ArrayList<>();
    private List<NewsEntity> newsList = new ArrayList<>();
    private int index;

    private NewsListAdapter adapter;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_news_list);
        Log.i("TAG", "NewsListFragment is create");
        Bundle bundle = getArguments();
        if (bundle != null) {
            columnNba = (Column_NBA) bundle.getSerializable(OwnConstant.COLUMN_NBA);
        }

        initEvents();

        controller = new NewsController(mActivity, this);
        swipeRefreshLayout.setRefreshing(true);
        controller.getNewsIndexs(100, columnNba.getColumn());


    }

    private void initEvents() {
        if (adapter == null)
            adapter = new NewsListAdapter(mActivity, newsList);
        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(mActivity));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.addItemDecoration(new SpaceItemDecoration(mActivity, 5));
        recycler.setAdapter(adapter);


        adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, View itemView) {
                Intent intent = new Intent(mActivity, ShowBNAActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(OwnConstant.COLUMN_NBA, columnNba);
                bundle.putString("articleId", newsList.get(postion).newsId);
                bundle.putString("title", newsList.get(postion).title);
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controller.getNewsIndexs(100, columnNba.getColumn());
            }
        });

        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int positon = ((LinearLayoutManager) manager).findLastVisibleItemPosition();
                if (positon == recyclerView.getAdapter().getItemCount() - 1) {
                    controller.getNewsList(101, columnNba.getColumn(), getArticleIds(index, OwnConstant.DEFAULTLOAD));
                }
            }
        });
    }


    @Override
    public void onUpdateView(int reqId, BaseNBAResponse response) {
        super.onUpdateView(reqId, response);

        if (swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
        switch (reqId) {
            case NetConstant.NO_NET_NO_CACHE:
            case NetConstant.NET_ERROR:
                Toast.makeText(mActivity, "网络连接异常，请重试！", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "reqId" + reqId + "  response" + response);
                break;

            case 100:
                newsIndexs = (List<NewsIndexEntity>) response.data;
                index = 0;
                newsList.clear();
                controller.getNewsList(101, columnNba.getColumn(), getArticleIds(index, OwnConstant.DEFAULTLOAD));

                break;
            case 101:
                Map<String, NewsEntity> map = (Map<String, NewsEntity>) response.data;
                ArrayList<NewsEntity> newList = new ArrayList<>(map.values());
                index += newList.size();
                adapter.addAll(newList);
                break;

        }
    }

    private String getArticleIds(int index, int count) {
        String articleIds = "";
        if (index >= 0 && index < newsIndexs.size()) {
            for (int i = index; i < (index + count); i++) {
                if (i < newsIndexs.size())
                    articleIds = articleIds + newsIndexs.get(i).id + ",";
            }
        }
        return "".equals(articleIds) ? "" : articleIds.substring(0, articleIds.length() - 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isVisibleToUser)
            JCVideoPlayer.releaseAllVideos();
        super.setUserVisibleHint(isVisibleToUser);
    }
}
