package com.ct.sprintnba_demo01.madapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.mentity.MMEntity;

import java.util.List;
import java.util.Random;

import it.sephiroth.android.library.picasso.Picasso;

/**
 * Created by ct on 2017/1/18.
 */

public class MMListAdapter extends BaseRVAdapter<MMEntity> {

    private OnItemClickListener listener;

    public MMListAdapter(Context mContext, List<MMEntity> mList) {
        super(mContext, mList, R.layout.layout_mm_list_item);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, MMEntity item) {
        final CardView cardView = holder.getView(R.id.card_view_mm);
        final ImageView img1 = holder.getView(R.id.img_mm_list_item);
        final ViewGroup.LayoutParams params = cardView.getLayoutParams();


       /* params.width = DeviceUtils.deviceWidth(mContext) / 2;
        params.height = getRandomHeight();
        cardView.setLayoutParams(params);*/
      //  Glide.with(mContext).load(item.small_image).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.ALL).into(img1);
       Picasso.with(mContext).load(item.mid_image).placeholder(R.drawable.bg01).into(img1);
        if (listener != null)
            holder.setOnItemViewClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, v);
                }
            });
    }

    private int getRandomHeight() {
        int height;
        height = (int) (DeviceUtils.deviceWidth(mContext) / 2 * (Math.random() + 0.5f));
        return height;
    }
}
