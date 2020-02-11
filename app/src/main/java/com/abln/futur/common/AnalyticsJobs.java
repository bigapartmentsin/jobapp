package com.abln.futur.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.activites.ListApplicataints;

import java.util.ArrayList;

public class AnalyticsJobs extends RecyclerView.Adapter<AnalyticsJobs.ViewHolder> {


    private final LayoutInflater mInflater;

    private ArrayList<ModChat> mItems;
    private Context mcontext;
    private Handler handler;


    public AnalyticsJobs(Context mcontext, ArrayList<ModChat> items,Handler handler) {
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

        final ModChat foo = mItems.get(position);
        holder.savedexperience.setText("Experience :" + foo.job_experience_from + " - " + foo.job_experience_to +" Yrs");
        holder.savedtitle.setText(foo.job_title);
        ImageLoader.loadImage(foo.first_pic,holder.ivinboximg);
        holder.center_textview.setText(foo.name);


        if (foo.exp_status.equalsIgnoreCase("1")) {

            holder.center_textview.setPaintFlags(holder.center_textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedtitle.setPaintFlags(holder.savedtitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedexperience.setPaintFlags(holder.savedexperience.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }



        //image view click;

        holder.ivinboximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // Intent listapp = new Intent(mcontext, ListApplicataints.class);
               // listapp.putExtra("data",foo.pid);
               // mcontext.startActivity(listapp);


                handler.onTapclicked(foo);

            }
        });







    }

    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }

    public interface Handler {

        void onTapclicked(ModChat savedlist);


        void onFullList(ModChat savedlist);


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

}
