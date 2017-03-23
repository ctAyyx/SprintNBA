package com.ct.sprintnba_demo01;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.NotificationManager;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.rx.TransformUtils;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import it.sephiroth.android.library.picasso.Picasso;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class MMDetaileActivity extends BaseActivity {


    @BindView(R.id.img_mm_detail)
    PhotoView img_detail;
    @BindView(R.id.img_mm_transition)
    ImageView img_transition;

    private boolean isShow = true;
    private String img_url;//资源图片地址

    private int now = -1;
    private final int SAVE = 0;
    private final int WALLPAOER = 1;
    private final int SHARE = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_mmdetaile;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        img_url = getIntent().getStringExtra("img_URL");
        String title = getIntent().getStringExtra("NAME");
        if (TextUtils.isEmpty(img_url))
            return;

        Toolbar toolbar = getToolBar();
        toolbar.setBackgroundColor(0x000000);
        getSupportActionBar().setTitle(title);

        ViewCompat.setTransitionName(img_transition, "imgDetail");
        Picasso.with(this).load(img_url).into(img_transition);
        Picasso.with(this).load(img_url).into(img_detail);

        img_transition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_transition.setVisibility(View.GONE);
                img_detail.setVisibility(View.VISIBLE);
                showOrHideToolBar();
            }
        });

        img_detail.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                //单击PhotoView事件，实现ToolBar的隐藏，实现全屏模式
                showOrHideToolBar();

                img_detail.setVisibility(View.GONE);
                img_transition.setVisibility(View.VISIBLE);

            }
        });
    }

    /**
     * 使用属性动画完成ToolBar的显示和隐藏
     */
    private void showOrHideToolBar() {
        AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator(2));
        set.setDuration(200);
        if (isShow) {
            ObjectAnimator animator01 = ObjectAnimator.ofFloat(getToolBar(), "alpha", 1.0f, 0.0f);
            ObjectAnimator animator02 = ObjectAnimator.ofFloat(getToolBar(), "translationY", 0, -(getStatusBarHeight()));
            set.play(animator01).with(animator02);

            DeviceUtils.exitSystemUI(this);
            isShow = false;

            //动画结束 隐藏Toolbar
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    getToolBar().setVisibility(View.GONE);
                }
            });
            set.start();

        } else {
            ObjectAnimator animator01 = ObjectAnimator.ofFloat(getToolBar(), "alpha", 0.0f, 1.0f);
            ObjectAnimator animator02 = ObjectAnimator.ofFloat(getToolBar(), "translationY", 0, 0);
            set.play(animator01).with(animator02);

            DeviceUtils.enterSystemUI(this);
            isShow = true;

            //动画开始 显示ToolBar
            set.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    getToolBar().setVisibility(View.VISIBLE);

                }
            });

            set.start();
        }

    }

    private int getStatusBarHeight() {
        int statusBarHeight = -1;
//获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 创建选项菜单
     * <p>
     * 保存
     * 设为壁纸
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.menu_mm_detaile, menu);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mm_detail_down://保存
                now = SAVE;
                handleImg();
                break;
            case R.id.mm_detail_bg://背景
                now = WALLPAOER;
                handleImg();
                break;
            case R.id.mm_detail_share://分享
                now = SHARE;
                handleImg();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 保存操作
     */
    private void handleImg() {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {

                Bitmap bitmap = null;

                try {
                    bitmap = Picasso.with(MMDetaileActivity.this).load(img_url).get();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                if (bitmap == null)
                    subscriber.onError(new NullPointerException("保存失败，请重试"));

                subscriber.onNext(bitmap);
                subscriber.onCompleted();
            }
        }).flatMap(new Func1<Bitmap, Observable<Uri>>() {
            @Override
            public Observable<Uri> call(Bitmap bitmap) {
                //转换并保存
                return getUriObservable(bitmap, img_url);
            }
        }).compose(TransformUtils.<Uri>defaultSchedulers())
                .subscribe(new Subscriber<Uri>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TAG", "ERROR" + e.toString());
                        switch (now) {
                            case SAVE:
                                Toast.makeText(MMDetaileActivity.this, "保存失败，请重试", Toast.LENGTH_LONG).show();
                                break;
                            case WALLPAOER:
                                Toast.makeText(MMDetaileActivity.this, "设置壁纸失败，请重试", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }

                    @Override
                    public void onNext(Uri uri) {
                        switch (now) {
                            case SAVE:
                                Toast.makeText(MMDetaileActivity.this, "保存图片成功", Toast.LENGTH_LONG).show();
                                break;
                            case WALLPAOER:
                                setWallpaper(uri);
                                break;
                            case SHARE:
                                share(uri);
                                break;
                        }

                    }
                });
    }

    /**
     * 获取被观察者对象
     *
     * @param bitmap
     * @param imgUri
     * @return Observable
     */
    private Observable<Uri> getUriObservable(Bitmap bitmap, String imgUri) {
        File file = getImgFile(bitmap, imgUri);

        if (file == null)
            return Observable.error(new NullPointerException("保存图片失败！"));
        Uri uri = Uri.fromFile(file);

        //通知图库更新
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));

        return Observable.just(uri);

    }


    /**
     * 保存图片
     *
     * @param bitmap 要保存的位图对象
     * @param imgUrl 图片地址
     * @return File
     */
    private File getImgFile(Bitmap bitmap, String imgUrl) {
        String fileName = "/sprint/mm/" + imgUrl.hashCode() + ".jpg";
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), fileName);

        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        FileOutputStream ous = null;

        try {
            ous = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ous);
        } catch (Exception e) {
            Log.i("TAG", "保存到文件失败" + e.toString());
            return null;
        } finally {
            if (ous != null)
                try {
                    ous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            ous = null;
        }

        return file;
    }

    /**
     * 设置壁纸
     *
     * @param imgUri
     */
    private void setWallpaper(Uri imgUri) {

        WallpaperManager manager = WallpaperManager.getInstance(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            File wallFile = new File(imgUri.getPath());
            Uri contentURI = getImageContentUri(MMDetaileActivity.this, wallFile.getAbsolutePath());
//                    Uri uri1 = getImageContentUri(mActivity, imageUri.getPath());
            startActivity(manager.getCropAndSetWallpaperIntent(contentURI));
        } else {
            try {
                manager.setStream(getContentResolver().openInputStream(imgUri));
            } catch (Exception e) {

            }
        }
    }


    private Uri getImageContentUri(Context context, String absPath) {
        Log.i("TAG", "保存：" + absPath);
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID}
                , MediaStore.Images.Media.DATA + "=? "
                , new String[]{absPath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(id));

        } else if (!absPath.isEmpty()) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.DATA, absPath);
            return context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return null;
        }
    }

    private void share(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        startActivity(Intent.createChooser(intent, "分享"));
    }
}
