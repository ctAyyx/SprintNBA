package com.ct.sprintnba_demo01.mfragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.ct.sprintnba_demo01.ChooseMMTabActivity;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.fragment.BaseFragment;
import com.ct.sprintnba_demo01.base.fragment.BaseLazyFragment;
import com.ct.sprintnba_demo01.constant.Column_MM;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.VPNewsAdapter;
import com.ct.sprintnba_demo01.mfragment.mmfragment.MMListFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by ct on 2017/1/13.
 */

public class MMFragment extends BaseLazyFragment {

    @BindView(R.id.tab_layout_fragment_mms)
    TabLayout tab_layout;
    @BindView(R.id.vp_fragment_mms)
    ViewPager vp_news;
    @BindView(R.id.img_tab_mms)
    ImageView img;

    private VPNewsAdapter adapter;
    private ArrayList<BaseFragment> mList;
    private String[] titles;//{"校花", "清纯", "气质", "萌女", "壁纸", "非主流", "明星", "美术", "家居", "型男", "萌宠"};
//    private Column_MM[] columnMms = {
//            Column_MM.SCHOOL_BABE,
//            Column_MM.PURE,
//            Column_MM.TEMPERAMENT,
//            Column_MM.LOLITA,
//            Column_MM.WALLPAPER,
//            Column_MM.ALTERNATIVE,
//            Column_MM.STAR,
//            Column_MM.ART,
//            Column_MM.HOME,
//            Column_MM.MAN,
//            Column_MM.PET
//    };

    private Column_MM[] columnMms = {Column_MM.N1,
            Column_MM.N2,
            Column_MM.N3,
            Column_MM.N4,
            Column_MM.N5,
            Column_MM.N6,
            Column_MM.N7,
            Column_MM.N8,
            Column_MM.N9,
            Column_MM.N10,
            Column_MM.N11,
            Column_MM.N12,
            Column_MM.N13,
            Column_MM.N14,
            Column_MM.N15,
            Column_MM.N16,
            Column_MM.N17,
            Column_MM.N18
    };

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_mms);
        titles = mActivity.getResources().getStringArray(R.array.gril_title);
        initData();
        adapter = new VPNewsAdapter(getFragmentManager(), mList, titles);
        vp_news.setAdapter(adapter);
        vp_news.setOffscreenPageLimit(titles.length);
        tab_layout.setupWithViewPager(vp_news);
    }

    /**
     * 初始化每个Fragment的bundle
     */
    private void initData() {
        if (mList == null) {
            mList = new ArrayList<>();
            for (int i = 0; i < titles.length; i++) {
                MMListFragment mmListFragment = new MMListFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(OwnConstant.COLUMN_MM, columnMms[i]);
                mmListFragment.setArguments(bundle);
                mList.add(mmListFragment);

            }
        }
    }

    @OnClick(R.id.img_tab_mms)
    public void onClick() {
        Intent intent = new Intent(mActivity, ChooseMMTabActivity.class);
        startActivity(intent);

    }


}
