package com.abln.futur.customViews;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.abln.futur.R;

import java.util.ArrayList;

public class CustomDialog extends Dialog {
    private Context mContext;
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView tvIcon;
    private ListView lvListView;

    private Button btnNegative;
    private Button btnPositive;

    private ArrayAdapter<String> arrayAdapter;

    public CustomDialog(@NonNull Context context) {
        super(context, R.style.Theme_Dialog);
        mContext = context;
        init();
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
        init();
    }

    protected CustomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        mContext = context;
        init();
    }

    private void init() {
        setContentView(R.layout.view_dialog);
        tvTitle = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        tvIcon = findViewById(R.id.tvIcon);
        lvListView = findViewById(R.id.lvListNames);

        btnNegative = findViewById(R.id.btnNegative);
        btnPositive = findViewById(R.id.btnPositive);
    }

    public CustomDialog setNegativeButton(String text, View.OnClickListener listener) {
        btnNegative.setVisibility(View.VISIBLE);
        btnNegative.setText(text);
        btnNegative.setOnClickListener(listener);
        return this;
    }

    public CustomDialog setPositiveButton(String text, View.OnClickListener listener) {
        btnPositive.setVisibility(View.VISIBLE);
        btnPositive.setText(text);
        btnPositive.setOnClickListener(listener);
        return this;
    }


    public void setListItems(ArrayList<String> strList) {
        lvListView.setVisibility(View.VISIBLE);
        arrayAdapter = new ArrayAdapter(mContext, R.layout.activity_email_listview, strList);
        lvListView.setAdapter(arrayAdapter);
    }

    public void setTitle(int titleId) {
        tvTitle.setText(titleId);
    }

    public void setTitle(@Nullable CharSequence title) {
        tvTitle.setText(title);
    }


    public void setMessage(int titleId) {
        tvMessage.setText(titleId);
    }


    public void setMessage(@Nullable CharSequence title) {
        tvMessage.setText(title);
    }

    /**
     * @param iconId CLEAR Font string id
     */
    public void setIcon(int iconId) {
        tvIcon.setVisibility(View.VISIBLE);
        tvIcon.setText(iconId);
    }
}
