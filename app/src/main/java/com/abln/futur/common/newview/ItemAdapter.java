package com.abln.futur.common.newview;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.activites.StoriesActivity;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.Stories;
import com.abln.futur.common.UIUtility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jackandphantom.circularimageview.RoundedImage;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    public static final int SPAN_COUNT_ONE = 1;
    public static final int SPAN_COUNT_THREE = 3;

    private static final int VIEW_TYPE_SMALL = 1;
    private static final int VIEW_TYPE_BIG = 2;

    private final LayoutInflater mInflater;
    clickHandler clickHandler;
    private ArrayList<FinalDataSets> mItems;
    private GridLayoutManager mLayoutManager;
    private Context mcontext;



    public ItemAdapter(Context mcontext, ArrayList<FinalDataSets> items, GridLayoutManager layoutManager, clickHandler clickHandler) {

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.clickHandler = clickHandler;
        this.mItems = items;

        this.mLayoutManager = layoutManager;


    }


    @Override
    public int getItemViewType(int position) {
        int spanCount = mLayoutManager.getSpanCount();
        if (spanCount == SPAN_COUNT_ONE) {
            return VIEW_TYPE_BIG;
        } else {
            return VIEW_TYPE_SMALL;
        }
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_BIG) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_saved_job_list, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_grid_two, parent, false);
        }
        return new ItemViewHolder(view, viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        final FinalDataSets js = mItems.get(position);

        //this is normal grid card ;




        ImageLoader.loadImage(js.jpost_ten,holder.jobPostImageView);


        if (getItemViewType(position) == VIEW_TYPE_BIG) {


            holder.designationTV.setText(js.jtitle);
            holder.postdate.setText(js.dist);
            holder.expTV.setText("Experience : "+js.jexp+" - "+js.jexpt+ " Yrs");

            if (js.jexpstatus.equalsIgnoreCase("1")) {
                holder.applyjob.setBackground(mcontext.getDrawable(R.drawable.ic_applied));
            } else {
                holder.applyjob.setBackground(mcontext.getDrawable(R.drawable.ic_apply_btn));
            }






            holder.jobPostImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO check for stories and pdf


                    clickHandler.redirect(js);

                }
            });


            holder.applyjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(mcontext);
                    dialog.setContentView(R.layout.yes_or_no_dilog_apply);




                    TextView yes = dialog.findViewById(R.id.yest_text);
                    TextView no = dialog.findViewById(R.id.cancle_txt);

                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            clickHandler.checkpdfexist(js);


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


                    //TODO alert builder :



                }
            });

            holder.savedjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickHandler.onapplySave(js);
                    notifyDataSetChanged();


                }
            });


            holder.sharejobpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    clickHandler.share();



                }
            });


            if (js.isstarred.equalsIgnoreCase("0")) {


                holder.savedjob.setBackground(mcontext.getDrawable(R.drawable.star_unsel));


            } else {
                holder.savedjob.setBackground(mcontext.getDrawable(R.drawable.star_selt_one));


            }






        }
        else{
          //  holder.postdate.setText(js.dist);
            ImageLoader.loadImage(js.jpost_ten,holder.jobPostImageView);
            Glide.with(mcontext).load(js.jpost_ten)
                    .fitCenter()
                    .into(holder.jobPostImageView);

            holder.jobPostImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO check for stories and pdf


                    clickHandler.redirect(js);



                }
            });



        }

    }


    @Override
    public int getItemCount() {

        return (null != mItems ? mItems.size() : 0);
    }


    public interface clickHandler {

        void onApplyClick(FinalDataSets searchResult);

        void onapplySave(FinalDataSets searchResult);

        void checkpdfexist(FinalDataSets finalDataSets);


        void redirect(FinalDataSets finalDataSets);

        void share();

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {


        ImageView jobPostImageView;
        TextView designationTV;
        TextView expTV;
        TextView postdate;
        TextView savedjob;
        TextView sharejobpost;
        TextView applyjob;


        //small view
        ItemViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_BIG) {

                jobPostImageView = itemView.findViewById(R.id.jobPostImageView);
                designationTV = itemView.findViewById(R.id.designationTV);
                expTV = itemView.findViewById(R.id.expTV);
                postdate = itemView.findViewById(R.id.postdate);
                savedjob = itemView.findViewById(R.id.savedjob);
                sharejobpost = itemView.findViewById(R.id.sharejobpost);
                applyjob = itemView.findViewById(R.id.applyjob);

            } else {

                jobPostImageView = itemView.findViewById(R.id.jobPostImageViewSmall);
             //   postdate = itemView.findViewById(R.id.postdate);

            }
        }
    }

}
