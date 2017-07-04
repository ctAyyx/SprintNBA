package com.ct.sprintnba_demo01.mfragment.mmfragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ct.sprintnba_demo01.MMDetaileActivity;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.response.BaseMMResponse;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.constant.Column_MM;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.MMListAdapter;
import com.ct.sprintnba_demo01.mcontroller.MMController;
import com.ct.sprintnba_demo01.mentity.MMEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by ct on 2017/1/18.
 */

public class MMListFragment extends BaseLazyFragment<BaseMMResponse> {
    @BindView(R.id.recycler_mm_list)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_mm_list)
    SwipeRefreshLayout refreshLayout;
    private MMListAdapter adapter;
    private List<MMEntity> mList = new ArrayList<>();
    private MMController controller;

    //    private Column_MM columnMm = Column_MM.SCHOOL_BABE;
    private Column_MM columnMm = Column_MM.N1;
    private static int DEFULT = 20;
    private int start = 0;
    //private int page = 1;


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_mm_list);
        Log.i("TAG", "MMListFragment is create");
        Bundle bundle = getArguments();
        if (bundle != null) {
            columnMm = (Column_MM) bundle.getSerializable(OwnConstant.COLUMN_MM);
        }

        adapter = new MMListAdapter(mActivity, mList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        controller = new MMController(mActivity, this);
        refreshLayout.setRefreshing(true);
        controller.getMMphoto(100, start, DEFULT, columnMm.getCategory());


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // page = 1;
                start = 0;
                //controller.getMMphoto(100, page, columnMm.getCategory());
                controller.getMMphoto(100, start, DEFULT, columnMm.getCategory());
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                int[] lastVisibleItemPosition = ((StaggeredGridLayoutManager) layoutManager)
                        .findLastVisibleItemPositions(null);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                if (visibleItemCount > 0 &&
                        (newState == RecyclerView.SCROLL_STATE_IDLE) &&
                        ((lastVisibleItemPosition[0] >= totalItemCount - 1) ||
                                (lastVisibleItemPosition[1] >= totalItemCount - 1))) {
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    // page += 1;
                    start += DEFULT;
                    // controller.getMMphoto(101, page, columnMm.getCategory());
                    controller.getMMphoto(101, start, DEFULT, columnMm.getCategory());
                }


            }
        });

        adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, View itemView) {

                Intent intent = new Intent(mActivity, MMDetaileActivity.class);
                // intent.putExtra("img_URL", mList.get(postion).big_image);
                //intent.putExtra("NAME", columnMm.getCategory());
                //  startActivity(intent);

                intent.putExtra("img_URL", mList.get(postion).picUrl);
                intent.putExtra("NAME", mList.get(postion).title);
                sceneTransition(intent, itemView);
            }
        });

    }

    private void sceneTransition(Intent intent, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, view, "imgDetail");
            ActivityCompat.startActivity(mActivity, intent, options.toBundle());
        } else {
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity, view, "imgDetail");
            ActivityCompat.startActivity(mActivity, intent, compat.toBundle());
        }
    }


    @Override
    public void onUpdateView(int reqId, BaseMMResponse response) {
        super.onUpdateView(reqId, response);
        Log.i("TAG", "reqId" + reqId + "  response" + response);
        if (refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
        switch (reqId) {
            case NetConstant.NO_NET_NO_CACHE:
            case NetConstant.NET_ERROR:
                Toast.makeText(mActivity, "网络连接异常，请重试！", Toast.LENGTH_LONG).show();
                break;
            case 100:
                mList.clear();
                ArrayList<MMEntity> newData = (ArrayList<MMEntity>) response.data;
                adapter.addAll(newData);
                break;
            case 101:
                ArrayList<MMEntity> newData2 = (ArrayList<MMEntity>) response.data;
                adapter.addAll(newData2);
                break;
        }
    }
}
