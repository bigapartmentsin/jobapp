package com.abln.futur.utils;

import android.content.Context;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.UIUtility;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class NetworkPdfviewr extends AppCompatActivity implements DownloadFile.Listener
{

    String url= "https://res.cloudinary.com/medinin/image/upload/v1562649354/futur";
    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    FrameLayout frame;
    PDFPagerAdapter adapter;
    final Context ctx = this;
    final DownloadFile.Listener listener = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.urlpdfloader);
        Bundle bundle = getIntent().getExtras();
        String   val = bundle.getString("pdf");
        String name = bundle.getString("name");
        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        frame = (FrameLayout) findViewById(R.id.event_wraper);
        remotePDFViewPager = new RemotePDFViewPager(ctx,url+val, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        UIUtility.showToastMsg_withErrorShort(this,"Error in Downloading");
    }


    @Override
    public void onProgressUpdate(int progress, int total) {

    }


    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }




    public void updateLayout() {
        root.removeAllViewsInLayout();
//        root.addView(remotePDFViewPager,
//                LinearLayout.LayoutParams.MATCH_PARENT, 700);
        root.addView(frame,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(remotePDFViewPager,LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);


    }



}
