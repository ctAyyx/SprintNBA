package com.ct.sprintnba_demo01.base.net;

import android.content.Context;


import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.constant.OwnConstant;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.ct.sprintnba_demo01.base.net.NetConstant.ConnectionTime;

/**
 * Created by ct on 2016/12/27.
 * ================================================
 * 网络访问服务类
 * ================================================
 * <p>
 * ================================================
 */

public class NetService {

    public static final String TOKEN = "api_token";
    public static final String HEADER_TOKEN = "TOKEN";
    private static ServiceApi dataRequest;
    private static ServiceApi serviceApiNBA;
    private static ServiceApi serviceApiMM;
    private static ServiceApi serviceApiMusic;

    private static HashMap<String, ServiceApi> map = new HashMap<>();


    private static Interceptor interceptor;
    private static OkHttpClient client;
    ;

    /**
     * API 服务获取
     *
     * @param context
     * @return
     */
    public static ServiceApi getApiService(final Context context) {
        if (dataRequest == null) {
            //initInterceptor(context);
            initHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetConstant.HOST_NBA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            dataRequest = retrofit.create(ServiceApi.class);
        }
        return dataRequest;
    }

    public static ServiceApi getApiService(Context context, String host) {
        if (map.get(host) == null) {
            initHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            ServiceApi serviceApi = retrofit.create(ServiceApi.class);
            map.put(host, serviceApi);
            return serviceApi;
        }
        return map.get(host);

/*        if (dataRequest == null) {
            //initInterceptor(context);
            initHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(host)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            dataRequest = retrofit.create(ServiceApi.class);
        }
        return dataRequest;*/
    }


    private static void initInterceptor(final Context context) {
        interceptor = new Interceptor(
        ) {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                ECache cache = ECache.get(context, HEADER_TOKEN);
                String token = cache.getAsString(TOKEN);
                if (token == null)
                    token = "";
                Request newRequest = chain.request().newBuilder().addHeader(HEADER_TOKEN, token).build();
                return chain.proceed(newRequest);

            }

        };
    }

    private static void initHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //     .addInterceptor(interceptor)
        if (interceptor != null)
            builder.addInterceptor(interceptor);
        client = builder.connectTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .readTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .writeTimeout(ConnectionTime, TimeUnit.MILLISECONDS)
                .build();
    }

    /*******************************************************
     * NBA
     *******************************************************/
    public static ServiceApi getApiServiceNBA(final Context context) {
        if (serviceApiNBA == null) {
            //initInterceptor(context);
            initHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetConstant.HOST_NBA)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            serviceApiNBA = retrofit.create(ServiceApi.class);
        }
        return serviceApiNBA;
    }

    /*************************************NBA*******************************************************/


    /***************************************************
     * MM
     ***************************************************/
    public static ServiceApi getApiServiceMM(final Context context) {
        if (serviceApiMM == null) {
            //initInterceptor(context);
            initHttp();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetConstant.HOST_MM)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            serviceApiMM = retrofit.create(ServiceApi.class);
        }
        return serviceApiMM;
    }
    /********************************************MM***************************************************/


    /***************************************************
     * Music
     ***************************************************/
    public static ServiceApi getApiServiceMusic(final Context context) {
        if (serviceApiMusic == null) {
            //initInterceptor(context);
            initHttp();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetConstant.HOST_MUSIC)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();
            serviceApiMusic = retrofit.create(ServiceApi.class);
        }
        return serviceApiMusic;
    }
    /********************************************MM***************************************************/


    /**
     * 登录后设置设置Token,取消登录设置为null
     *
     * @param context
     * @param token
     */
    public static void setToken(Context context, String token) {
        ECache cache = ECache.get(context, HEADER_TOKEN);
        cache.put(TOKEN, token);
        dataRequest = null;
    }


}
