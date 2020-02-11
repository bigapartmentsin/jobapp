package com.abln.futur.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;


public class DialogUtils {


    public static void showAlertDialog(Activity activity, String titletext, String posBtnTxt, String negBtntxt, String description, final DialogListener listener) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = (activity).getLayoutInflater();


            if (!TextUtils.isEmpty(titletext)) {
                alertDialogBuilder.setTitle(titletext);
            }

            if (!TextUtils.isEmpty(description)) {
                alertDialogBuilder.setMessage(description);
            }

            if (!TextUtils.isEmpty(negBtntxt)) {
                alertDialogBuilder.setNegativeButton(negBtntxt, (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onNegativeButtonClick();
                    }
                });
            }

            if (!TextUtils.isEmpty(posBtnTxt)) {
                alertDialogBuilder.setPositiveButton(posBtnTxt, (dialog, which) -> {
                    dialog.dismiss();
                    if (listener != null) {
                        listener.onPositiveButtonClick();
                    }
                });
            }

            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialogBuilder.setCancelable(false);
            //  builder.setView(inflater.inflate(R.layout.dialogue, null))
            alertDialog.setCancelable(false);
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface DialogListener {
        void onPositiveButtonClick();

        void onNegativeButtonClick();
    }

}
