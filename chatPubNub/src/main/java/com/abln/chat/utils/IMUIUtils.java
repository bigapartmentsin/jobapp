package com.abln.chat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.abln.chat.R;

import java.util.Locale;

import androidx.appcompat.app.AlertDialog;


public class IMUIUtils {

    public static void showAlert(Context context, String title, String message) {
        showAlert(context, title, message, 0);
    }

    public static void showAlert(Context context, String title, String message, int icon) {
        showAlert(context, title, message, icon, null, null);
    }

    public static void showAlert(Context context, String title, String message, int icon,
                                 String positiveButton, OnClickListener positiveButtonListener) {
        showAlert(context, title, message, icon, positiveButton, positiveButtonListener, null, null, null);
    }

    public static void showAlert(Context context, String title, String message, int icon,
                                 String positiveButton, OnClickListener positiveButtonListener,
                                 OnCancelListener cancelListener) {
        showAlert(context, title, message, icon, positiveButton, positiveButtonListener, null, null, cancelListener);
    }

    public static void showAlert(Context context, String title, String message, int icon,
                                 String positiveButton, OnClickListener positiveButtonListener,
                                 String negativeButton, OnClickListener negativeButtonListener,
                                 OnCancelListener cancelListener) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message);

        if (icon != 0) {
            builder.setIcon(context.getResources().getDrawable(icon));
        }

        if (null == positiveButton) {
            positiveButton = context.getResources().getString(R.string.im_ok_txt);
        }

        builder.setPositiveButton(positiveButton, positiveButtonListener);

        if (null != negativeButton) {
            builder.setNegativeButton(negativeButton, negativeButtonListener);
        }

        if (null != cancelListener) {
            builder.setOnCancelListener(cancelListener);
        }

        if (null != negativeButton && null != positiveButton) {
            builder.setCancelable(false);

        } else {
            builder.setCancelable(true);
        }

        builder.create().show();
    }

    public static void showSoftKeyboardForWindow(Context context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).
                toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.SHOW_FORCED);
    }

    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Spannable getHighlightedSpan(String filteredText, String text) {
        Spannable spannable = new SpannableString(text);

        if (!IMUtils.isNullOrEmpty(filteredText)) {
            int startPos = text.toLowerCase(Locale.US).indexOf(filteredText.toLowerCase(Locale.US));
            int endPos = startPos + filteredText.length();

            if (startPos != -1) {
                ColorStateList blackColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#05D4F3")});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blackColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new UnderlineSpan(), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return spannable;
    }

    // convert px to dp
    public static int convertPixelsToDp(Context context, float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                px, context.getResources().getDisplayMetrics());
    }
}
