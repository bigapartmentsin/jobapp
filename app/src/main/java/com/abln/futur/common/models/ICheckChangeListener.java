package com.abln.futur.common.models;


public interface ICheckChangeListener {

    void onItemChecked(int position, boolean value);

    void onItemApplyChecked(int position, boolean value);


    void onItemStarCheck(int position, boolean value);

}
