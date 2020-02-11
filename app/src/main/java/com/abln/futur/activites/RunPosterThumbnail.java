package com.abln.futur.activites;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RunPosterThumbnail extends AppCompatActivity {
    LinearLayout savedImage;

    public static Bitmap loadBitmapFromView(View view) {

        // width measure spec
        int widthSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
        // height measure spec
        int heightSpec = View.MeasureSpec.makeMeasureSpec(
                view.getMeasuredHeight(), View.MeasureSpec.EXACTLY);
        // measure the view
        view.measure(widthSpec, heightSpec);
        // set the layout sizes
        view.layout(view.getLeft(), view.getTop(), view.getMeasuredWidth() + view.getLeft(), view.getMeasuredHeight() + view.getTop());
        // create the bitmap
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        // create a canvas used to get the view's image and draw it on the bitmap
        Canvas c = new Canvas(bitmap);
        // position the image inside the canvas
        c.translate(-view.getScrollX(), -view.getScrollY());
        // get the canvas
        view.draw(c);

        return bitmap;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.poster_thumb_nail_);


        Bitmap bm = null;

        savedImage = findViewById(R.id.imageWithoutFrame);
        //   savedImage.setDrawingCacheEnabled(true);
        // savedImage.buildDrawingCache();
        // bm = savedImage.getDrawingCache();


        loadBitmapFromView(savedImage);


//        String fileName = String.valueOf(Calendar.getInstance().getTimeInMillis());
//// generate the image path
//        String imagePath = Environment.getExternalStorageDirectory().toString() + File.separator +  fileName + ".png";
//
//        try {
//
//            // save the image as png
//            FileOutputStream out = new FileOutputStream(imagePath);
//            // compress the image to png and pass it to the output stream
//            loadBitmapFromView(savedImage).compress(Bitmap.CompressFormat.PNG, 90, out);
//
//            // save the image
//            out.flush();
//            out.close();
//
//        } catch (Exception error) {
//            Log.e("Error saving image", error.getMessage());
//        }


        //savedImage.destroyDrawingCache();

        File file = saveBitMap(this, savedImage);

        if (file != null) {
            Log.i("TAG", "Drawing saved to the gallery!");
        } else {
            Log.i("TAG", "Oops! Image could not be saved.");
        }


    }


//    private Bitmap convertLayoutToImage() {
//        LinearLayout linearView = (LinearLayout) this.getLayoutInflater(null).inflate(R.layout
//                .popup_job_posts, null, false); //you can pass your xml layout
//
//        linearView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        linearView.layout(0, 0, linearView.getMeasuredWidth(), linearView.getMeasuredHeight());
//
//        linearView.setDrawingCacheEnabled(true);
//        linearView.buildDrawingCache();
//        return linearView.getDrawingCache();// creates bitmap and returns the same
//    }

    private File saveBitMap(Context context, View drawView) {
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Download");


//        if (!pictureFileDir.exists()) {
//            boolean isDirectoryCreated = pictureFileDir.mkdirs();
//            if(!isDirectoryCreated){
//                Log.i("ATG", "Can't create directory to save the image");
//            }
//
//            return null;
//        }


        String filename = pictureFileDir.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        File pictureFile = new File(filename);
        Bitmap bitmap = getBitmapFromView(drawView);
        try {
            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery(context, pictureFile.getAbsolutePath());
        return pictureFile;
    }


    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        } else {
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }


    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
