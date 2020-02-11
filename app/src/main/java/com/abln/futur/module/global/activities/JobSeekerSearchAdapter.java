package com.abln.futur.module.global.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;

import java.io.Serializable;
import java.util.List;

public class JobSeekerSearchAdapter extends RecyclerView.Adapter<JobSeekerSearchAdapter.MyViewHolder> {


    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public List<JobSeekerSearchResult.PatientList> moviesList;
    public boolean isDataRefreshed = false; //Set true when come back from viewer and notifyDataSetChanged() is called
    private boolean isLoadingAdded = false;
    private Context mContext;

    private FolderClickListener mFolderClickListener;

    public JobSeekerSearchAdapter(Context context, List<JobSeekerSearchResult.PatientList> moviesList, FolderClickListener folderClickListener) {
        this.moviesList = moviesList;
        this.mContext = context;
        this.mFolderClickListener = folderClickListener;
    }

    @Override
    public JobSeekerSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_view_grid, parent, false);

        return new JobSeekerSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobSeekerSearchAdapter.MyViewHolder holder, int position) {
        final JobSeekerSearchResult.PatientList data = moviesList.get(position);

        //   loadImage(mContext,data.getPhoto(),holder.jobPost_image);
        holder.distanceText.setText("" + data.getPatientId());

        holder.jobPost_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFolderClickListener.onPatientClicked(position);
            }
        });

        if (getItemCount() > PAGINATION_POSITION && position == getItemCount() - PAGINATION_POSITION) {
            mFolderClickListener.onScrollEnd(getItemCount());
        } else {
            return;
        }

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }


    public interface FolderClickListener extends Serializable {

        void onScrollEnd(int lastPosition);

        //        void oPc (String patientID, String name, String age, String mobile, String gender);
        void onPatientClicked(int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView jobPost_image;
        public TextView distanceText;


        public MyViewHolder(View view) {
            super(view);
            jobPost_image = view.findViewById(R.id.jobPost_image);
            distanceText = view.findViewById(R.id.jobPost_distanceText);

        }
    }

//    @UiThread
//    public void updateList(List<GetAll_UserResponse.PatientList> newList) {
//        mls.addAll(newList);
//        notifyDataSetChanged();
//    }
}
