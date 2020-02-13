package com.abln.futur.common.savedlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.UIUtility;

import java.util.ArrayList;

public class SavedItem extends RecyclerView.Adapter<SavedItem.ViewHolder> {


    private final LayoutInflater mInflater;
    clickHandler clickHandler;
    private ArrayList<Savedlist> mItems;
    private Context mcontext;


    public SavedItem(Context mcontext, ArrayList<Savedlist> items, clickHandler clickHandler) {

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.clickHandler = clickHandler;
        this.mItems = items;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mainGroup = mInflater.inflate(R.layout.new_saved_data, parent, false);
        return new ViewHolder(mainGroup);


    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Savedlist foo = mItems.get(position);
        holder.savedexperience.setText("Experience :" + foo.job_experience_from + " - " + foo.job_experience_to +" Yrs");
        holder.savedtitle.setText(foo.job_title);
        holder.savedposteddate.setText(foo.nodays);
        ImageLoader.loadImage(foo.job_post_image10,holder.saveimageview);



        //image view click;


        if (foo.exp_status.equalsIgnoreCase("1")) {


            holder.savedtitle.setPaintFlags(holder.savedtitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            holder.savedexperience.setPaintFlags(holder.savedexperience.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);



        }


        holder.saveimageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clickHandler.imageClick(foo);
            }
        });




        holder.savedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                clickHandler.onRemoveSaved(foo);
                notifyDataSetChanged();


            }
        });


        holder.sharejobpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clickHandler.share();


            }
        });



        

        if (foo.astatus.equalsIgnoreCase("0")) {

            holder.savedapplyjob.setBackground(mcontext.getDrawable(R.drawable.ic_apply_btn));

            //holder.applyjob.setBackground(mcontext.getDrawable(R.drawable.ic_apply_btn));

        } else {

            holder.savedapplyjob.setBackground(mcontext.getDrawable(R.drawable.ic_applied));
            holder.savedapplyjob.setClickable(false);

        }





        holder.savedapplyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (foo.exp_status.equalsIgnoreCase("1")) {

                    UIUtility.showToastMsg_withErrorShort(mcontext,"You cannot apply to an expired post ");

                }else{

                    clickHandler.onapplyClick(foo);
                    notifyDataSetChanged();

                }






            }
        });


    }

    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }

    public interface clickHandler {

        void onapplyClick(Savedlist savedlist);

        void onRemoveSaved(Savedlist savedlist);

        void imageClick(Savedlist savedlist);

        void share();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View rowItem;

        private ImageView saveimageview;
        private TextView savedjob;
        private TextView sharejobpost;
        private TextView savedexperience;
        private TextView savedtitle;
        private TextView savedapplyjob;
        private TextView savedposteddate;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            saveimageview = itemView.findViewById(R.id.saveimageview);
            savedjob = itemView.findViewById(R.id.savedjob);
            sharejobpost = itemView.findViewById(R.id.sharejobpost);
            savedexperience = itemView.findViewById(R.id.savedexperience);
            savedtitle = itemView.findViewById(R.id.savedtitle);
            savedapplyjob = itemView.findViewById(R.id.applyjobnew);
            savedposteddate = itemView.findViewById(R.id.savedposteddate);


        }
    }

}
