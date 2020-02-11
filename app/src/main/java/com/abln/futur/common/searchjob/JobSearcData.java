package com.abln.futur.common.searchjob;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.OnitemClickLIstener;
import com.abln.futur.common.UIUtility;

import java.util.ArrayList;

public class JobSearcData extends RecyclerView.Adapter<JobSearcData.MyViewHolder> {


    private final LayoutInflater mInflater;
    private OnitemClickLIstener onItemClickListener;
    private Context mcontext;
    private ArrayList<SearchResult> jobs;
    private clickHandler clickHandler;


    public JobSearcData(Context mcontext, ArrayList<SearchResult> jobs, clickHandler clickHandler) {

        this.jobs = jobs;
        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.clickHandler = clickHandler;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SearchResult js = jobs.get(position);
        holder.designationTV.setText(js.getJobTitle());
        holder.expTV.setText(js.getJobExperience());


        holder.applyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                UIUtility.showToastMsg_withSuccessShort(mcontext, "Successfully");

                new AlertDialog.Builder(mcontext)
                        .setMessage("Are you sure you want to apply")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UIUtility.showToastMsg_withSuccessShort(mcontext, " You update it.");
                                clickHandler.onApplyClick(js);
                            }
                        })


                        .setNegativeButton(android.R.string.no, null)
                        .show();


            }
        });
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View mainGroup = mInflater.inflate(R.layout.my_saved_job_list, parent, false);
        return new MyViewHolder(mainGroup);

    }

    @Override
    public int getItemCount() {
        return (null != jobs ? jobs.size() : 0);
    }


    public interface clickHandler {

        void onApplyClick(SearchResult searchResult);

        void onapplySave(SearchResult searchResult);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView jobPost_image;
        public TextView designationTV;
        public TextView expTV;
        public TextView savedjob;
        public TextView sharejobpost;
        public TextView applyjob;
        public TextView postdate;
        public TextView distanceText;
        private View rowItem;


        public MyViewHolder(View view) {
            super(view);
            jobPost_image = view.findViewById(R.id.jobPostImageView);
            designationTV = view.findViewById(R.id.designationTV);
            expTV = view.findViewById(R.id.expTV);
            savedjob = view.findViewById(R.id.savedjob);
            sharejobpost = view.findViewById(R.id.sharejobpost);
            applyjob = view.findViewById(R.id.applyjob);
            postdate = view.findViewById(R.id.postdate);


            distanceText = view.findViewById(R.id.jobPost_distanceText);

        }
    }


}
