package com.abln.futur.module.global.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.module.chats.adapter.GetAll_UserResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class NonJobSeekerJobsPostAdapter extends RecyclerView.Adapter<NonJobSeekerJobsPostAdapter.MyViewHolder> {

    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public List<GetAll_UserResponse.PatientList> moviesList;
    public boolean isDataRefreshed = false; //Set true when come back from viewer and notifyDataSetChanged() is called
    private boolean isLoadingAdded = false;
    private Context mContext;

    private FolderClickListener mFolderClickListener;

    public NonJobSeekerJobsPostAdapter(Context context, List<GetAll_UserResponse.PatientList> moviesList, FolderClickListener folderClickListener) {
        this.moviesList = moviesList;
        this.mContext = context;
        this.mFolderClickListener = folderClickListener;
    }

    public void add(GetAll_UserResponse.PatientList mc) {
        moviesList.add(mc);
        notifyItemInserted(moviesList.size() - 1);
    }

    public void addAll(List<GetAll_UserResponse.PatientList> mcList) {
        for (GetAll_UserResponse.PatientList mc : mcList) {
            add(mc);
        }
    }

    public void remove(GetAll_UserResponse.PatientList city) {
        int position = moviesList.indexOf(city);
        if (position > -1) {
            moviesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = moviesList.size() - 1;
        GetAll_UserResponse.PatientList item = getItem(position);

        if (item != null) {
            moviesList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public GetAll_UserResponse.PatientList getItem(int position) {
        return moviesList.get(position);
    }

    public void filterList(ArrayList<GetAll_UserResponse.PatientList> filterdNames) {
        this.moviesList.clear();
        this.moviesList.addAll(filterdNames);
        this.moviesList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public NonJobSeekerJobsPostAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stories_list, parent, false);

        return new NonJobSeekerJobsPostAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NonJobSeekerJobsPostAdapter.MyViewHolder holder, int position) {
        final GetAll_UserResponse.PatientList data = moviesList.get(position);


        // UIUtility.loadImage_Picasso(data.getPhoto(),holder.img_story, holder.storyProgress);
        // UIUtility.loadImage_Picasso(data.getPhoto(),holder.img_eye,holder.storyProgress);

        holder.img_eye.setVisibility(View.GONE);

        //Load animation
        final Animation slide_down = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);

        holder.img_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                String _tag = holder.dropDownBtn.getTag().toString();
//                if (_tag.equals("0")) {
//                    holder.dropDownBtn.setTag(1);
//                    holder.details.startAnimation(slide_up);
//                    holder.details.setVisibility(View.VISIBLE);
//                } else {
//                    holder.dropDownBtn.setTag(0);
//                    holder.details.startAnimation(slide_down);
//                    holder.details.setVisibility(View.GONE);
//                }
//                    if(holder.details.getVisibility()==View.VISIBLE){
//                        holder.details.setVisibility(View.GONE);
//                    }else{
//                        holder.details.setVisibility(View.VISIBLE);
//                    }


                mFolderClickListener.onJobPostSelected(data);
            }
        });


        if (getItemCount() > PAGINATION_POSITION && position == getItemCount() - PAGINATION_POSITION) {
            mFolderClickListener.onScrollJobPostsEnd(getItemCount());
        } else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    @UiThread
    public void updateList(List<GetAll_UserResponse.PatientList> newList) {
        moviesList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface FolderClickListener extends Serializable {

        void onScrollJobPostsEnd(int lastPosition);

        //        void oPc (String patientID, String name, String age, String mobile, String gender);
        void onJobPostSelected(GetAll_UserResponse.PatientList position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView img_story, img_eye;
        public ProgressBar storyProgress;


        public MyViewHolder(View view) {
            super(view);
            img_eye = view.findViewById(R.id.imgViewed);
            img_story = view.findViewById(R.id.imgStory);
            storyProgress = view.findViewById(R.id.storyProgress);

        }
    }
}