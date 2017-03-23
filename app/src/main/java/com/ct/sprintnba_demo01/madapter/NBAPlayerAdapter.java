package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.mentity.NBAPlayer;
import com.ct.sprintnba_demo01.mview.indexablelistview.IndexableAdapter;

/**
 * Created by  on 2017/3/8.ct
 */

public class NBAPlayerAdapter extends IndexableAdapter<NBAPlayer> {
    private Context context;

    public NBAPlayerAdapter(Context context) {
        this.context = context;
    }

    @Override
    protected TextView onCreateTitleViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_nba_player_title, parent, false);
        return (TextView) view;
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_nba_player, parent, false);
        return new PlayersHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, NBAPlayer cityEntity) {
        PlayersHolder playersHolder = (PlayersHolder) holder;
        playersHolder.tvName.setText(cityEntity.getName());
        Glide.with(context).load(cityEntity.icon).into(playersHolder.ivHead);
    }

    class PlayersHolder extends IndexableAdapter.ViewHolder {
        TextView tvName;
        ImageView ivHead;

        public PlayersHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_item_nbaplayer);
            ivHead = (ImageView) view.findViewById(R.id.img_item_nbaplayer);
        }
    }
}
