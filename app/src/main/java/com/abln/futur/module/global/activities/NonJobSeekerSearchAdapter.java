package com.abln.futur.module.global.activities;

import android.content.Context;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.utils.CircleImageView;
import com.abln.futur.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NonJobSeekerSearchAdapter extends RecyclerView.Adapter<NonJobSeekerSearchAdapter.MyViewHolder> {

    public static final int PAGE_SIZE = 9;
    public static final int PAGINATION_POSITION = PAGE_SIZE / 2;
    public List<NonJobSeekerSearchResult.PatientList> moviesList;
    public List<NonJobSeekerSearchResult.PatientList> userSelectedList;
    public boolean isDataRefreshed = false; //Set true when come back from viewer and notifyDataSetChanged() is called
    private boolean isLoadingAdded = false;
    private Context mContext;

    private NonJobSeekerSearchAdapter.FolderClickListener mFolderClickListener;

    public NonJobSeekerSearchAdapter(Context context, List<NonJobSeekerSearchResult.PatientList> moviesList, NonJobSeekerSearchAdapter.FolderClickListener folderClickListener,
                                     List<NonJobSeekerSearchResult.PatientList> userSelectedList) {
        this.moviesList = moviesList;
        this.mContext = context;
        this.mFolderClickListener = folderClickListener;
        this.userSelectedList = userSelectedList;
    }

    @UiThread
    public void filterList(ArrayList<NonJobSeekerSearchResult.PatientList> filterdNames) {
        this.moviesList = filterdNames;
        notifyDataSetChanged();
    }

    @Override
    public NonJobSeekerSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.persons_list_view_jobseekers, parent, false);

        return new NonJobSeekerSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NonJobSeekerSearchAdapter.MyViewHolder holder, int position) {
        final NonJobSeekerSearchResult.PatientList data = moviesList.get(position);

        // UIUtility.loadImage_Picasso_NoProgerss_local(mContext,data.getPhoto(),holder.userIcon);
        holder.distanceText.setText("" + data.getDistance());
        holder.nameTxt.setText("" + data.getName());
        holder.designationTxt.setText("" + data.getDesignation());
        holder.expTxt.setText("" + data.getExperience());

        if (userSelectedList != null && userSelectedList.contains(data)) {
            holder.radioButton.setChecked(true);
        } else {
            holder.radioButton.setChecked(false);
        }

        holder.rlActionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rlResumeView.getVisibility() == View.VISIBLE) {
//                    holder.rlActionImage.setRotation(90);
                    roateImage(holder.rlActionImage, 360);
                    holder.rlResumeView.setVisibility(View.GONE);
                    notifyDataSetChanged();
                } else {
                    holder.rlResumeView.setVisibility(View.VISIBLE);
//                    holder.rlActionImage.setRotation(90);
                    roateImage(holder.rlActionImage, 180);
                    notifyDataSetChanged();
                }
            }
        });

        holder.closeResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roateImage(holder.rlActionImage, 360);
                holder.rlResumeView.setVisibility(View.GONE);
                notifyDataSetChanged();
            }
        });

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFolderClickListener.radioButtonClicked(position, data);
            }
        });


//        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
////                if(holder.radioButton.isChecked()){
////                    holder.radioButton.setChecked(b);
////                }else{
////                    holder.radioButton.setChecked(true);
////                }
//
////                if(!userSelectedList.contains(mls.get(position))){
////                    userSelectedList.add(mls.get(position));
////                }
//
//                mFolderClickListener.radioButtonClicked(position, data);
//            }
//        });
//        holder.radioButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(holder.radioButton.isChecked()){
//                    holder.radioButton.setChecked(false);
//                }else{
//                    holder.radioButton.setChecked(true);
//                }
//            }
//        });


//        holder.resumePdf.fromUri(Uri.parse("http://www.africau.edu/images/default/sample.pdf"))// all pages are displayed by default
//                .enableSwipe(true) // allows to block changing pages using swipe
//                .swipeHorizontal(false)
//                .defaultPage(0)
//
//                .onTap(new OnTapListener() {
//                    @Override
//                    public boolean onTap(MotionEvent motionEvent) {
//                       // onClickItem();
//                        return false;
//                    }
//                })
//
//                .onPageChange(new OnPageChangeListener() {
//                    @Override
//                    public void onPageChanged(int i, int i1) {
//                       // tvPageCount.setText(i + 1 + " of " + i1);
//
//                    }
//                })
//                // allows to draw something on the current page, usually visible in the middle of the screen
//                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
//                .password(null)
//                .scrollHandle(null)
//                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
//                // spacing between pages in dp. To define spacing color, set view background
//                .spacing(3)
//                .load();

//        holder.jobPost_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mFolderClickListener.oPc(position);
//            }
//        });

        if (getItemCount() > PAGINATION_POSITION && position == getItemCount() - PAGINATION_POSITION) {
            mFolderClickListener.onScrollEnd(getItemCount());
        } else {
            return;
        }

    }

    private void roateImage(ImageView imageView, float angle) {
        Matrix matrix = new Matrix();
        imageView.setScaleType(ImageView.ScaleType.MATRIX); //required
        matrix.postRotate(angle, imageView.getDrawable().getBounds().width() / 2, imageView.getDrawable().getBounds().height() / 2);
        imageView.setImageMatrix(matrix);
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public interface FolderClickListener extends Serializable {

        void onScrollEnd(int lastPosition);

        //        void oPc (String patientID, String name, String age, String mobile, String gender);
        void onPatientClicked(int position);

        void radioButtonClicked(int position, NonJobSeekerSearchResult.PatientList userLists);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public CircleImageView userIcon;
        public ImageView rlActionImage, closeResume;
        public TextView distanceText, nameTxt, designationTxt, expTxt;
        public TextView resumePdf;
        public RelativeLayout rlResumeView;
        public RadioButton radioButton;
        public boolean radioClicked;


        public MyViewHolder(View view) {
            super(view);
            userIcon = view.findViewById(R.id.userIcon);
            distanceText = view.findViewById(R.id.distance);
            nameTxt = view.findViewById(R.id.userNameText);
            designationTxt = view.findViewById(R.id.userDesignation);
            expTxt = view.findViewById(R.id.totalExp);
            rlActionImage = view.findViewById(R.id.rlActionImage);
            rlResumeView = view.findViewById(R.id.resumeViewLayout);
            resumePdf = view.findViewById(R.id.pdfResumeView);
            closeResume = view.findViewById(R.id.pdfclose);
            radioButton = view.findViewById(R.id.radioBtnSelectUser);

        }
    }

//    @UiThread
//    public void updateList(List<GetAll_UserResponse.PatientList> newList) {
//        mls.addAll(newList);
//        notifyDataSetChanged();
//    }
}
