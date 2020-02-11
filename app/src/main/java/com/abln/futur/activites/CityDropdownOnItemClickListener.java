package com.abln.futur.activites;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

public class CityDropdownOnItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {


        // get the context and main activity to access variables
        Context mContext = v.getContext();
        DashboardActivity mainActivity = ((DashboardActivity) mContext);

        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);

        // dismiss the pop up
        mainActivity.popupWindowCity.dismiss();

        // get the text and set it as the button text
        String selectedItemText = ((TextView) v).getText().toString();
        mainActivity.epEditSpinner.setText(selectedItemText);

        // get the id
        String selectedItemTag = ((TextView) v).getTag().toString();
        Toast.makeText(mContext, "City ID is: " + selectedItemTag, Toast.LENGTH_SHORT).show();

    }
}
