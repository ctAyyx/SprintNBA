package com.ct.sprintnba_demo01.base.adapter.callback;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;

/**
 * Created by ct on 2017/3/10.
 * ==============================
 * 实现RecyclerView 的拖拽 和笔刷效果
 * ==============================
 */

public class RvHelperCallback extends ItemTouchHelper.Callback {
    private BaseRVAdapter adapter;

    public RvHelperCallback(BaseRVAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        //定义拖拽支持的方向
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        //定义滑动支持的方向
        int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        //使用ItemTouchHelper.makeMovementFlags(int, int)创建代表方向的Flag
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    /**
     * onMove()和onSwiped()两个方法被用来通知底层数据改变。所以我们需要先创建一个接口。随后使用链式回调传递事件。
     */
    public interface ItemTouchHelperListener {
        boolean onItemMove(int fromPosition, int toPosition);

        boolean onItemSwipe(int position);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //      adapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //    adapter.onItemSwipe(viewHolder.getAdapterPosition());
    }

    /**
     * 实现这个方法，ItemTouchHelper只能drag而不是swipe（反之亦然）所以必须正确指定是否支持drag。
     * 实现isLongPressDragEnabled()方法返回true去支持长按RecyclerView的item时的drag事件。
     */
    @Override
    public boolean isLongPressDragEnabled() {
        return super.isLongPressDragEnabled();
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return super.isItemViewSwipeEnabled();
    }
}
