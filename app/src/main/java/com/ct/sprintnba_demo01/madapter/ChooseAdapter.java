package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.constant.Column_MM;
import com.ct.sprintnba_demo01.madapter.callback.OnItemMoveListener;
import com.ct.sprintnba_demo01.mentity.TabEntity;

import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2017/3/13.
 */

public class ChooseAdapter extends BaseRVAdapter<TabEntity<Column_MM>> implements OnItemMoveListener {

    public ChooseAdapter(Context mContext, List<TabEntity<Column_MM>> mList) {
        super(mContext, mList, R.layout.layout_item_choose_tab);
    }

    @Override
    public void onBindData(BaseRVHolder holder, int position, TabEntity<Column_MM> item) {
        holder.setText(R.id.tv_choose_tab, item.title);
        if (position == 0) {
            //表示第一个Item 这里我们默认第一个不能移动 设置其背景色为灰色
            if (holder != null && holder.itemView != null && holder.itemView instanceof CardView) {
                ((CardView) holder.itemView).setCardBackgroundColor(Color.parseColor("#44585858"));
                ((CardView) holder.itemView).setCardElevation(0f);
                ((CardView) holder.itemView).setMaxCardElevation(0f);
            }
        }

    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);

    }
}
