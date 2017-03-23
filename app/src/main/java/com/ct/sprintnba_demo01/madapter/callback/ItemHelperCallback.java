package com.ct.sprintnba_demo01.madapter.callback;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.TextView;

import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;

/**
 * Created by ct on 2017/3/13.
 */

public class ItemHelperCallback extends ItemTouchHelper.Callback {
    private OnItemMoveListener mListener;

    public ItemHelperCallback(OnItemMoveListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int flags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(flags, 0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (mListener != null)
            mListener.onMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {

        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null && viewHolder.itemView != null && viewHolder.itemView instanceof CardView) {
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#44585858"));
            ((CardView) viewHolder.itemView).setCardElevation(0f);
            ((CardView) viewHolder.itemView).setMaxCardElevation(0f);
        }
        super.onSelectedChanged(viewHolder, actionState);


    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if (viewHolder != null && viewHolder.itemView != null && viewHolder.itemView instanceof CardView) {
            ((CardView) viewHolder.itemView).setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            ((CardView) viewHolder.itemView).setCardElevation(3f);
            ((CardView) viewHolder.itemView).setMaxCardElevation(5f);
        }

    }
}
