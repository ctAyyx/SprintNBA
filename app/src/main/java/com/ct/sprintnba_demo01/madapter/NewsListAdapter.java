package com.ct.sprintnba_demo01.madapter;


import android.content.Context;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;


import com.ct.sprintnba_demo01.base.net.NetConstant;
import com.ct.sprintnba_demo01.base.net.ServiceApi;
import com.ct.sprintnba_demo01.manimation.AnimationHelper;
import com.ct.sprintnba_demo01.mentity.NewsEntity;

import com.ct.sprintnba_demo01.mentity.VideoInfo;
import com.google.gson.Gson;

import java.util.List;
import java.util.concurrent.TimeUnit;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.ct.sprintnba_demo01.base.net.NetConstant.ConnectionTime;


/**
 * Created by ct on 2017/1/18.
 */

public class NewsListAdapter extends BaseRVAdapter<NewsEntity> {
    private BaseRVAdapter.OnItemClickListener listener;
    private OkHttpClient client;
    private AnimationHelper helper;

    public NewsListAdapter(Context mContext, List<NewsEntity> mList) {
        super(mContext, mList, R.layout.layout_news_list_item, R.layout.layout_new_list_item_video);
        helper = new AnimationHelper();
    }

    public void setOnItemClickListener(BaseRVAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getLayoutIndex(int position, NewsEntity item) {
        if (item.atype.equals("2"))
            return 1;
        return 0;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, final NewsEntity item) {

        if (item.atype.equals("2")) {
            final JCVideoPlayerStandard standard = holder.getView(R.id.jcvideo_news_list_item);

            // 近期腾讯视频真实地址解析后播放 提示“您未获授权，无法查看此网页。 HTTP403” 故同时支持跳转到网页播放
            ImageView ivGoto = holder.getView(R.id.ivGoto);
            standard.setUp("", JCVideoPlayer.SCREEN_LAYOUT_LIST, item.title);

            //获取视频真实地址
            if (TextUtils.isEmpty(item.realUrl)) {
                getVideoUri(item.vid, new RequestCallBack() {
                    @Override
                    public void onSuccess(VideoInfo real) {
                        if (real.vl.vi != null && real.vl.vi.size() > 0) {
                            String vid = real.vl.vi.get(0).vid;
                            String vkey = real.vl.vi.get(0).fvkey;
                            String url = real.vl.vi.get(0).ul.ui.get(0).url + vid + ".mp4?vkey=" + vkey;
                            item.realUrl = url;
                            standard.setUp(url, JCVideoPlayer.SCREEN_LAYOUT_LIST, item.title);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i("TAG", "获取视频播放地址错误" + t.toString());
                    }
                });
            } else {
                standard.setUp(item.realUrl, JCVideoPlayer.SCREEN_LAYOUT_LIST, item.title);
            }

            Glide.with(mContext).load(item.imgurl).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).centerCrop().into(standard.thumbImageView);

        } else {
            ImageView img = holder.getView(R.id.img_news_list_item);
            Glide.with(mContext).load(item.imgurl).asBitmap().format(DecodeFormat.PREFER_ARGB_8888)
//                    //  .transform(GlideUtil.getRoundTransformation(mContext, 5))
                    .into(img);
            //        Picasso.with(mContext).load(item.imgurl).resize(DeviceUtils.deviceWidth(mContext), DeviceUtils.dp2px(mContext, 200)).centerCrop().into(img);

            holder.setText(R.id.tv_news_list_item, item.abstractX);


            if (listener != null)
                holder.setOnItemViewClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onItemClick(position, v);
                    }
                });

        }


        helper.showItemAnim(holder.getItemView(), position, null);

    }

    /**
     * 初始网络设置
     */
    private void initHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        client = builder.connectTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .readTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .writeTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .build();
    }


    /**
     * 获取NBA 视屏的真实地址
     */

    private void getVideoUri(String vid, final RequestCallBack callBack) {
        initHttp();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstant.HOST_NBA_VIDEO)
                .addConverterFactory(ScalarsConverterFactory.create())
                //  .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build();

        ServiceApi serviceApi = retrofit.create(ServiceApi.class);

        Call<String> call = serviceApi.getVideoUrl(vid);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("TAG", "获取视屏" + response.body());
                if (response != null && !TextUtils.isEmpty(response.body())) {
                    String rep = response.body().replaceAll("QZOutputJson=", "").replaceAll(" ", "").replaceAll("\n", "");//获取Json数据
                    if (rep.endsWith(";"))
                        rep = rep.substring(0, rep.length() - 1);
                    //开始解析Json
                    VideoInfo videoInfo = new Gson().fromJson(rep, VideoInfo.class);
                    callBack.onSuccess(videoInfo);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }


    public interface RequestCallBack {
        void onSuccess(VideoInfo videoInfo);

        void onError(Throwable t);
    }

}
