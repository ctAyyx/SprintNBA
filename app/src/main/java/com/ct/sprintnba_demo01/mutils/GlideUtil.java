package com.ct.sprintnba_demo01.mutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.ct.sprintnba_demo01.base.utils.DeviceUtils;

/**
 * Created by Administrator on 2017/1/20.
 */

public class GlideUtil {

    /**
     * 圆形图片
     */
   public class CircleTransformation extends BitmapTransformation {


        public CircleTransformation(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;

            canvas.drawCircle(r, r, r, paint);
            return result;

        }


        @Override
        public String getId() {

            return getClass().getName();
        }
    }

    /**
     * 圆角图片
     */
   public class RoundTransformation extends BitmapTransformation {
        private float radius;


        public RoundTransformation(Context context) {
            this(context, 4);
        }

        public RoundTransformation(Context context, int dp) {
            super(context);
            final float scale = context.getResources().getDisplayMetrics().density;
            radius = dp * scale + 0.5f;
        }


        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return roundCrop(pool, toTransform);

        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null)
                return null;
            Log.i("TAG", "============1");
            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Log.i("TAG", "============2");
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rect = new RectF(0, 0, source.getWidth(), source.getHeight());

            canvas.drawRoundRect(rect, radius, radius, paint);
            Log.i("TAG", "============3");
            return result;

        }

        @Override
        public String getId() {
            Log.i("TAG", "============4" + getClass().getName());
            return getClass().getName();
        }
    }
}
