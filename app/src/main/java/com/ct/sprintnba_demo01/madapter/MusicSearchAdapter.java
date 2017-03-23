package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.view.View;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.mentity.QuerySongEntity;

import java.util.List;

/**
 * Created by ct on 2017/3/1.
 * ============================
 * 音乐搜索适配器
 * ============================
 * 展示搜索出来的歌曲
 * =============================
 */

public class MusicSearchAdapter extends BaseRVAdapter<QuerySongEntity> {

    private OnItemClickListenerDefualt<QuerySongEntity> mListener;

    public MusicSearchAdapter(Context mContext, List<QuerySongEntity> mList) {
        super(mContext, mList, R.layout.layout_music_list_item_song);
    }

    public void setOnItemCliokListener(OnItemClickListenerDefualt<QuerySongEntity> itemCliokListener) {
        this.mListener = itemCliokListener;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, final QuerySongEntity item) {

        //隐藏图片
        holder.getView(R.id.img_music_item_photo).setVisibility(View.GONE);

        holder.setText(R.id.tv_music_item_title, item.songname);
        holder.setText(R.id.tv_music_item_special, item.artistname);

        holder.setOnClickListener(R.id.img_music_item_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //如果mListener不为空
        if (mListener != null) {
            holder.setOnClickListener(R.id.linear_music_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(position, v, item);
                }
            });
        }


    }




}
