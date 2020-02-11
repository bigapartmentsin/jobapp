package com.abln.futur.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.abln.futur.R;

import java.util.ArrayList;

public class LocationAdapter extends PagerAdapter {

    private Context context;
    private ArrayList<Addinfo> locaton;
    private oneventclick oneventclick;

    public LocationAdapter(Context context, ArrayList<Addinfo> locaton, oneventclick event) {
        this.context = context;
        this.locaton = locaton;
        this.oneventclick = event;
    }

    @Override
    public int getCount() {
        return (null != locaton ? locaton.size() : 0);
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_pager_address, null);
        TextView textView = view.findViewById(R.id.location_data);

        Addinfo in = locaton.get(position);
//pointer

        textView.setText(in.location);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withSuccessShort(context, in.location);
                // oneventclick.sendData(in.location);
                oneventclick.onClickyes(in.location);
            }
        });


        // int currentItem = locaton.getCurrentItem();
        //    Addinfo infofiles = locaton.get(position);

        oneventclick.onClickyes(in.location);
        container.addView(view);

        return view;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    public interface oneventclick {


        void onClickyes(String postion);

        void sendData();
    }


}
