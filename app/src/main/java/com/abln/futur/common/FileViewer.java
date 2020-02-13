package com.abln.futur.common;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.abln.chat.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.net.URI;

public class FileViewer extends AppCompatActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, View.OnClickListener {

    PDFView pdfView;
    String url ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_view);
        Bundle bundle = getIntent().getExtras();
        pdfView = findViewById(R.id.pdfView);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        url = bundle.getString("url");


 System.out.println("PDFVIEWER"+url);
        getUrl(url);

    }





    private void getpdffile(String path){
        File file = new File(URI.create(path).getPath());
        pdfView.fromFile(file)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .onPageChange(this)
                .load();


    }



    public void getUrl(String url){
        Uri path = Uri.fromFile(new File("/storage/self/primary/FuturApp/" + url));
        File file = new File(path.getPath());
        if (file.exists()) {

            System.out.println("PDFVIEWER"+path.toString());
            getpdffile(path.toString());
        }else{
            Toast.makeText(this,"PDF Corrupted",Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {



        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();
    }




}
