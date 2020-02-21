package com.abln.futur;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.abln.futur.activites.BaseActivity;
import com.abln.futur.interfaces.TaskCompleteListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Handshake;

public class DuplicateActivity extends BaseActivity implements TaskCompleteListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.linkedin_url_text)
    EditText linkedinurl;
    @BindView(R.id.copy_image_four)
    ImageView ivFour;
    @BindView(R.id.copy_image)
    ImageView ivOne;
    @BindView(R.id.copy_image_two)
    ImageView ivTwo;
    @BindView(R.id.copy_image_three)
    ImageView ivThree;
    @BindView(R.id.other_new_textview)
    EditText editTwo;
    @BindView(R.id.other_new_edittext)
    EditText editThree;
    @BindView(R.id.other_new_edittextthre)
    EditText editFour;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_accounts_page);


        useData();
        linkedinurl.setText("welcomebaby");
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        ivOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String text = linkedinurl.getText().toString();
                myClip = ClipData.newPlainText("text", text);
                myClipboard.setPrimaryClip(myClip);
                Toast.makeText(getApplicationContext(), "Text Copied",
                        Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplication(), text, Toast.LENGTH_LONG).show();
            }
        });


        ivTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }



    private void useData() {
        ButterKnife.bind(this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
    }










    // creating



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void getVersion(){




         }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getDeviceInformation(){







    }










    public void sendingINfor(){


        //
    }


}
