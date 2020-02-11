package com.abln.futur.common;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;

public abstract class Navpost extends BottomSheetDialog {


    public Navpost(@NonNull Context context) {
        super(context);
    }

    public Navpost(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Navpost(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
