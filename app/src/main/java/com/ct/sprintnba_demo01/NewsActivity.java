package com.ct.sprintnba_demo01;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.base.fragment.BaseFragment;
import com.ct.sprintnba_demo01.base.rx.RxBus;
import com.ct.sprintnba_demo01.base.rx.RxDataEvent;
import com.ct.sprintnba_demo01.base.utils.ECache;
import com.ct.sprintnba_demo01.base.utils.Preference;
import com.ct.sprintnba_demo01.constant.OwnConstant;
import com.ct.sprintnba_demo01.madapter.DrawerLVAdapter;
import com.ct.sprintnba_demo01.madapter.DrawerVPAdapter;
import com.ct.sprintnba_demo01.mentity.DrawerEntity;
import com.ct.sprintnba_demo01.mfragment.MMFragment;
import com.ct.sprintnba_demo01.mfragment.MusicFragment;
import com.ct.sprintnba_demo01.mfragment.NewsFragment;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.ct.sprintnba_demo01.mutils.GlideUtil;
import com.ct.sprintnba_demo01.mview.XViewPager;
import com.ct.sprintnba_demo01.mview.view.CircleImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * ========================
 * 主界面
 * ========================
 */
public class NewsActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, DrawerLVAdapter.OnItemClickListener {


    @BindView(R.id.xviewpager_news)
    XViewPager viewPager;
    @BindView(R.id.img_head_portrait_drawer_news)
    CircleImageView img_head;
    @BindView(R.id.tv_nickname_drawer_news)
    TextView tv_nickname;
    @BindView(R.id.lv_drawer_news)
    ListView lv_menu;
    @BindView(R.id.drawer_news)
    DrawerLayout drawer;
    @BindView(R.id.img_bg_drawer)
    ImageView img_bg;

    private boolean toClose;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLVAdapter adapter;
    private ArrayList<DrawerEntity> mList = new ArrayList<>();

    private int currentPosition;//当前选中界面标志器
    private ArrayList<BaseFragment> list;
    private ECache eCache;

    private AlertDialog dialog;
    //调用照相机返回图片临时文件
    private File tempFile;

    private String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    public int getLayoutId() {
        return R.layout.activity_news;

    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {

        if (drawerToggle == null) {
            drawerToggle = new ActionBarDrawerToggle(this, drawer, getToolBar(), R.string.drawer_open, R.string.drawer_close);
            drawerToggle.setDrawerIndicatorEnabled(true);
            drawer.addDrawerListener(drawerToggle);
        }

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
        String path = preference.getString("photoUri", "");

        if (TextUtils.isEmpty(path))
            Glide.with(this).load(R.drawable.timg)
                    .transform(new GlideUtil().new CircleTransformation(NewsActivity.this))
                    .into(img_head);
        else
            Glide.with(this).load(path)
                    .transform(new GlideUtil().new CircleTransformation(NewsActivity.this))
                    .into(img_head);
        tv_nickname.setText("昵称");
        viewPager.setEnableScroll(false);

        getData();
        adapter = new DrawerLVAdapter(this, mList);
        lv_menu.setAdapter(adapter);
        initListener();

        initFragment();
        viewPager.setAdapter(new DrawerVPAdapter(getSupportFragmentManager(), list));
        viewPager.setOffscreenPageLimit(list.size());
        setTitle(mList.get(viewPager.getCurrentItem() + 1).getText());

        startBgAnimator();
        //创建拍照存储临时文件
        createCameraTempFile(savedInstanceState);


    }

    /**
     * 开始背景图的动画
     */
    private void startBgAnimator() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img_bg, "translationY", 0f, -190f, 0f);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(img_bg, "translationX", 0f, -240f, 0f);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator2.setRepeatCount(ValueAnimator.INFINITE);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(33000);
        set.setInterpolator(new LinearInterpolator());
        set.play(animator1).with(animator2);
        set.start();


    }


    /**
     * 初始化监听
     */
    private void initListener() {
        img_head.setOnClickListener(this);

        adapter.setOnItemClickListener(this);

    }

    @Override
    public void onClick(int position, View view, DrawerEntity entity) {
        currentPosition = position;
        Menu menu = getToolBar().getMenu();
        switch (entity.tag) {
            case NBA:
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_news, menu);
                setTitle(entity.getText());
                break;
            case MM:
                JCVideoPlayer.releaseAllVideos();
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_news, menu);
                setTitle(entity.getText());
                break;
            case MUSIC:
                JCVideoPlayer.releaseAllVideos();
                //修改Toolbar 的菜单
                menu.clear();
                getMenuInflater().inflate(R.menu.menu_music_search, menu);
                setTitle(entity.getText());
                break;
            case CACHE:
                FileUtils.cleanAppCache(this);
                Toast.makeText(this, "缓存清理成功！", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                break;
            case NIGHT:
                JCVideoPlayer.releaseAllVideos();
                changeTheme();
                break;
        }
        drawer.closeDrawer(Gravity.LEFT);//关闭侧滑栏
        viewPager.setCurrentItem(currentPosition, false);
    }

    @Override
    protected void onPause() {
        JCVideoPlayer.releaseAllVideos();
        super.onPause();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_news, menu);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                //前往关于界面
                startActivityNow(AboutMeActivity.class, null, null);
                break;
            case R.id.menu_music_search:
                //启动搜索界面
                startActivityNow(MusicSearchActivity.class, null, null);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化侧滑栏数据
     */
    private void getData() {
        mList.add(new DrawerEntity(0, "专栏"));
        mList.add(new DrawerEntity(R.drawable.nba, "NBA", DrawerEntity.DrawerType.NBA, 0));
        mList.add(new DrawerEntity(R.drawable.mm, "福利", DrawerEntity.DrawerType.MM, 1));
        mList.add(new DrawerEntity(R.drawable.music, "音乐", DrawerEntity.DrawerType.MUSIC, 2));
        mList.add(new DrawerEntity(0, "其它"));
        mList.add(new DrawerEntity(R.drawable.clean, "清除缓存", DrawerEntity.DrawerType.CACHE));
        if (Preference.getBoolean(OwnConstant.ISNIGHTTHEME))
            mList.add(new DrawerEntity(R.drawable.night, "夜间模式", DrawerEntity.DrawerType.NIGHT));
        else
            mList.add(new DrawerEntity(R.drawable.sun, "夜间模式", DrawerEntity.DrawerType.NIGHT));

    }

    /**
     * 初始化侧滑栏点击加载的界面
     */
    private void initFragment() {
        if (list == null)
            list = new ArrayList<>();
        list.add(new NewsFragment());
        list.add(new MMFragment());
        list.add(new MusicFragment());
    }


    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress())
            return;

        if (drawer.isDrawerOpen(Gravity.LEFT))
            drawer.closeDrawer(Gravity.LEFT);
        else {
            if (toClose)
                finish();
            else {

                Toast.makeText(this, "再次点击退出应用！", Toast.LENGTH_SHORT).show();
                toClose = true;
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        toClose = false;
                    }
                }, 1000);
            }

        }

    }

    /************************************************************************************************
     * 夜间模式切换模块
     ************************************************************************************************/
/*
    private void changeToNight() {
        if (eCache == null)
            eCache = ECache.get(this, OwnConstant.CACHE_ACTIVITY_BASE);
        String isNight = eCache.getAsString(OwnConstant.CACHE_ACTIVITY_NIGHT);
        Log.i("TAG", "当前模式" + isNight);
        if (TextUtils.isEmpty(isNight) || OwnConstant.DAY.equals(isNight)) {
            //夜间模式
            prepareNight(true);
            eCache.put(OwnConstant.CACHE_ACTIVITY_NIGHT, OwnConstant.NIGHT);
        } else {
            //白天模式
            prepareNight(false);
            eCache.put(OwnConstant.CACHE_ACTIVITY_NIGHT, OwnConstant.DAY);
        }


    }


    private void prepareNight(boolean startNight) {
        updataNightMode(startNight);
        Handler handler = new Handler(getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 500);
    }

    private void updataNightMode(boolean startNight) {
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        Configuration configuration = getApplicationContext().getResources().getConfiguration();

        configuration.uiMode &= ~Configuration.UI_MODE_NIGHT_MASK;
        configuration.uiMode |= startNight ? Configuration.UI_MODE_NIGHT_YES : Configuration.UI_MODE_NIGHT_NO;
        getApplicationContext().getResources().updateConfiguration(configuration, metrics);
    }
*/

    /*********************************************************************************************************
     * 更换头像模块
     **********************************************************************************************************/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_head_portrait_drawer_news://点击选择头像
                showDialogFromBottom();
                break;
            case R.id.linear_dialog_camera://点击启动照相机
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
                startActivityForResult(camera, 100);
                cancel();
                break;
            case R.id.linear_dialog_gallery://点击进入图库

                //6.0以上动态申请权限
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (EasyPermissions.hasPermissions(this, permission)) {
                        inPhoto();
                    } else {
                        //申请权限
                        EasyPermissions.requestPermissions(this, "需要读取图片库的权限！", 0, permission);
                    }
                } else {
                    inPhoto();
                }


                break;
        }
    }


    private void inPhoto() {
        Intent graller = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(graller, 101);
        cancel();
    }


    /**
     * 底部显示Dialog
     */
    private void showDialogFromBottom() {
        if (dialog == null) {

            //初始换Dialog中的布局
            View view = View.inflate(this, R.layout.layout_dialog_choose_photo, null);
            LinearLayout linear_camera = (LinearLayout) view.findViewById(R.id.linear_dialog_camera);
            LinearLayout linear_gallery = (LinearLayout) view.findViewById(R.id.linear_dialog_gallery);
            linear_camera.setOnClickListener(this);
            linear_gallery.setOnClickListener(this);

            //创建Dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.dialog);
            builder.setView(view);
            dialog = builder.create();
            //设置Dialog位置
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            //设置dialog进入动画
            window.setWindowAnimations(R.style.dialog_show);
            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
        }

        dialog.show();

    }

    private void cancel() {
        if (dialog == null)
            return;
        if (dialog.isShowing())
            dialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    //照相机 指定了临时文件位置 则data为空
                    gotoClipActivity(Uri.fromFile(tempFile));
                    break;
                case 101:
                    if (data != null)
                        gotoClipActivity(data.getData());
                    break;
                case 102:
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            String path = getRealFilePathFromUri(this, uri);
                            Glide.with(this).load(path).transform(new GlideUtil().new CircleTransformation(this)).into(img_head);
                            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
                            SharedPreferences.Editor editor = preference.edit();
                            editor.putString("photoUri", path);
                            editor.commit();

                        }


                    }
                    break;
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //打开图片裁剪界面
    private void gotoClipActivity(Uri uri) {
        if (uri == null)
            return;
        Intent intent = new Intent(this, ClipActivity.class);
        intent.setData(uri);
        startActivityForResult(intent, 102);

    }

    /**
     * 创建调用照相机存储的临时文件
     */
    private void createCameraTempFile(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey("tempFile"))
            tempFile = (File) savedInstanceState.getSerializable("tempFile");
        else
            tempFile = new File(checkDirPath(FileUtils.getTempDir()), System.currentTimeMillis() + ".jpg");
    }

    /**
     * 检查文件是否存在
     */
    private String checkDirPath(String dirpath) {
        if (TextUtils.isEmpty(dirpath))
            return "";

        File dir = new File(dirpath);
        if (!dir.exists())
            dir.mkdirs();
        return dirpath;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tempFile", tempFile);
    }

    /**
     * 根据Uri返回文件绝对路径
     * 兼容了file:///开头的 和 content://开头的情况
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    private String getRealFilePathFromUri(Context context, Uri uri) {
        if (uri == null)
            return "";
        String scheme = uri.getScheme();
        String date = null;
        if (scheme == null)
            date = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme))
            date = uri.getPath();
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1)
                        date = cursor.getString(index);
                }
                cursor.close();
            }
        }
        return date;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //申请权限成功
        inPhoto();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //申请权限失败
    }


}
