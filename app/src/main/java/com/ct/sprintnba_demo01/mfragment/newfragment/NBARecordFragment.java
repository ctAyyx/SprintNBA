package com.ct.sprintnba_demo01.mfragment.newfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.WebActivity;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.NBARecordAdapter;
import com.ct.sprintnba_demo01.mentity.NBARecord;
import com.ct.sprintnba_demo01.mutils.SpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/3/7.
 * =================================
 * 球队战绩
 * =================================
 */

public class NBARecordFragment extends BaseLazyFragment {

    @BindView(R.id.swipe_nba_record)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_nba_record)
    RecyclerView recyclerView;

    private List<NBARecord> mList;
    private NBARecordAdapter adapter;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_nba_record);

        mList = new ArrayList<>();
        adapter = new NBARecordAdapter(mActivity, mList);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpaceItemDecoration(mActivity, 6));

        adapter.setOnItemClickListener(new BaseRVAdapter.OnItemClickListenerDefualt<NBARecord>() {
            @Override
            public void onItemClick(int postion, View itemView, NBARecord nbaRecord) {
                startWeb(nbaRecord);
            }
        });

        //获取数据
        getData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    private void startWeb(NBARecord nbaRecord) {
        Intent intent = new Intent(mActivity, WebActivity.class);
        intent.putExtra(OwnConstant.WEB_TITLE, nbaRecord.team.name);
        intent.putExtra(OwnConstant.WEB_URL, nbaRecord.team.detailUrl);
        mActivity.startActivity(intent);

    }


    private void getData() {
        Call<ResponseBody> call = NetService.getApiService(mActivity, NetConstant.HOST_NBA).getTeamRecord();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
                if (response != null && response.body() != null) {
                    try {
                        String dataJson = response.body().string();
                        json(dataJson);

                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        });

    }

    private void json(String json) {
        if (TextUtils.isEmpty(json))
            return;

        try {
            JSONObject obj = new JSONObject(json);
            obj.getInt("code");
            obj.getString("version");
            JSONArray ary = obj.getJSONArray("data");
            if (ary != null) {
                mList.clear();
                for (int i = 0; i < ary.length(); i++) {
                    //创建NBARecord实体类
                    NBARecord record = new NBARecord();
                    //获取实体类Json数据
                    JSONObject recordJson = ary.getJSONObject(i);
                    if (recordJson == null)
                        return;
                    record.type = recordJson.getInt("type");
                    record.title = recordJson.getString("title");
                    record.head = getHead(recordJson.getJSONArray("head"));
                    record.rows = getTeam(recordJson.getJSONArray("rows"));
                    mList.add(record);

                    if (record.rows != null && record.rows.size() != 0) {
                        //c重新封装数据
                        for (NBARecord.Team team : record.rows) {
                            NBARecord recod = new NBARecord();
                            recod.team = team;
                            mList.add(recod);
                        }

                    }
                }

                adapter.notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private ArrayList<String> getHead(JSONArray array) {
        ArrayList<String> list = new ArrayList<>();
        if (array == null || array.length() == 0)
            return list;

        for (int i = 0; i < array.length(); i++) {
            try {
                list.add(array.getString(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;

    }


    private ArrayList<NBARecord.Team> getTeam(JSONArray array) {
        ArrayList<NBARecord.Team> list = new ArrayList<>();
        if (array == null || array.length() == 0)
            return list;

        for (int i = 0; i < array.length(); i++) {
            try {
                JSONArray teamArray = array.getJSONArray(i);

                NBARecord.Team team = new NBARecord.Team();
                JSONObject teamObj = teamArray.getJSONObject(0);

                team.teamId = teamObj.getInt("teamId");
                team.name = teamObj.getString("name");
                team.badge = teamObj.getString("badge");
                team.serial = teamObj.getString("serial");
                team.color = teamObj.getString("color");
                team.detailUrl = teamObj.getString("detailUrl");

                team.wins = teamArray.getInt(1);
                team.defeata = teamArray.getInt(2);
                team.winRate = teamArray.getString(3);
                team.winDifference = teamArray.getDouble(4);

                list.add(team);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}
