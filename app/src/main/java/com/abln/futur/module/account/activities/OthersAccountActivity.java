package com.abln.futur.module.account.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.PrefManager;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OthersAccountActivity extends BaseActivity {

    @BindView(R.id.linkedin_edit_text)
    TextInputEditText etLinkedUrl;

    @BindView(R.id.other1_edit_text)
    TextInputEditText etOther1;

    @BindView(R.id.other2_edit_text)
    TextInputEditText etOther2;

    @BindView(R.id.other3_edit_text)
    TextInputEditText etOther3;

    private PrefManager prefManager = new PrefManager();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_account);

        ButterKnife.bind(this);
        mContext = this;

        etLinkedUrl.setText(prefManager.getLinkedinurl());
        etOther1.setText(prefManager.getOther1());
        etOther2.setText(prefManager.getOther2());
        etOther3.setText(prefManager.getOther3());

        etLinkedUrl.setSelection(etLinkedUrl.length());
        etOther1.setSelection(etOther1.length());
        etOther2.setSelection(etOther2.length());
        etOther3.setSelection(etOther3.length());

        etLinkedUrl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etLinkedUrl.getRight() - etLinkedUrl.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        copyToClipBoard(etLinkedUrl);

                        return true;
                    }
                }
                return false;
            }
        });

        etOther1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etOther1.getRight() - etOther1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        copyToClipBoard(etOther1);

                        return true;
                    }
                }
                return false;
            }
        });


        etOther2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etOther2.getRight() - etOther2.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        copyToClipBoard(etOther2);

                        return true;
                    }
                }
                return false;
            }
        });

        etOther3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etOther3.getRight() - etOther3.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        copyToClipBoard(etOther3);

                        return true;
                    }
                }
                return false;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        prefManager.setLinkedinurl(etLinkedUrl.getText().toString());
        prefManager.setOther1(etOther1.getText().toString());
        prefManager.setOther2(etOther2.getText().toString());
        prefManager.setOther3(etOther3.getText().toString());
    }

    private void copyToClipBoard(EditText text) {
        ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", text.getText());
        clipMan.setPrimaryClip(clip);
    }


}
