package com.ct.sprintnba_demo01.mfragment.newfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.WebActivity;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.NBAPlayerAdapter;
import com.ct.sprintnba_demo01.mcontroller.NewsController;
import com.ct.sprintnba_demo01.mentity.NBAPlayer;
import com.ct.sprintnba_demo01.mview.indexablelistview.IndexEntity;
import com.ct.sprintnba_demo01.mview.indexablelistview.IndexableStickyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ct on 2017/3/8.
 * <p>
 * ==============================
 * NBA 球员
 * ==============================
 */

public class NBAPlayerFragment extends BaseLazyFragment<BaseNBAResponse> {

    @BindView(R.id.search_nba_player)
    SearchView searchView;
    @BindView(R.id.index_list_nba_player)
    IndexableStickyListView indexView;

    private List<NBAPlayer> mList = new ArrayList<>();
    private NewsController controller;
    private NBAPlayerAdapter adapter;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_nba_player);

        adapter = new NBAPlayerAdapter(mActivity);
        indexView.setAdapter(adapter);

        controller = new NewsController(mActivity, this);
        controller.getNBAPlayer(100);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 委托处理搜索
                indexView.searchTextChange(newText);
                return true;
            }
        });


        indexView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {
            @Override
            public void onItemClick(View v, IndexEntity indexEntity) {
                if (indexEntity instanceof NBAPlayer) {
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra(OwnConstant.WEB_URL, NetConstant.HOST_NBA_PLAYER + ((NBAPlayer) indexEntity).id);
                    intent.putExtra(OwnConstant.WEB_TITLE, indexEntity.getName());
                    mActivity.startActivity(intent);
                }
            }
        });


    }


    @Override
    public void onUpdateView(int reqId, BaseNBAResponse response) {
        super.onUpdateView(reqId, response);

        switch (reqId) {
            case 100:
                if (response.data == null)
                    return;
                List<NBAPlayer> newList = (List<NBAPlayer>) response.data;
                mList.clear();
                mList.addAll(newList);
                indexView.bindDatas(mList);

                break;
        }
    }
}
