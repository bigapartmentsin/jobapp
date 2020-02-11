package com.abln.futur.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestThumbnail extends AppCompatActivity {


    Button imagesave;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_layout);
        imagesave = findViewById(R.id.saveclick);

        imagesave.performClick();

        RelativeLayout savingLayout = findViewById(R.id.idForSaving);
        File file = saveBitMap(TestThumbnail.this, savingLayout);
        if (file != null) {
            // pd.cancel();
            Log.i("TAG", "Drawing saved to the gallery!");
        } else {
            // pd.cancel();
            Log.i("TAG", "Oops! Image could not be saved.");
        }

        //    pd = new ProgressDialog(TestThumbnail.this);


    }

    public void SaveClick(View view) {
        pd.setMessage("saving your image");
        pd.show();

    }

    private File saveBitMap(Context context, View drawView) {


        //   File pictureFileDir = new File(Environment.getExternalStorageDirectory()+"/AppName");


        //  File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        // File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;


//
//
//        if (!pictureFileDir.exists()) {
//            boolean isDirectoryCreated = pictureFileDir.mkdirs();
//            if(!isDirectoryCreated)
//                Log.i("TAG", "Can't create directory to save the image");
//            return null;
//        }


        String filename = File.separator + System.currentTimeMillis() + ".jpg";
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

    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
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


    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue scanning gallery.");
        }
    }


}
