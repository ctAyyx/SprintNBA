package com.ct.sprintnba_demo01.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by ct on 2017/1/12.
 * ========================================
 * listView adapter 基类
 * ========================================
 */

public abstract class BaseLVAdapter<T> extends BaseAdapter implements DataHelper<T> {
    protected Context mContext;
    protected List<T> mList;
    protected int[] layoutIds;
    protected LayoutInflater mInflater;

    protected BaseLVHolder holder = new BaseLVHolder();

    public BaseLVAdapter(Context mContext, List<T> mList, int...layoutIds) {
        this.mContext = mContext;
        this.mList = mList;
        this.layoutIds = layoutIds;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public BaseLVAdapter(Context mContext, List<T> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList == null ? null : mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int layoutId = getViewCheckLayoutId(position);
        holder = holder.get(mContext, position, convertView, parent, layoutId);
        conver(holder, position, mList.get(position));
        return holder.getConvertView();
    }

    private int getViewCheckLayoutId(int position) {
        int layoutId;
        if (layoutIds == null || layoutIds.length == 0) {
            layoutId = getLayoutId(position, mList.get(position));
        } else {
            layoutId = layoutIds[getLayoutIndex(position, mList.get(position))];
        }
        return layoutId;
    }

    /**
     * 构造函数中没有传入LayoutId,重写此方法完成LayoutId的赋值
     *
     * @param position
     * @param t
     * @return layoutId
     */
    public int getLayoutId(int position, T t) {
        return 0;
    }

    /**
     * 指定Item布局样式的索引。默认为第一个
     *
     * @param position
     * @param t
     * @return layoutId
     */
    public int getLayoutIndex(int position, T t) {
        return 0;
    }

    public abstract void conver(BaseLVHolder holder, int position, T t);

    @Override
    public boolean addAll(List<T> list) {
        boolean result = mList.addAll(list);
        notifyDataSetChanged();
        return result;
    }

    @Override
    public boolean addAll(int position, List<T> list) {
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
