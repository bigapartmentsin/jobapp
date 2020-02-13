package com.abln.futur.common.savedlist;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.UIUtility;
import com.abln.futur.module.job.activities.MyPostedJobListRequest;
import com.abln.futur.module.job.activities.MyjobPost;
import com.abln.futur.services.NetworkOperationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.abln.futur.common.ImageLoader.loadImage;
import static com.abln.futur.common.UIUtility.dateFormatFun;
import static com.abln.futur.common.UIUtility.getAge;

public class newUserListAdapter extends RecyclerView.Adapter<newUserListAdapter.ViewHolder> {



    private final LayoutInflater mInflater;
    clickHandler clickHandler;
    private ArrayList<UserList> mItems;
    private Context mcontext;
    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public static final String DATE_FORMAT = "yyyy/mm/dd";  //or use "M/d/yyyy"
    public static String totaldays;



    public newUserListAdapter(Context mcontext, ArrayList<UserList> items, clickHandler clickHandler) {

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.clickHandler = clickHandler;
        this.mItems = items;

    }


    public static long getDaysBetweenDates(String start, String end) {
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

    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mainGroup = mInflater.inflate(R.layout.my_posted_job_list, parent, false);
        return new ViewHolder(mainGroup);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final UserList data = mItems.get(position);

        if (data.exp_status.equals("1")) {


            holder.designationTV.setPaintFlags(holder.designationTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.experienceTV.setPaintFlags(holder.experienceTV.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }else{
            holder.designationTV.setText(data.job_title);

            String value = "" + data.job_experience_from + " - " + data.job_experience_to + "";
            holder.experienceTV.setText("Experience " + value + " Yrs");
        }

        String img = "https://res.cloudinary.com/medinin/image/upload/v1562649354/futur" ;
        //
        loadImage(img+data.job_post_image10, holder.jobPostImageView);



        if (data.exp_status.equalsIgnoreCase("")) {

            holder.showTimerIcon.setBackground(mcontext.getDrawable(R.drawable.ic_clock_running));
        } else {

            holder.showTimerIcon.setBackground(mcontext.getDrawable(R.drawable.ic_clock_expired));
        }





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
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
        builder.setTitle("Choose Expire After");

// add a list

        builder.setItems(days, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // horse
                    case 1: // cow
                    case 2: // camel
                    case 3: // sheep
                    case 4: // goat


                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void doDelete() {

        MyPostedJobListRequest mFavoriteRquestBody = new MyPostedJobListRequest();
        mFavoriteRquestBody.setApikey("");
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mcontext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getUserJobPost);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mcontext.startService(intent);

    }

    private boolean doUpdateTime() {
        MyjobPost myjobPost = new MyjobPost();
        myjobPost.getData().getUserList().toString();
        return true;
    }


    public String _dateStr;
    private String glDobStr;
    private void dataDialog(String data) {

        final Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(R.layout.dob_select_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        final DatePicker datePicker = dialog.findViewById(R.id.datePicker);
        long now = System.currentTimeMillis() - 1000;
        //  datePicker.setMaxDate((long) (now - (6.6485e+11)));
        TextView doneBtn = dialog.findViewById(R.id.doneBtn);


        doneBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                glDobStr = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
                String value = dateFormatFun(datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear());

                String endDate = glDobStr;
                String currentDate;


                getAge(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());

                 _dateStr = value;


                //TODO error has occured
                clickHandler.updatetime(endDate, _dateStr);

                dialog.dismiss();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/mm/dd");


                Date inputdate = null;
                SimpleDateFormat old = new SimpleDateFormat("d-m-yyyy");
                try {

                    inputdate = old.parse(endDate);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                SimpleDateFormat newformate = new SimpleDateFormat("yyyy/mm/dd");

                String newendtime = newformate.format(inputdate);

                Calendar c = Calendar.getInstance();
                c.setTime(new Date());
                String output = sdf.format(c.getTime());

                getDaysBetweenDates(output, newendtime);

                datedialog(_dateStr + "( " + totaldays + " days)");


            }
        });


        dialog.show();

    }

    private void tochdialog(String date, String data) {


        final Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(R.layout.expire_date_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout selectDate = dialog.findViewById(R.id.datePicker);
        TextView doneBtn = dialog.findViewById(R.id.doneBtn);

        TextView setdate = dialog.findViewById(R.id.setdate);

        System.out.print(date);

        if (date.equalsIgnoreCase("")) {
            setdate.setText("Select Date");
        } else {
            setdate.setText(date);
        }


        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dataDialog(data);
                dialog.dismiss();


            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void datedialog(String value) {
        final Dialog dialog = new Dialog(mcontext);
        dialog.setContentView(R.layout.expire_date_picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        LinearLayout selectDate = dialog.findViewById(R.id.datePicker);
        TextView doneBtn = dialog.findViewById(R.id.doneBtn);

        TextView setdate = dialog.findViewById(R.id.setdate);


        setdate.setText(value);

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();

            }
        });


        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        public ImageView jobPostImageView;
        public TextView designationTV, experienceTV, deleteBtn, shareBtn, showTimerIcon;

        public ViewHolder(@NonNull View view) {
            super(view);
            designationTV = view.findViewById(R.id.designationTV);
            experienceTV = view.findViewById(R.id.expTV);
            deleteBtn = view.findViewById(R.id.deleteBtn);
            shareBtn = view.findViewById(R.id.shareBtn);
            showTimerIcon = view.findViewById(R.id.showTimerIcon);
            jobPostImageView =view.findViewById(R.id.jobPostImageView);



        }
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
