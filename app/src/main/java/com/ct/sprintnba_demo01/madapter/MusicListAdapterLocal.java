package com.ct.sprintnba_demo01.madapter;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.ct.sprintnba_demo01.R;
import com.ct.sprintnba_demo01.ShowArtistActivity;
import com.ct.sprintnba_demo01.base.adapter.BaseRVAdapter;
import com.ct.sprintnba_demo01.base.adapter.BaseRVHolder;
import com.ct.sprintnba_demo01.base.net.NetService;
import com.ct.sprintnba_demo01.base.response.BaseMusicResponse;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.mentity.Bitrate;
import com.ct.sprintnba_demo01.mentity.Music;
import com.ct.sprintnba_demo01.mentity.SongEntity;
import com.ct.sprintnba_demo01.mentity.SongInfo;
import com.ct.sprintnba_demo01.music.DownOnlineMusic;
import com.ct.sprintnba_demo01.music.ShareOnlineMusic;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ct on 2017/2/14.
 */

public class MusicListAdapterLocal extends BaseRVAdapter<SongEntity> implements View.OnClickListener {
    private OnItemClickListenerDefualt<SongEntity> listener;
    private AlertDialog dialog;
    private boolean isLocal;

    //=========dialog 控件============
    private View view;
    private TextView tv_title, tv_share, tv_down, tv_detail;
    private LinearLayout linear_share, linear_down, linear_detail;
    private ImageView img_photo, img_down;
    private SongEntity currentSong;

    public MusicListAdapterLocal(Context mContext, List<SongEntity> mList, boolean isLocal) {
        super(mContext, mList, R.layout.layout_music_list_item_song);
        this.isLocal = isLocal;
    }


    public void setOnItemClickListener(OnItemClickListenerDefualt<SongEntity> listener) {
        this.listener = listener;
    }

    @Override
    public void onBindData(BaseRVHolder holder, final int position, final SongEntity item) {
        ImageView img = holder.getView(R.id.img_music_item_photo);
        Glide.with(mContext).load(item.pic_small).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).placeholder(R.drawable.default_cover).into(img);
        holder.setText(R.id.tv_music_item_title, item.title);
        holder.setText(R.id.tv_music_item_special, item.author + " - " + item.album_title);

        item.position = position;

        holder.setOnClickListener(R.id.img_music_item_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(position, item);
            }
        });

        if (listener != null)
            holder.setOnClickListener(R.id.linear_music_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position, v, item);
                }
            });

    }

    /**
     * 创建显示
     */
    private void showDialog(int position, SongEntity entity) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            dialog = builder.create();
        }
        if (view == null) {
            view = View.inflate(mContext, R.layout.layout_music_dialog, null);

            tv_title = (TextView) view.findViewById(R.id.tv_music_dialog_title);
            tv_share = (TextView) view.findViewById(R.id.tv_music_dialog_share);
            tv_detail = (TextView) view.findViewById(R.id.tv_music_dialog_detail);
            tv_down = (TextView) view.findViewById(R.id.tv_music_dialog_down);
            linear_share = (LinearLayout) view.findViewById(R.id.linear_music_dialog_share);
            linear_detail = (LinearLayout) view.findViewById(R.id.linear_music_dialog_detail);
            linear_down = (LinearLayout) view.findViewById(R.id.linear_music_dialog_down);
            img_photo = (ImageView) view.findViewById(R.id.img_music_dialog_detail);
            img_down = (ImageView) view.findViewById(R.id.img_music_dialog_down);

            linear_down.setOnClickListener(this);
            linear_detail.setOnClickListener(this);
            linear_share.setOnClickListener(this);
        }

        tv_title.setText(entity.title);
        if (isLocal) {
            //重新设置Dialog里面的值
            tv_down.setText("设置铃音");
            tv_detail.setText("歌曲信息");
            img_down.setImageResource(R.drawable.ring);


        } else
            Glide.with(mContext).load(entity.pic_small).into(img_photo);
        currentSong = entity;
        dialog.setView(view);
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_music_dialog_share:
                share();
                cancel();
                break;
            case R.id.linear_music_dialog_down:
                if (isLocal)
                    setRing();
                else
                    down();
                cancel();
                break;
            case R.id.linear_music_dialog_detail:
                if (isLocal)
                    musicMsg();
                else
                    showArtist(currentSong.ting_uid, currentSong.artist_name);
                cancel();
                break;
        }
    }

    /**
     * 分享歌曲
     */
    private void share() {
        new ShareOnlineMusic(mContext, currentSong) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onSuccess(Bitrate bitrate) {
                cancel();
            }

            @Override
            public void onFail(Exception e) {

            }
        }.execute();
    }

    /**
     * 下载歌曲
     */
    private void down() {

        new DownOnlineMusic(mContext, currentSong) {
            @Override
            public void onPrepare() {

            }

            @Override
            public void onSuccess(Bitrate bitrate) {
                cancel();
            }

            @Override
            public void onFail(Exception e) {

            }
        }.execute();
    }

    /**
     * 启动歌手详情界面
     *
     * @param uid  歌手Id
     * @param name 歌手名
     */
    private void showArtist(String uid, String name) {
        Intent intent = new Intent(mContext, ShowArtistActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(OwnConstant.MUSIC_ARTIST_ID, uid);
        bundle.putString(OwnConstant.MUSIC_ARTIST_NAME, name);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
        cancel();
    }

    /**
     * 关闭Dialog
     */
    private void cancel() {
        if (dialog != null && dialog.isShowing())
            dialog.cancel();
    }

    /**
     * 设置为铃音
     */
    private void setRing() {
        File file = new File(currentSong.url);
        if (!file.exists())
            return;
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(currentSong.url);
        //查询音乐文件在媒体库是否存在
        Cursor cursor = mContext.getContentResolver().query(uri,
                null, MediaStore.MediaColumns.DATA + "=?", new String[]{currentSong.url}, null);
        if (cursor == null)
            return;
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            String _id = cursor.getString(0);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Audio.Media.IS_MUSIC, true);
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_PODCAST, false);

            mContext.getContentResolver().update(uri, values, MediaStore.MediaColumns.DATA + "=?",
                    new String[]{currentSong.url}
            );
            Uri newUri = ContentUris.withAppendedId(uri, Long.valueOf(_id));
            RingtoneManager.setActualDefaultRingtoneUri(mContext, RingtoneManager.TYPE_RINGTONE, newUri);


        }
        cursor.close();

    }

    /**
     * 歌曲信息
     */
    private void musicMsg() {

    }

}
