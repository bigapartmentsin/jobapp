package com.abln.futur.module.job.activities;

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
import com.abln.futur.common.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MySavedJobPostsAdapter extends RecyclerView.Adapter<MySavedJobPostsAdapter.MyViewHolder> {

    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public static final String TAG = MySavedJobPostsAdapter.class.getSimpleName();
    public List<MyjobPost.UserList> ls;
    public boolean isDataRefreshed = false; //Set true when come back from viewer and notifyDataSetChanged() is called
    private boolean isLoadingAdded = false;
    private Context mContext;

    private MySavedJobPostsAdapter.FolderClickListener mFolderClickListener;

    public MySavedJobPostsAdapter(Context context, List<MyjobPost.UserList> ls, MySavedJobPostsAdapter.FolderClickListener folderClickListener) {
        this.ls = ls;
        this.mContext = context;
        this.mFolderClickListener = folderClickListener;
    }

    public void add(MyjobPost.UserList mc) {
        ls.add(mc);
        notifyItemInserted(ls.size() - 1);
    }

    public void addAll(List<MyjobPost.UserList> mcList) {
        for (MyjobPost.UserList mc : mcList) {
            add(mc);
        }
    }

    public void remove(MyjobPost.UserList city) {
        int position = ls.indexOf(city);
        if (position > -1) {
            ls.remove(position);
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

        int position = ls.size() - 1;
        MyjobPost.UserList item = getItem(position);

        if (item != null) {
            ls.remove(position);
            notifyItemRemoved(position);
        }
    }

    public MyjobPost.UserList getItem(int position) {
        return ls.get(position);
    }

    public void filterList(ArrayList<MyjobPost.UserList> filterdNames) {
        this.ls.clear();
        this.ls.addAll(filterdNames);
        this.ls = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public MySavedJobPostsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_saved_job_list, parent, false);

        return new MySavedJobPostsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MySavedJobPostsAdapter.MyViewHolder holder, int position) {
        final MyjobPost.UserList data = ls.get(position);


        ImageLoader.loadImage(data.getJobPostImage1(), holder.img_story, holder.storyProgress);
        // UIUtility.loadImage_Picasso(data.getPhoto(),holder.img_eye,holder.storyProgress);

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
        return ls.size();
    }

    @UiThread
    public void updateList(List<MyjobPost.UserList> newList) {
        ls.addAll(newList);
        notifyDataSetChanged();
    }

    public interface FolderClickListener extends Serializable {

        void onScrollEnd(int lastPosition);

        //        void oPc (String patientID, String name, String age, String mobile, String gender);
        void onPatientClicked(int position);

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
