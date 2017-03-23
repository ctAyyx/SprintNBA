package com.ct.sprintnba_demo01.mfragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.IView;
import com.ct.sprintnba_demo01.base.fragment.BaseFragment;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.BaseResponse;
import com.ct.sprintnba_demo01.constant.Column_NBA;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.VPNewsAdapter;
import com.ct.sprintnba_demo01.mcontroller.NewsController;
import com.ct.sprintnba_demo01.mentity.NewsIndexEntity;
import com.ct.sprintnba_demo01.mfragment.newfragment.NBAPlayerFragment;
import com.ct.sprintnba_demo01.mfragment.newfragment.NBARecordFragment;
import com.ct.sprintnba_demo01.mfragment.newfragment.NewsListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ct on 2017/1/13.
 * ===============================
 * 新闻界面
 * (TabLayout + ViewPager)
 * ===============================
 */

public class NewsFragment extends BaseLazyFragment {

    @BindView(R.id.tab_layout_fragment_news)
    TabLayout tab_layout;
    @BindView(R.id.vp_fragment_news)
    ViewPager vp_news;
    private VPNewsAdapter adapter;
    private ArrayList<BaseFragment> mList;
    private String[] titles = {"今日头条", "视频集锦", "球队战绩", "所有球员"};
    private Column_NBA[] columnNbas = {Column_NBA.BANNER, Column_NBA.VIDEO};

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_news);

        if (mList == null) {
            mList = new ArrayList<>();
            for (String title : titles) {
                if (title.equals("今日头条")) {
                    NewsListFragment newListFragment = new NewsListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OwnConstant.COLUMN_NBA, Column_NBA.BANNER);
                    newListFragment.setArguments(bundle);
                    mList.add(newListFragment);
                }

                if (title.equals("视频集锦")) {
                    NewsListFragment newListFragment = new NewsListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(OwnConstant.COLUMN_NBA, Column_NBA.VIDEO);
                    newListFragment.setArguments(bundle);
                    mList.add(newListFragment);
                }
                if (title.equals("球队战绩")) {
                    NBARecordFragment fragment = new NBARecordFragment();
                    mList.add(fragment);
                }

                if (title.equals("所有球员")) {
                    NBAPlayerFragment fragment = new NBAPlayerFragment();
                    mList.add(fragment);
                }


            }
        }
        adapter = new VPNewsAdapter(getFragmentManager(), mList, titles);
        vp_news.setAdapter(adapter);
        vp_news.setOffscreenPageLimit(titles.length);
        tab_layout.setupWithViewPager(vp_news);

    }


}
