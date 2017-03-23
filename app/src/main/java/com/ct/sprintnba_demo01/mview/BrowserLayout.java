package com.ct.sprintnba_demo01.mview;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ct.sprintnba_demo01.R;

/**
 * Created by ct on 2017/3/6.
 * <p>
 * ===========================
 * 浏览器布局
 * ===========================
 */

public class BrowserLayout extends LinearLayout implements View.OnClickListener {
    private Context mContext;
    private WebView mWebView;
    private View mBrowserControllerView;

    private ImageButton mGoBackbtn;          //返回上级界面
    private ImageButton mGoForwardBtn;      //进入下一个页面
    private ImageButton mGoBrowserBtn;      //用浏览器查看
    private ImageButton mRefreshBtn;         //刷新

    private int mBarHeight = 5;
    private ProgressBar mProgressBar;
    private boolean isOverrideUrlLoading = true;

    private String mLoadUrl;
    private OnReceiveTitleListener mListener;


    public BrowserLayout(Context context) {
        super(context);
        init(context);
    }

    public BrowserLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BrowserLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);

        //初始化网页加载进度条
        mProgressBar = (ProgressBar) LayoutInflater.from(context).inflate(R.layout.layout_browser_progress, null);
        mProgressBar.setMax(100);
        mProgressBar.setProgress(0);

        addView(mProgressBar, LinearLayout.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, mBarHeight, getResources().getDisplayMetrics()));

        //创建一个WebView
        mWebView = new WebView(context);
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setBuiltInZoomControls(false);//是否支持手势缩放
        mWebView.getSettings().setSupportMultipleWindows(true);//是否支持多窗口
        mWebView.getSettings().setUseWideViewPort(true);//是否支持HTML的一些标记
        mWebView.getSettings().setLoadWithOverviewMode(true);//适应屏幕
        mWebView.getSettings().setSupportZoom(false);//是否支持缩放

        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        mWebView.getSettings().setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1);
        addView(mWebView, lps);

        mWebView.setWebChromeClient(new AppCacheWebChromeClient());//判断页面加载过程
        mWebView.setWebViewClient(new MonitorWebClient());//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开

        //初始化浏览控制栏
        mBrowserControllerView = LayoutInflater.from(context).inflate(R.layout.layout_browser_controller, null);
        mGoBackbtn = (ImageButton) mBrowserControllerView.findViewById(R.id.imgBtn_back_browser_controller);
        mGoForwardBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.imgBtn_forward_browser_controller);
        mRefreshBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.imgBtn_refresh_browser_controller);
        mGoBrowserBtn = (ImageButton) mBrowserControllerView.findViewById(R.id.imgBtn_go_browser_controller);

        addView(mBrowserControllerView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        mGoBackbtn.setOnClickListener(this);
        mGoForwardBtn.setOnClickListener(this);
        mRefreshBtn.setOnClickListener(this);
        mGoBrowserBtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.imgBtn_back_browser_controller:
                if (canGoBack())
                    goBack();
                break;
            case R.id.imgBtn_forward_browser_controller:
                if (canGoForward())
                    goForward();
                break;
            case R.id.imgBtn_refresh_browser_controller:
                refresh();
                break;
            case R.id.imgBtn_go_browser_controller:
                useOtherBrowser(mContext, mLoadUrl);
                break;
        }

    }

    /**
     * 设置网页加载监听
     */
    public void setOnReceiveTitleListener(OnReceiveTitleListener listener) {
        this.mListener = listener;
    }

    /**
     * 判断网页能否返回上一级
     */
    public boolean canGoBack() {
        return mWebView != null ? mWebView.canGoBack() : false;
    }

    /**
     * 判断网页是否能向前
     */
    public boolean canGoForward() {
        return mWebView != null ? mWebView.canGoForward() : false;
    }

    /**
     * 回到网页上一级
     */
    public void goBack() {
        if (mWebView != null)
            mWebView.goBack();
    }

    /**
     * 网页向前如果可以
     */
    public void goForward() {
        if (mWebView != null)
            mWebView.goForward();
    }

    /**
     * 网页重新加载
     */
    public void refresh() {
        if (mWebView != null)
            mWebView.reload();
    }

    /**
     * 使用第三方浏览器打开
     */
    public void useOtherBrowser(Context context, String url) {
        if (TextUtils.isEmpty(url) || context == null)
            return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);

    }

    public void goneBottomBar() {
        mBrowserControllerView.setVisibility(GONE);
    }

    public void visibleBottomBar() {
        mBrowserControllerView.setVisibility(VISIBLE);
    }

    public void loadUrl(String url) {
        if (mWebView != null)
            mWebView.loadUrl(url);
    }

    public WebView getWebView() {
        return mWebView;
    }

    //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
    private class MonitorWebClient extends WebViewClient {
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                String url = request.getUrl().toString().toLowerCase();
                if (!hasAd(getContext(), url))
                    return super.shouldInterceptRequest(view, request);
                else
                    return new WebResourceResponse(null, null, null);
            }
            return super.shouldInterceptRequest(view, request);
        }

        //当load有ssl层的https页面时，如果这个网站的安全证书在Android无法得到认证，WebView就会变成一个空白页，而并不会像PC浏览器中那样跳出一个风险提示框

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {

            //忽略证书的错误继续Load页面内容
            handler.proceed();
            // super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mLoadUrl = url;
            if (mListener != null)
                mListener.onPageFinished();
        }
    }

    /**
     * 用于判断页面加载过程
     */
    private class AppCacheWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100)
                mProgressBar.setVisibility(GONE);
            else {
                mProgressBar.setVisibility(VISIBLE);
                mProgressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mListener != null)
                mListener.onReceive(title);
        }
    }

    public interface OnReceiveTitleListener {
        void onReceive(String title);

        void onPageFinished();
    }

    /**
     * 检查请求是否包含指定字段
     */
    private boolean hasAd(Context context, String url) {
        Resources resources = context.getResources();
        String[] adUrls = resources.getStringArray(R.array.url_ad);
        for (String adUrl : adUrls) {
            if (url.contains(adUrl))
                return true;
        }
        return false;
    }


}


