package com.abln.futur.common.postjobs;

public interface BaseView {

    //shows the progress to user
    void showProgress(String text);

    //dismiss the progress
    void dismissProgress();

    //displaying internet error message to user
    void showInternetError();

    void showErrorDialog(String title, String description, String positiveBtn);


}
