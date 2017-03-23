package com.ct.sprintnba_demo01.base.net;


import com.ct.sprintnba_demo01.base.response.ArtistResponse;
import com.ct.sprintnba_demo01.base.response.BaseMMResponse;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.base.response.BaseNBAResponse;
import com.ct.sprintnba_demo01.base.response.MusicLrcResponse;
import com.ct.sprintnba_demo01.base.response.QueryMusicResponse;
import com.ct.sprintnba_demo01.mentity.MMEntity;
import com.ct.sprintnba_demo01.mentity.NBAPlayer;
import com.ct.sprintnba_demo01.mentity.NewDetailEntity;
import com.ct.sprintnba_demo01.mentity.NewsEntity;
import com.ct.sprintnba_demo01.mentity.NewsIndexEntity;
import com.ct.sprintnba_demo01.mentity.SongEntity;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import rx.Observer;

/**
 * Created by ct on 2016/12/27.
 * ==================================
 * 数据请求接口
 * ==================================
 */

public interface ServiceApi {

    /**
     * 获取头条数据列表Id
     */
    @GET("news/index")
    Observable<BaseNBAResponse<List<NewsIndexEntity>>> getNewIndex(@Query("column") String column);

    /**
     * 获取头条数据
     * /news/item?column=banner&articleIds=20170118002120,20170117030184,20170117021830
     */
    @GET("news/item")
    Observable<BaseNBAResponse<Map<String, NewsEntity>>> getNewsItem(@Query("column") String column, @Query("articleIds") String articleIds);

    /**
     * 获取详情
     */
    @GET("news/detail")
    Observable<BaseNBAResponse<NewDetailEntity>> getDetail(@Query("column") String column, @Query("articleId") String articleId);

    /**
     * 获取视频地址
     */
    @GET("getinfo?platform=11001&charge=0&otype=json")
    Call<String> getVideoUrl(@Query("vids") String vid);


    /**
     * 获取球队战绩
     */
    @GET("team/rank?appver=1.0.2.2&appvid=1.0.2.2")
    Call<ResponseBody> getTeamRecord();


    /**
     * 获取球员信息
     */
    @GET("player/list?appver=1.0.2.2&appvid=1.0.2.2")
    Observable<BaseNBAResponse<List<NBAPlayer>>> getNBAPlayer();

    /************************************************************************************************
     * 美女图片
     ***********************************************************************************************/
    @GET("api.php?c=photo&a=getImageList")
    Observable<BaseMMResponse<List<MMEntity>>> getMMPhoto(@Query("page") int page, @Query("category") String category);


    /**************************************************************************************************
     * Music
     **************************************************************************************************/

    /**
     * 获取指定类型和数量的歌曲条目
     *
     * @param type 歌曲类型
     * @param size 获取歌曲数量
     *             http://tingapi.ting.baidu.com/v1/restserver/ting?method=baidu.ting.billboard.billList&type=2&size=3
     */
    @GET("v1/restserver/ting?method=baidu.ting.billboard.billList")
    Observable<BaseMusicResponse<ArrayList<SongEntity>>> getTop3Music(@Query("type") String type, @Query("size") int size);

    /**
     * 获取指定类型，数量，和起始位置的歌曲条目
     * <p>
     * * @param type 歌曲类型
     *
     * @param size   获取歌曲数量
     * @param offset 开始位置
     */
    @GET("v1/restserver/ting?method=baidu.ting.billboard.billList")
    Observable<BaseMusicResponse<ArrayList<SongEntity>>> getMusicList(@Query("type") String type, @Query("size") int size, @Query("offset") int offset);

    /**
     * 获取指定Id的歌曲信息
     *
     * @param song_id 要获取的音乐id
     */
    @GET("/v1/restserver/ting?method=baidu.ting.song.play")
    Call<BaseMusicResponse> getMusic(@Query("songid") String song_id);

    /**
     * 下载歌词
     */
    @Streaming
    @GET
    Call<ResponseBody> downMusicLyric(@Url String url);

    /**
     * 下载专辑图片
     */
    @Streaming
    @GET
    Call<ResponseBody> downMusicImg(@Url String url);


    /**
     * 获取歌手详情
     */
    @GET("/v1/restserver/ting?method=baidu.ting.artist.getInfo")
    Observable<ArtistResponse> getArtist(@Query("tinguid") String tingId);

    /**
     * 歌曲搜索
     * /v1/restserver/ting?method=baidu.ting.search.catalogSug&query=I%20see%20you
     */
    @GET("/v1/restserver/ting?method=baidu.ting.search.catalogSug")
    Observable<QueryMusicResponse> getSongList(@Query("query") String query);

    /**
     * 下载搜索歌曲的歌词
     */
    @GET("/v1/restserver/ting?method=baidu.ting.song.lry")
    Call<MusicLrcResponse> downSearchLrc(@Query("songid") String songId);

}
