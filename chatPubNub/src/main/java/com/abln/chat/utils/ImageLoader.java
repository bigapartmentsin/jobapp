package com.abln.chat.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.abln.chat.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;


public class ImageLoader {


    private static boolean isGif(String isGif) {
        return isGif != null && isGif.endsWith(".gif");
    }

    public static void loadImage(String imagePath, ImageView imageView) {
        loadImageIntrnl(imagePath, imageView, R.drawable.dummy_image);
    }

    public static void loadImage(String imagePath, ImageView imageView, int placeHolder) {
        loadImageIntrnl(imagePath, imageView, placeHolder);
    }

    public static void newloadImage(String imag, ImageView iv, int placeholder) {

        myImageLoader(imag, iv, placeholder);

    }

    public static void loadImage(String imagePath, final ImageView imageView, final View progressView) {
        loadImageIntrnl(imagePath, imageView, progressView, R.drawable.ic_icon_download);
    }


    public static void loadCircularImage(String imagePath, final ImageView imageView, int placeHolder) {
        loadImageIntrnl(imagePath, imageView, placeHolder);
    }


    public static void loadCircularImage(String imagePath, final ImageView imageView, final View progressView) {
        loadImageIntrnl(imagePath, imageView, progressView, R.drawable.ic_icon_download);
    }

    public static void loadLocalImage(String imagePath, ImageView imageView, int placeHolder) {
        Glide.with(AppConfig.getContext()).load(imagePath)
                .placeholder(placeHolder)
                .error(placeHolder)
                .into(imageView);
    }

    private static void loadImageIntrnl(String imagePath, ImageView imageView, int placeHolder) {
        try {
            if (TextUtils.isEmpty(imagePath)) {
                imageView.setImageResource(placeHolder);
            } else {
                if (isGif(imagePath)) {
                    Glide.with(AppConfig.getContext()).load(imagePath)

                            .placeholder(placeHolder)
                            .error(placeHolder)
                            .dontAnimate()
                            .into(imageView);
                } else {

                    Glide.with(AppConfig.getContext()).load(imagePath)
                            .placeholder(placeHolder)


                            .error(placeHolder)
                            .dontAnimate()
                            .into(imageView);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void myImageLoader(String imagePath, ImageView imageView, int placeHolder) {
        try {
            if (TextUtils.isEmpty(imagePath)) {
                imageView.setImageResource(placeHolder);
            } else {
                if (isGif(imagePath)) {
                    Glide.with(AppConfig.getContext()).load(imagePath)
                            .placeholder(placeHolder)
                            .error(placeHolder)
                            .dontAnimate()

                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(7)))
                            .into(imageView);
                } else {

                    Glide.with(AppConfig.getContext()).load(imagePath)
                            .placeholder(placeHolder)
                            .apply(RequestOptions.bitmapTransform(new RoundedCorners(7)))
                            .error(placeHolder)
                            .dontAnimate()

                            .into(imageView);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public static void loadImageIntrnl(String imagePath, final ImageView imageView, final View progressView, int placeholder) {
        try {
            if (TextUtils.isEmpty(imagePath)) {
                imageView.setImageResource(placeholder);
                progressView.setVisibility(View.GONE);
            } else {
                progressView.setVisibility(View.VISIBLE);

                Glide.with(AppConfig.getContext()).load(imagePath)
                        .placeholder(placeholder).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                })
                        .error(placeholder)
                        .dontAnimate()
                        .into(imageView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int roundPixelSize) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = roundPixelSize;
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


}
