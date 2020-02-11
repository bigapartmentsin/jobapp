package com.abln.futur.utils;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RawRes;
import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;
import com.abln.futur.activites.Demo;
import com.abln.futur.common.UIUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.BasePDFPagerAdapter;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Pdfviewer extends AppCompatActivity {

    private PdfRenderer mPdfRenderer;
    private PdfRenderer.Page mPdfPage;
    private static final String FILE_NAME = "pdfview.pdf";

    private ImageView mImageView;

    //update ur l




    PDFViewPager pdfViewPager;
    BasePDFPagerAdapter adapter;

  //  RemotePDFViewPager remotePDFViewPager;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_showcase);

        pdfViewPager = (PDFViewPager) findViewById(R.id.pdfViewPager);


//file:///storage/self/primary/FuturApp/pdf.pdf


        downloadForm("http://www.pdf995.com/samples/pdf.pdf","pdf.pdf");

//String url ="http://www.pdf995.com/samples/pdf.pdf" ;
//        remotePDFViewPager =
//                new RemotePDFViewPager(this, url, this);






    }









    // new function added here ;

    private class GetFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FuturApp");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);




            try {
                //    pdfFile.createNewFile();

                pdfFile.createNewFile();


            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, pdfFile);

            viewPdfFile(fileName);
            return null;
        }
    }



    private void downloadForm(String Url_path, String file_name) {
        new Pdfviewer.GetFiles().execute(Url_path, file_name);
    }


    private static final int MEGABYTE = 1024 * 1024;

    public static void downloadFile(String fileUrl, File directory) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }


            fileOutputStream.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }









    private void viewPdfFile(String pdfFile) {
        try {


            Uri path = Uri.fromFile(new File("/storage/self/primary/FuturApp/" + pdfFile));

System.out.println("getting the downloaded path here "+path);


            String p = getFilePathFromURI(this, path);

          String   pdfFilePath = p;

          System.out.println("new path "+pdfFilePath);


           // adapter = new PDFPagerAdapter(this, path.getPath());
            //pdfViewPager.setAdapter(adapter);


            PDFViewPager pdfViewPager = new PDFViewPager(this,pdfFilePath);
            setContentView(pdfViewPager);



//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setDataAndType(path, "application/pdf");
//            startActivity(intent);



        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "There is no any PDF Viewer", Toast.LENGTH_LONG).show();
        }

    }



    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ((PDFPagerAdapter) pdfViewPager.getAdapter()).close();
    }


}
































