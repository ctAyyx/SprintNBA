package com.ct.sprintnba_demo01.base.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.ct.sprintnba_demo01.R;

/**
 * Created by ct on 2017/1/12.
 */

public class BaseLVHolder implements ScrollViewHelper.AbsListView<BaseLVHolder> {
    private SparseArray<View> mViews = new SparseArray<>();
    private SparseArray<View> mConvertViews = new SparseArray<>();

    private View mConvertView;
    protected int mPositon;
    protected int mLayoutId;
    protected Context mContext;

    protected BaseLVHolder() {
    }

    protected BaseLVHolder(Context context, int positon, ViewGroup parent, int layoutId) {
        this.mConvertView = mConvertViews.get(layoutId);
        this.mPositon = positon;
        this.mContext = context;
        this.mLayoutId = layoutId;
        if (mConvertView == null) {
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            mConvertViews.put(layoutId, mConvertView);
            mConvertView.setTag(this);
        }
    }

    public <BVH extends BaseLVHolder> BVH get(Context context, int positon, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null)
            return (BVH) new BaseLVHolder(context, positon, parent, layoutId);
        else {
            BaseLVHolder holder = (BaseLVHolder) convertView.getTag();
            if (holder.mLayoutId != layoutId) {
                return (BVH) new BaseLVHolder(context, positon, parent, layoutId);
            }
            holder.setPosition(positon);
            return (BVH) holder;
        }


    }

    public void setPosition(int position) {
        this.mPositon = position;
    }

    public View getConvertView() {
        return mConvertViews.size() == 0 ? null : mConvertViews.valueAt(0);
    }

    public View getConvertView(int layoutId) {
        return mConvertViews.size() == 0 ? null : mConvertViews.get(layoutId);
    }

    public <V extends View> V getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (V) view;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    @Override
    public BaseLVHolder setText(int viewId, String value) {
        TextView tv = getView(viewId);
        tv.setText(value);
        return this;
    }

    @Override
    public BaseLVHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    @Override
    public BaseLVHolder setTextColorRes(int viewId, int colorRes) {
        TextView tv = getView(viewId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            tv.setTextColor(mContext.getResources().getColor(colorRes, null));
        else
            tv.setTextColor(mContext.getResources().getColor(colorRes));
        return this;
    }

    @Override
    public BaseLVHolder setImageResource(int viewId, int imgResId) {
        ImageView img = getView(viewId);
        img.setImageResource(imgResId);
        return this;
    }

    @Override
    public BaseLVHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    @Override
    public BaseLVHolder setBackgroundColorRes(int viewId, int colorRes) {
        View view = getView(viewId);
        view.setBackgroundResource(colorRes);
        return this;
    }

    @Override
    public BaseLVHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView img = getView(viewId);
        img.setImageDrawable(drawable);
        return this;
    }

    @Override
    public BaseLVHolder setImageDrawableRes(int viewId, int drawableRes) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            drawable = mContext.getResources().getDrawable(drawableRes, null);
        else
            drawable = mContext.getResources().getDrawable(drawableRes);
        return setImageDrawable(viewId, drawable);
    }

    @Override
    public BaseLVHolder setImageUrl(int viewId, String imgUrl) {
        return this;
    }

    @Override
    public BaseLVHolder setImageBitmap(int viewId, Bitmap imgBitmap) {
        ImageView img = getView(viewId);
        img.setImageBitmap(imgBitmap);
        return this;
    }

    @Override
    public BaseLVHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    @Override
    public BaseLVHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        view.setVisibility(visible);
        return this;
    }

    @Override
    public BaseLVHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    @Override
    public BaseLVHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    @Override
    public BaseLVHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    @Override
    public BaseLVHolder setAlpha(int viewId, float value) {
        getView(viewId).setAlpha(value);
        return this;
    }

    @Override
    public BaseLVHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = getView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    @Override
    public BaseLVHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    @Override
    public BaseLVHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
