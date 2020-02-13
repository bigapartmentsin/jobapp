package com.abln.chat.ui.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.R;
import com.abln.chat.utils.ModChat;
import com.abln.chat.utils.Models;
import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class JobDataAdapter  extends  RecyclerView.Adapter<JobDataAdapter.ViewHolder>  {



    private final LayoutInflater mInflater;

    private ArrayList<Models> mItems;
    private Context mcontext;
   clickHandler handler ;


    public JobDataAdapter(Context mcontext, ArrayList<Models> items,clickHandler handler) {

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.handler = handler;
        this.mItems = items;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mainGroup = mInflater.inflate(R.layout.popup_data_card, parent, false);
        return new ViewHolder(mainGroup);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final Models foo = mItems.get(position);

        if (foo.exp_status.equalsIgnoreCase("1")) {

            holder.center_textview.setPaintFlags(holder.center_textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedtitle.setPaintFlags(holder.savedtitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedexperience.setPaintFlags(holder.savedexperience.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        holder.savedexperience.setText("Experience :" + foo.job_experience_from + " - " + foo.job_experience_to +" Yrs");
        holder.savedtitle.setText(foo.job_title);
      //  ImageLoader.loadImage(foo.first_pic,holder.ivinboximg);



        holder.center_textview.setText(foo.name);


        Glide.with(mcontext).load(foo.first_pic)
                .dontAnimate()
                .into(holder.ivinboximg);






        
    }








    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }







    public class ViewHolder extends RecyclerView.ViewHolder {

        private View rowItem;

        private ImageView ivinboximg;


        private TextView savedexperience;
        private TextView savedtitle;

        private TextView center_textview;

        private RelativeLayout root_layout;






        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            ivinboximg = itemView.findViewById(R.id.saveimageview);
            center_textview = itemView.findViewById(R.id.center_textview);
            savedexperience = itemView.findViewById(R.id.savedexperience);
            savedtitle = itemView.findViewById(R.id.savedtitle);
            root_layout = itemView.findViewById(R.id.root_layout);


        }
    }


    public interface clickHandler {

        void onView();



    }


}
