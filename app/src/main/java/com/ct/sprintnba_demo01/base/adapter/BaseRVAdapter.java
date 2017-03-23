package com.ct.sprintnba_demo01.base.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by ct on 2017/1/17.
 * ====================================
 * RecyclerView 适配器基类
 * ====================================
 */

public abstract class BaseRVAdapter<T> extends RecyclerView.Adapter<BaseRVHolder> implements DataHelper<T> {

    public static final int TYPE_HEADER = -1;
    public static final int TYPE_FOOTER = -2;

    private View mHeaderView;
    private View mFooterView;

    private int headerViewId = -1;
    private int footerViewId = -1;

    protected Context mContext;
    protected List<T> mList;
    protected int[] layoutIds;
    protected LayoutInflater mInflater;

    private SparseArray<View> mContentViews = new SparseArray<>();

    public BaseRVAdapter(Context mContext, List<T> mList, int... layoutIds) {
        this.mContext = mContext;
        this.mList = mList;
        this.layoutIds = layoutIds;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public BaseRVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mHeaderView != null && viewType == TYPE_HEADER)
            return new BaseRVHolder(mContext, headerViewId, mInflater.inflate(headerViewId, parent, false));
        if (mFooterView != null && viewType == TYPE_FOOTER)
            return new BaseRVHolder(mContext, footerViewId, mInflater.inflate(footerViewId, parent, false));
        if (viewType < 0 || viewType > layoutIds.length)
            throw new ArrayIndexOutOfBoundsException("layoutIndex ");
        if (layoutIds.length == 0)
            throw new IllegalArgumentException("not layoutId");
        int layoutId = layoutIds[viewType];
        View view = mContentViews.get(layoutId);
        if (view == null)
            view = mInflater.inflate(layoutId, parent, false);
        BaseRVHolder holder = (BaseRVHolder) view.getTag();
        if (holder == null || holder.getLayoutId() != layoutId)
            holder = new BaseRVHolder(mContext, layoutId, view);
        return holder;


    }

    @Override
    public void onBindViewHolder(BaseRVHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER)
            return;
        if (getItemViewType(position) == TYPE_FOOTER)
            return;
        position = getPosition(position);
        T item = mList.get(position);
        onBindData(holder, position, item);

    }


    @Override
    public int getItemCount() {
        if (mHeaderView == null && mFooterView == null)
            return mList == null ? 0 : mList.size();
        if (mHeaderView != null && mFooterView != null)
            return mList == null ? 2 : mList.size() + 2;
        return mList == null ? 1 : mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mHeaderView != null)
            return TYPE_HEADER;
        if (position == getItemCount() - 1 && mFooterView != null)
            return TYPE_FOOTER;
        position = getPosition(position);
        return getLayoutIndex(position, mList.get(position));
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) manager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

    }

    /**
     * 处理   StaggeredGridLayoutManager
     *
     * @param holder
     */
    @Override
    public void onViewAttachedToWindow(BaseRVHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    private int getPosition(int position) {
        if (mHeaderView != null || mFooterView != null)
            position = position - 1;
        return position;
    }

    /**
     * 指定item布局样式在layoutIds的索引。默认为第一个
     *
     * @param position
     * @param item
     * @return
     */
    public int getLayoutIndex(int position, T item) {
        return 0;
    }

    /****
     * 设置头部
     *
     * @param headerViewId
     */
    public View setHeaderView(int headerViewId) {
        mHeaderView = mInflater.inflate(headerViewId, null);
        this.headerViewId = headerViewId;
        notifyItemInserted(0);
        return mHeaderView;
    }

    /**
     * 设置底部
     *
     * @param footerViewId
     */
    public View setFooterView(int footerViewId) {
        mFooterView = mInflater.inflate(footerViewId, null);
        this.footerViewId = footerViewId;
        notifyItemInserted(mList.size());
        return mFooterView;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public View getFooterView() {
        return mFooterView;
    }


    public interface OnItemClickListener {
        void onItemClick(int postion, View itemView);
    }

    public interface OnItemClickListenerDefualt<T> {
        /**
         * Item点击事件
         *
         * @param postion  Item的位置
         * @param itemView 点击的item
         * @param t        Item内容
         */
        void onItemClick(int postion, View itemView, T t);
    }

    public abstract void onBindData(BaseRVHolder holder, int position, T item);

    @Override
    public boolean addAll(List<T> list) {
        boolean result = mList.addAll(list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean addAll(int position, List list) {
        boolean result = mList.addAll(position, list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public void add(T data) {
        mList.add(data);
        notifyDataSetChanged();
    }

    @Override
    public void add(int position, T data) {
        mList.add(position, data);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    @Override
    public boolean contains(T data) {
        return mList.contains(data);
    }

    @Override
    public T getData(int index) {
        return mList.get(index);
    }

    @Override
    public void modify(T oldData, T newData) {
        modify(mList.indexOf(oldData), newData);
    }

    @Override
    public void modify(int index, T newData) {
        mList.set(index, newData);
        notifyDataSetChanged();
    }

    @Override
    public boolean remove(T data) {
        boolean result = mList.remove(data);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public T remove(int index) {
        T t = mList.remove(index);
        notifyDataSetChanged();
        return t;
    }
}
