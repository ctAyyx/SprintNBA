package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.manimation.AnimationHelper;
import com.ct.sprintnba_demo01.mentity.NBARecord;

import java.util.List;

/**
 * Created by ct on 2017/3/7.
 * ============================
 * NBA 球队战绩
 * ============================
 */

public class NBARecordAdapter extends BaseRVAdapter<NBARecord> {
    private OnItemClickListenerDefualt<NBARecord> mListener;
    private AnimationHelper helper;

    public NBARecordAdapter(Context mContext, List<NBARecord> mList) {
        super(mContext, mList, R.layout.layout_item_nbarecord_title, R.layout.layout_item_nbarecord_content);
        helper = new AnimationHelper();
    }

    public void setOnItemClickListener(OnItemClickListenerDefualt<NBARecord> listener) {
        this.mListener = listener;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, final NBARecord item) {

        if (TextUtils.isEmpty(item.title)) {
            ImageView img = holder.getView(R.id.img_item_nbarecord);
            Glide.with(mContext).load(item.team.badge).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).into(img);

            holder.setText(R.id.tv_item_nbarecord_team_name, item.team.name);
            holder.setText(R.id.tv_item_nbarecord_team_win, item.team.wins + "");
            holder.setText(R.id.tv_item_nbarecord_team_defeat, item.team.defeata + "");
            holder.setText(R.id.tv_item_nbarecord_team_winrate, item.team.winRate);
            holder.setText(R.id.tv_item_nbarecord_team_windifference, item.team.winDifference + "");

            LinearLayout linearLayout = holder.getView(R.id.linear_item_nbarecord);
            helper.showItemAnim(linearLayout, position, null);

            if (mListener != null) {
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onItemClick(position, v, item);
                    }
                });
            }


        } else {
            holder.setText(R.id.tv_item_nbarecord_title, item.title);
        }


    }

    @Override
    public int getLayoutIndex(int position, NBARecord item) {
        if (TextUtils.isEmpty(item.title))
            return 1;
        return 0;
    }
}
