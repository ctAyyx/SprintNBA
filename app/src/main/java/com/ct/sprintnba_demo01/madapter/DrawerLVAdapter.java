package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.view.View;

import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseLVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseLVHolder;
import com.ct.sprintnba_demo01.mentity.DrawerEntity;
import com.ct.sprintnba_demo01.mutils.FileUtils;

import java.util.List;

/**
 * Created by ct on 2017/1/12.
 * =======================
 * 侧滑栏选项adapter
 * =======================
 */

public class DrawerLVAdapter extends BaseLVAdapter<DrawerEntity> {

    private OnItemClickListener mListener;

    public DrawerLVAdapter(Context mContext, List<DrawerEntity> mList) {
        super(mContext, mList, R.layout.layout_item_drawer, R.layout.layout_item_drawer_title);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public int getLayoutIndex(int position, DrawerEntity drawerEntity) {
        if (drawerEntity.tag == null)
            return 1;
        return 0;
    }

    @Override
    public void conver(BaseLVHolder holder, final int position, final DrawerEntity drawerEntity) {

        if (drawerEntity.tag != null) {
            if (drawerEntity.tag == DrawerEntity.DrawerType.CACHE) {
                holder.getView(R.id.tv_item_drawer_cache).setVisibility(View.VISIBLE);
                holder.setText(R.id.tv_item_drawer_cache, FileUtils.getCacheSize(mContext));
            } else
                holder.getView(R.id.tv_item_drawer_cache).setVisibility(View.GONE);

            holder.setImageResource(R.id.img_item_drawer, drawerEntity.getImgId())
                    .setText(R.id.tv_item_drawer, drawerEntity.getText());

            //设置点击事件
            if (mListener != null) {
                holder.setOnClickListener(R.id.linear_item_drawer, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onClick(drawerEntity.getPosition(), v, drawerEntity);
                    }
                });
            }

        } else {
            holder.setText(R.id.tv_item_drawer_title, drawerEntity.getText());
        }

    }

    public interface OnItemClickListener {
        void onClick(int position, View view, DrawerEntity entity);
    }

}
