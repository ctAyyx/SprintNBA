package com.ct.sprintnba_demo01;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.BaseResponse;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.constant.Column_NBA;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mcontroller.NewsController;
import com.ct.sprintnba_demo01.mentity.NewDetailEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sephiroth.android.library.picasso.Picasso;

/**
 *
 * */
public class ShowBNAActivity extends BaseActivity<NewsController, BaseNBAResponse> {


    @BindView(R.id.img_show_bna)
    ImageView img_show;
    @BindView(R.id.tool_bar_show_nba)
    Toolbar tool_bar_show;
    @BindView(R.id.tv_show_nba)
    TextView tv_show;

    private Column_NBA columnNba = Column_NBA.BANNER;
    private String newId;
    private String title;

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_bna;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) tool_bar_show.getLayoutParams();
        params.topMargin = DeviceUtils.getStatusBarHeight(this);
        tool_bar_show.setLayoutParams(params);

        if (bundle != null) {
            controller = new NewsController(this, this);
            initToolBar(tool_bar_show);
            columnNba = (Column_NBA) bundle.getSerializable(OwnConstant.COLUMN_NBA);
            newId = bundle.getString("articleId");
            title = bundle.getString("title");
            setTitle(title);
            controller.getDetail(100, columnNba.getColumn(), newId);

        }


    }


    @Override
    public void onUpdateView(int reqId, BaseNBAResponse response) {
        super.onUpdateView(reqId, response);
        switch (reqId) {
            case 100:
                NewDetailEntity entity = (NewDetailEntity) response.data;
                //   Glide.with(this).load(entity.imgurl).into(img_show);
                Picasso.with(this).load(entity.imgurl).into(img_show);
                tv_show.setText(setContent(entity));
                break;
        }
    }

    private String setContent(NewDetailEntity entity) {
        String info = "";
        if (entity == null || entity.content == null)
            return info;
        for (NewDetailEntity.Describe describe : entity.content) {
            if (!TextUtils.isEmpty(describe.info))
                info = info + "     " + describe.info + "\r\n";
        }
        return info;
    }
}
