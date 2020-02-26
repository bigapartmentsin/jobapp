package com.abln.futur.common.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.UIUtility;

import java.util.ArrayList;

public class PostItems extends RecyclerView.Adapter<PostItems.ViewHolder> {


    private final LayoutInflater mInflater;

    private ArrayList<PostDataModel> mItems;
    private Context mcontext;
    clickHandler handler ;



    public PostItems(Context mcontext, ArrayList<PostDataModel> items, clickHandler handler) {

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.handler = handler;
        this.mItems = items;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View mainGroup = mInflater.inflate(R.layout.popup_job_card, parent, false);
        return new ViewHolder(mainGroup);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final PostDataModel foo = mItems.get(position);

        ImageLoader.loadImage(foo.first_pic,holder.ivinboximg);
        holder.ivinboximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                UIUtility.showToastMsg_withSuccessShort(mcontext,"Click PostName"+foo.job_title);

                //handler.onapplyClick();
                handler.onapplyClick(foo);






                //

            }
        });


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



        }
    }




    public interface clickHandler {

        void onapplyClick();
        void onapplyClick(PostDataModel data);



    }

}
