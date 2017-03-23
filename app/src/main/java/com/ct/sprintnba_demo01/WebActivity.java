package com.ct.sprintnba_demo01;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mview.BrowserLayout;

import butterknife.BindView;

public class WebActivity extends BaseActivity implements BrowserLayout.OnReceiveTitleListener {

    @BindView(R.id.browser_layout)
    BrowserLayout mBrowserLayout;

    private String mWebTitle;
    private String mWebUrl;
    private boolean isShowBottomBar = true;


    @Override
    public int getLayoutId() {
        return R.layout.activity_web;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        Intent intent = getIntent();

        if (intent == null) {
            setTitle("未知");
            return;
        }

        mWebTitle = intent.getStringExtra(OwnConstant.WEB_TITLE);
        mWebUrl = intent.getStringExtra(OwnConstant.WEB_URL);
        isShowBottomBar = intent.getBooleanExtra(OwnConstant.WEB_SHOW_BOTTOM_BAR, true);

        if (!TextUtils.isEmpty(mWebTitle))
            setTitle(mWebTitle);
        else
            setTitle("未知");


        if (isShowBottomBar)
            mBrowserLayout.visibleBottomBar();
        else
            mBrowserLayout.goneBottomBar();

        mBrowserLayout.setOnReceiveTitleListener(this);

        if (!TextUtils.isEmpty(mWebUrl))
            mBrowserLayout.loadUrl(mWebUrl);
        else
            Toast.makeText(this, "获取URl失败", Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onPause() {
        if (mBrowserLayout.getWebView() != null) {
            mBrowserLayout.getWebView().onPause();
            mBrowserLayout.getWebView().reload();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mBrowserLayout.getWebView() != null) {
            mBrowserLayout.getWebView().removeAllViews();
            mBrowserLayout.getWebView().destroy();
        }

        super.onDestroy();
    }

    @Override
    public void onReceive(String title) {
        if (TextUtils.isEmpty(mWebTitle))
            setTitle(title);
    }

    @Override
    public void onPageFinished() {

    }
}
