package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mentity.MusicEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;

import java.util.List;

/**
 * Created by ct on 2017/2/13.
 */

public class MusicListAdapter extends BaseRVAdapter<MusicEntity> {

    private OnItemClickListenerDefualt<MusicEntity> onItemClickListener;


    public MusicListAdapter(Context mContext, List<MusicEntity> mList) {
        super(mContext, mList, R.layout.layout_music_list_item_title, R.layout.layout_music_list_item, R.layout.layout_music_list_item_song);

    }

    public void setOnItemClickListener(OnItemClickListenerDefualt<MusicEntity> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, final MusicEntity item) {


        if ("#".equals(item.type))
            holder.setText(R.id.tv_music_list_title, item.name);
        else {
            ImageView img = holder.getView(R.id.img_music_list_photo);
            if (item.billboard != null)
                Glide.with(mContext).load(item.billboard.pic_s260).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(img);
            if (item.songList != null) {
                int[] tvIds = {R.id.tv_music_list_first, R.id.tv_music_list_second, R.id.tv_music_list_third};
                for (int i = 0; i < item.songList.size(); i++) {
                    holder.setText(tvIds[i], (i + 1) + "." + item.songList.get(i).title + " - " + item.songList.get(i).author);
                }
            }

            //设置点击事件
            if (onItemClickListener != null)
                holder.setOnItemViewClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListener.onItemClick(position, v, item);
                    }
                });

        }


    }

    @Override
    public int getLayoutIndex(int position, MusicEntity item) {
        if ("#".equals(item.type))
            return 0;
        return 1;

    }


}
