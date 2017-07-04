package com.ct.sprintnba_demo01;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ct.sprintnba_demo01.base.BaseActivity;
import com.ct.sprintnba_demo01.mutils.FileUtils;
import com.ct.sprintnba_demo01.mview.view.ClipViewLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.OnClick;

public class ClipActivity extends BaseActivity {

    @BindView(R.id.clip_view)
    ClipViewLayout clipViewLayout;
    @BindView(R.id.tv_cancel_clip)
    TextView tv_cancel;
    @BindView(R.id.tv_ok_clip)
    TextView tv_ok;


    @Override
    public int getLayoutId() {
        return R.layout.activity_clip;
    }

    @Override
    public void initViewsAndEvents(Bundle savedInstanceState) {
        //获取裁剪图片地址
        Uri uri = getIntent().getData();
        //为裁剪布局添加图片地址
        clipViewLayout.setImageSrc(uri);

    }

    @OnClick({R.id.tv_cancel_clip, R.id.tv_ok_clip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel_clip:
                finish();
                break;
            case R.id.tv_ok_clip:
                generateUriAndReturn();
                break;
        }
    }

    private void generateUriAndReturn() {
        //返回剪裁图片
        Bitmap bitmap = clipViewLayout.clip();
        if (bitmap == null)
            return;
        Uri saveFile = Uri.fromFile(new File(getCacheDir(), "clip_" + System.currentTimeMillis() + ".jpg"));
        if (saveFile != null) {
            OutputStream ous = null;
            try {
                ous = getContentResolver().openOutputStream(saveFile);
                if (ous != null)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, ous);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    if (ous != null)
                        ous.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //设置返回结果
            Intent intent = new Intent();
            intent.setData(saveFile);
            setResult(RESULT_OK, intent);
            finish();

        }


    }

}
