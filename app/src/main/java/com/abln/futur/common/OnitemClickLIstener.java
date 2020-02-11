package com.abln.futur.common;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by sumeeth.
 */
public interface OnitemClickLIstener {

    void onItemClick(RecyclerView.Adapter adapter, View view, int position);

}
