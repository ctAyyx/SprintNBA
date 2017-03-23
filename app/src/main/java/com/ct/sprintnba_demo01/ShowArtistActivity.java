package com.ct.sprintnba_demo01;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.response.ArtistResponse;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mcontroller.ArtistController;

import butterknife.BindView;
import it.sephiroth.android.library.picasso.Picasso;


public class ShowArtistActivity extends BaseActivity<ArtistController, ArtistResponse> {


    @BindView(R.id.img_show_artist)
    ImageView img_artist;
    @BindView(R.id.tool_bar_show_artist)
    Toolbar toolbar;
    @BindView(R.id.tv_show_artist)
    TextView tv_artist;

    @Override
    public int getLayoutId() {
        return R.layout.activity_show_artist;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        params.topMargin = DeviceUtils.getStatusBarHeight(this);
        toolbar.setLayoutParams(params);


        if (bundle != null) {
            controller = new ArtistController(this, this);
            initToolBar(toolbar);
            String tingId = bundle.getString(OwnConstant.MUSIC_ARTIST_ID);
            String name = bundle.getString(OwnConstant.MUSIC_ARTIST_NAME);
            controller.getArtist(100, tingId);

            setTitle(TextUtils.isEmpty(name) ? "未知" : name);
        }


    }


    @Override
    public void onUpdateView(int reqId, ArtistResponse response) {
        super.onUpdateView(reqId, response);

        switch (reqId) {
            case 100:
                //  Glide.with(this).load(response.avatar_s500).into(img_artist);
                Picasso.with(this).load(response.avatar_s500).into(img_artist);
                break;
        }


    }
}
