package com.abln.futur.module.global.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.UIUtility;
import com.abln.futur.module.chats.adapter.GetAll_UserResponse;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;


public class JobRoleAdapter extends RecyclerView.Adapter<JobRoleAdapter.MyViewHolder> {

    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public List<GetAll_UserResponse.PatientList> moviesList;
    private int selectedPosition = -1;

    private Context mContext;

    private FolderClickListener mFolderClickListener;

    private String[] resources= new String[] {
            "ic_technology_s",
            "ic_arts___entertainment_",
            "ic_advertising_s",
            "ic_beauty_wellness_s",
            "ic_banking_selected",
            "ic_consulting_s",
            "ic_construction_s",
            "ic_driver__delivery_s",
            "ic_education__academia_s",
            "ic_events__promotion_s",
            "ic_food___beverage_s",
            "ic_government__politics_",
            "ic_healthcare_s",
            "ic_media__journalism_s",
            "ic_manufacturing_selected_new",
            "ic_property_sales__letting_s",
            "ic_travel___hospitality_selected_",
            "ic_vc___investment_",
            "ic_other_s"

    };


    public JobRoleAdapter(Context context, List<GetAll_UserResponse.PatientList> moviesList, FolderClickListener folderClickListener) {
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

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public GetAll_UserResponse.PatientList getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public JobRoleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_roles_list, parent, false);

        return new JobRoleAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobRoleAdapter.MyViewHolder holder, int position) {
        final GetAll_UserResponse.PatientList data = moviesList.get(position);

        if (selectedPosition == position) {



                //            holder.itemView.setSelected(true); //using selector drawable
                holder.rlJobRoles.setBackground(mContext.getDrawable(R.drawable.selected_bac));
                Glide.with(mContext).load(UIUtility.getDrawableIdFromFileName(mContext,data.getUnphoto())).into(holder.jobRoleImg);










        } else {
//            holder.itemView.setSelected(false);
          //  holder.rlJobRoles.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));

            Glide.with(mContext).load(UIUtility.getDrawableIdFromFileName(mContext,data.getPhoto())).into(holder.jobRoleImg);
            holder.rlJobRoles.setBackground(mContext.getDrawable(R.drawable.indu_card));
         //   Glide.with(mContext).load(UIUtility.getDrawableIdFromFileName(mContext,data.getPhoto())).into(holder.jobRoleImg);


        }


        System.out.println("Image Holder"+data.getPhoto());






        final Animation slide_down = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(mContext,
                R.anim.slide_up);


        //TODO jobrolad commented by me  .

        holder.rlJobRoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;



                    data.setUnphoto(resources[selectedPosition]);
                    notifyDataSetChanged();

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

        void onPatientClicked(int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView jobRoleImg;
        public TextView jobRoleText;
        public RelativeLayout rlJobRoles;


        public MyViewHolder(View view) {
            super(view);
            jobRoleImg = view.findViewById(R.id.jobRoleImg);
       //     jobRoleText = view.findViewById(R.id.jobRoleText);
           rlJobRoles = view.findViewById(R.id.main_wrap);

        }
    }
}