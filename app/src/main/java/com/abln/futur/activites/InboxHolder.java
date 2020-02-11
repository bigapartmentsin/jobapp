package com.abln.futur.activites;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.PagerAdapter;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.ModelAnaly;

import org.w3c.dom.Text;

public class InboxHolder  extends PagerAdapter {

    private ModelAnaly modelAnaly;
    private Handler handler;

    private Context context;
    public InboxHolder(Context context,ModelAnaly modelAnaly,Handler handler ){
        this.modelAnaly = modelAnaly;
        this.context = context;
        this.handler = handler;

    }
    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view;

        if (position == 0) {

             view = LayoutInflater.from(context).inflate(R.layout.inbox_carview, null);

            ImageView postimage = view.findViewById(R.id.jobPostImageView);
            TextView jt = view.findViewById(R.id.designationTV);
            TextView expTV = view.findViewById(R.id.expTV);
            TextView showTimerIcon = view.findViewById(R.id.showTimerIcon);
            TextView shareBtn = view.findViewById(R.id.shareBtn);
            TextView deleteBtn = view.findViewById(R.id.deleteBtn);

            ImageLoader.loadImage(modelAnaly.first_pic,postimage);
            jt.setText(modelAnaly.job_title);
            expTV.setText("Experience : " + modelAnaly.job_experience_from + " - " + modelAnaly.job_experience_to +" Yrs");




            if (modelAnaly.exp_status.equalsIgnoreCase("1")) {

                jt.setPaintFlags(jt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                expTV.setPaintFlags(expTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
               shareBtn.setVisibility(View.GONE)
               ;
               showTimerIcon.setVisibility(View.GONE);



            }

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    //TODO dilog



                     AlertDialog alert;
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure you want to Delete ?");
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            handler.onDelete(modelAnaly);

                        }
                    });


                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    });


                    alert = builder.create();
                    alert.show();




                }
            });

            showTimerIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIUtility.showToastMsg_withAlertInfoShort(context,"ShowTimer");
                    handler.onTimer(modelAnaly);
                }
            });


            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                   handler.onShare(modelAnaly);
                }
            });

             container.addView(view);
        }else{

             view = LayoutInflater.from(context).inflate(R.layout.analytis_card, null);

                TextView newappnumber = view.findViewById(R.id.newappnumber);
                TextView reviewednumber = view.findViewById(R.id.reviewednumber);
                TextView acceptednumber = view.findViewById(R.id.acceptednumber);
                TextView rejectednumber = view.findViewById(R.id.rejectednumber);
                TextView chattingnumber = view.findViewById(R.id.chattingnumber);
                newappnumber.setText(modelAnaly.full_analytics.param_one);
                reviewednumber.setText(modelAnaly.full_analytics.param_two);
                acceptednumber.setText(modelAnaly.full_analytics.param_three);
                rejectednumber.setText(modelAnaly.full_analytics.param_four);
                chattingnumber.setText(modelAnaly.full_analytics.param_five);
                container.addView(view);
        }



        return view;

    }


    public interface Handler{

      void onDelete(ModelAnaly modelAnaly);
      void onShare(ModelAnaly modelAnaly);
      void onTimer(ModelAnaly modelAnaly);




    }
}
