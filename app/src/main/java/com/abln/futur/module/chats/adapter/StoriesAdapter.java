package com.abln.futur.module.chats.adapter;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.abln.futur.common.ImageLoader.loadImage;


public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.MyViewHolder> {

    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public List<GetAll_UserResponse.PatientList> moviesList;
    public boolean isDataRefreshed = false; //Set true when come back from viewer and notifyDataSetChanged() is called
    private boolean isLoadingAdded = false;
    private Context mContext;

    private FolderClickListener mFolderClickListener;

    public StoriesAdapter(Context context, List<GetAll_UserResponse.PatientList> moviesList, FolderClickListener folderClickListener) {
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
    public StoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stories_list, parent, false);

        return new StoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StoriesAdapter.MyViewHolder holder, int position) {
        final GetAll_UserResponse.PatientList data = moviesList.get(position);


        loadImage(data.getPhoto(), holder.img_story, holder.storyProgress);
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
        return moviesList.size();
    }

    @UiThread
    public void updateList(List<GetAll_UserResponse.PatientList> newList) {
        moviesList.addAll(newList);
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