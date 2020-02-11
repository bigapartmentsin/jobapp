package com.abln.futur.common.searchjob;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.OnitemClickLIstener;
import com.abln.futur.common.UIUtility;

import java.util.ArrayList;

public class Gridadapter extends RecyclerView.Adapter<Gridadapter.MyViewHolder> {

    private final LayoutInflater mInflater;
    private OnitemClickLIstener onItemClickListener;
    private Context mcontext;
    private ArrayList<SearchResult> jobs;


    public Gridadapter(Context context, ArrayList<SearchResult> jobs) {

        this.jobs = jobs;
        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final SearchResult js = jobs.get(position);
        holder.designationTV.setText(js.getJobTitle());
        holder.expTV.setText(js.getJobExperience());


        //  holder.applyjob.setOnClickListener(new View.OnClickListener() {


        //}


        holder.applyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(mcontext, "Appled Stories ");

                makeStories();

            }
        });


    }

    public void makeStories() {
        Intent i = new Intent(mcontext, Search.class);
        mcontext.startActivity(i);
        Intent nedata = new Intent(mcontext, Result.class);
        mcontext.checkPermission("", 1, 3);
    }


    @Override
    public boolean onFailedToRecycleView(@NonNull MyViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
    }


    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
    }


    @Override
    public void onViewAttachedToWindow(@NonNull MyViewHolder holder) {
        super.onViewAttachedToWindow(holder);
    }


    public void setJobs(ArrayList<SearchResult> jobs) {
        this.jobs = jobs;
    }


    public LayoutInflater getmInflater() {
        return mInflater;
    }

    @NonNull

    public Filter getFilter() {
        Filter dataFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    filterResults.values = "";
                    filterResults.count = 5;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && (results.count > 0)) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }


        };
        return dataFilter;
    }

    private void notifyDataSetInvalidated() {


    }


    @Override
    public int getItemCount() {

        return 0;


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
