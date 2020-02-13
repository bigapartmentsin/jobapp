package com.abln.futur.common;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.savedlist.Savedlist;
import com.abln.futur.common.savedlist.UserList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import static com.abln.futur.common.ImageLoader.loadImage;

public class PostedAdapter   extends RecyclerView.Adapter<PostedAdapter.ListHolder> implements Filterable {




    private final LayoutInflater mInflater;
    private ArrayList<UserList> mItems;
    private Context mcontext;
    clickHandler clickHandler;

    public static final String DATE_FORMAT = "yyyy/mm/dd";  //or use "M/d/yyyy"
    public static String totaldays;












    public PostedAdapter(Context mcontext, ArrayList<UserList> mItems, clickHandler clickHandler) {

        this.mcontext = mcontext;
        this.mItems = mItems;
        this.clickHandler = clickHandler;
        mInflater = LayoutInflater.from(mcontext);


    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View mainGroup = mInflater.inflate(R.layout.my_posted_job_list, parent, false);
        return new ListHolder(mainGroup);


    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {


        final UserList data = mItems.get(position);

        String img = "https://res.cloudinary.com/medinin/image/upload/v1562649354/futur" ;
        loadImage(img+data.job_post_image10, holder.jobPostImageView);


        holder.designationTV.setText(data.job_title);
        String value = "" + data.job_experience_from + " - " + data.job_experience_to + "";
        holder.experienceTV.setText("Experience " + value + " Yrs");






        if (data.exp_status.equalsIgnoreCase("")) {

            holder.showTimerIcon.setBackground(mcontext.getDrawable(R.drawable.ic_clock_running));
        } else {

            holder.showTimerIcon.setBackground(mcontext.getDrawable(R.drawable.ic_clock_expired));
        }



        if (data.exp_status.equals("1")) {
            holder.designationTV.setPaintFlags(holder.designationTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.experienceTV.setPaintFlags(holder.experienceTV.getPaintFlags()    | Paint.STRIKE_THRU_TEXT_FLAG);



        } else {
            holder.designationTV.setPaintFlags(holder.designationTV.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.experienceTV.setPaintFlags(holder.experienceTV.getPaintFlags()    & (~Paint.STRIKE_THRU_TEXT_FLAG));

        }





        // event handler


        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickHandler.share();

                //
            }
        });



        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.yes_or_no_dilog);


                TextView yes = dialog.findViewById(R.id.yest_text);
                TextView no = dialog.findViewById(R.id.cancle_txt);



                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {





                        clickHandler.updatedelete(data);

                        notifyDataSetChanged();


                        dialog.dismiss();





                    }
                });


                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        });


        holder.showTimerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

                Date inputdate = null;

                try {

                    inputdate = sdf.parse(data.created_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat newformate = new SimpleDateFormat("yyyy/MM/dd");

                String newendtime = newformate.format(inputdate);


                // change the user end   25-12-2019;
                SimpleDateFormat nef = new SimpleDateFormat("dd-MM-yyyy");
                Date changeDate = null;

                try {

                    changeDate = nef.parse(data.job_exp_date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String lastdate = newformate.format(changeDate);


                getDaysBetweenDates(lastdate, newendtime);
                long diff = changeDate.getTime() - inputdate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;
                int totalleftdays = (int) days;

                switch (data.dcount) {
                    case "5":
                        String[] ds = {"10 Days", "15 Days", "20 Days", "25 Days", "30 Days"};
                        alertbuilder(ds);
                        break;


                    case "10":

                        String[] ad = {"15 Days", "20 Days", "25 Days", "30 Days"};
                        alertbuilder(ad);
                        break;
                    case "15":
                        String[] va = {"20 Days", "25 Days", "30 Days"};
                        alertbuilder(va);
                        break;
                    case "20":
                        String[] da = {"25 Days", "30 Days"};
                        alertbuilder(da);
                        break;
                    case "25":
                        String[] du = {"30 Days"};
                        alertbuilder(du);
                        break;



                }





            }
        });


        holder.jobPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.imageClick(data);
            }
        });








    }


    private void alertbuilder(String[] days) {


        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Choose Expire After");


        builder.setItems(days, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:


                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }






    @Override
    public int getItemCount() {

        return (null != mItems ? mItems.size() : 0);

    }









    public class ListHolder extends RecyclerView.ViewHolder {




        public ImageView jobPostImageView;
        public TextView designationTV, experienceTV, deleteBtn, shareBtn, showTimerIcon,repost;
        private View rowItem;
        public ListHolder(View itemView) {
            super(itemView);

            designationTV = itemView.findViewById(R.id.designationTV);
            experienceTV = itemView.findViewById(R.id.expTV);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            showTimerIcon = itemView.findViewById(R.id.showTimerIcon);
            jobPostImageView =itemView.findViewById(R.id.jobPostImageView);
            repost = itemView.findViewById(R.id.repost);


        }
    }





    public  long getDaysBetweenDates(String start, String end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        Date startDate, endDate;
        long numberOfDays = 0;
        try {
            startDate = dateFormat.parse(start);
            endDate = dateFormat.parse(end);
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.print("Difference between two dates " + numberOfDays);
        totaldays = String.valueOf(numberOfDays);
        return numberOfDays;
    }




    private  long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }



    public interface clickHandler {

        void onapplyClick(Savedlist savedlist);
        void onRemoveSaved(Savedlist savedlist);
        void share();
        void updatetime(String myjobpos, String vat);
        void updatedelete(UserList myjobPost);
        void imageClick(UserList myjobPost);
    }






}
