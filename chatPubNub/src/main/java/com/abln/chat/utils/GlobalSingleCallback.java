package com.abln.chat.utils;

import android.content.Context;


import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;


public abstract class GlobalSingleCallback<T> extends DisposableSingleObserver<T> {

    boolean isShowProgress;
    //  private final BaseView view;
    private Context mContext;

    public GlobalSingleCallback(BaseView view) {
        this(true, "Please wait...", view);
    }

    public GlobalSingleCallback(boolean isShowProgress, BaseView view) {

        //   this(isShowProgress, "Please wait...", view);


    }

    public GlobalSingleCallback(boolean isShowProgress, String progressTxt, BaseView view) {
        //   this.view = view;
        this.isShowProgress = isShowProgress;


//        if (isShowProgress) {
//            view.showProgress(progressTxt);
//        }


    }


    public abstract void onApiSuccess(BaseResponse baseResponse);

    public void onDataSuccess(T t) {
    }


    public abstract void onFailure(Throwable e);


    @Override
    public void onSuccess(@NonNull T t) {
        //  view.dismissProgress();
        if (t instanceof BaseResponse) {
            BaseResponse baseResponse = (BaseResponse) t;
            onApiSuccess(baseResponse);
        } else {
            onDataSuccess(t);
        }
    }


    @Override
    public void onError(@NonNull Throwable e) {
        //   view.dismissProgress();
        onFailure(e);
    }


}
