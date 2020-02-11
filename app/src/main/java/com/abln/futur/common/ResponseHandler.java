package com.abln.futur.common;

public interface ResponseHandler {
    void onSuccess(String response, Object data, int urlId, int position);

    void onFailure(Exception e, int urlId);


    //shows the progress to user
    void showProgress(String text);

    //dismiss the progress
    void dismissProgress();

}
