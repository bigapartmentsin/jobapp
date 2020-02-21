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
import com.abln.futur.activites.Analytics;
import com.abln.futur.activites.ListApplicataints;
import com.abln.futur.common.savedlist.Savedlist;

import java.util.ArrayList;

public class InboxItem extends RecyclerView.Adapter<InboxItem.ViewHolder> {


    private final LayoutInflater mInflater;

    private ArrayList<ModChat> mItems;
    private Context mcontext;
    clickHandler handler ;


    public InboxItem(Context mcontext, ArrayList<ModChat> items,clickHandler handler) {

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



        if (mItems.get(position).equals(0)){




        }






        final ModChat foo = mItems.get(position);

        if (foo.exp_status.equalsIgnoreCase("1")) {

            holder.center_textview.setPaintFlags(holder.center_textview.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedtitle.setPaintFlags(holder.savedtitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.savedexperience.setPaintFlags(holder.savedexperience.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }

        holder.savedexperience.setText("Experience :" + foo.job_experience_from + " - " + foo.job_experience_to +" Yrs");
        holder.savedtitle.setText(foo.job_title);
        ImageLoader.loadImage(foo.first_pic,holder.ivinboximg);
        holder.center_textview.setText(foo.name);

        //image view click;

        holder.ivinboximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


        holder.root_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                handler.onapplyClick();
                Intent listapp = new Intent(mcontext, ListApplicataints.class);
                listapp.putExtra("data",foo.pid);
                listapp.putExtra("name",foo.name.replace("Post",""));
                mcontext.startActivity(listapp);


            }
        });







    }


    private static final int STATIC_CARD = 0;
    private static final int DYNAMIC_CARD = 1;

    @Override
    public int getItemViewType(int position) {
        if(position == 0) {
            return STATIC_CARD;
        } else {
            return DYNAMIC_CARD;
        }
    }

    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }

    public interface clickHandler {

        void onapplyClick();



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
