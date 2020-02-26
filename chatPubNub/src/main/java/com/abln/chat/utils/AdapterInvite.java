package com.abln.chat.utils;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class AdapterInvite  extends RecyclerView.Adapter<AdapterInvite.ViewHolder> {




    private Context mcontext;
    private final LayoutInflater mInflater;
    private ArrayList<Admod> mItems;
    private clickHandler clickHandler;






  public  AdapterInvite(Context mcontext,ArrayList<Admod> items,clickHandler clickHandler){

        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.clickHandler = clickHandler;

        this.mItems = items;

    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View mainGroup = mInflater.inflate(R.layout.popup_job_card_chat, parent, false);
        return new ViewHolder(mainGroup);


    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {



        final Admod foo = mItems.get(position);
        Glide.with(mcontext).load(foo.thumbnail).into(holder.ivinboximg);

        holder.ivinboximg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (foo.ispdf){



                    clickHandler.datarefresh();
                    Toast.makeText(mcontext,"Opening Pdf ",Toast.LENGTH_LONG).show();



                    Intent i = new Intent(mcontext, RenderPdfView.class);
                    i.putExtra("pdf",foo.pdf);
                    i.putExtra("name",foo.name);
                    i.putExtra("apply",foo.jexpstatus);
                    i.putExtra("star",foo.isstarred);
                    i.putExtra("uid",foo.rid);
                    i.putExtra("key",foo.pid);
                    i.putExtra("cid",foo.cid);
                    mcontext.startActivity(i);

                }else {

                    clickHandler.datarefresh();

                    Toast.makeText(mcontext,"Opening Stories  ",Toast.LENGTH_LONG).show();






                    // end of the week data sets . //  are added // to work for the stories //


                        String url = "";

                    ArrayList<String> keyvalue = new ArrayList<String>();

                    if (!foo.jpost_one.equalsIgnoreCase("0")){

                        keyvalue.add(foo.jpost_one);

                    }

                    if(!foo.jpost_two.equalsIgnoreCase("0")){

                        keyvalue.add(foo.jpost_two);

                    }

                    if (!foo.jpost_three.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_three);
                    }

                    if (!foo.jpost_four.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_four);
                    }

                    if (!foo.jpost_five.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_five);
                    }

                    if (!foo.jpost_six.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_six);
                    }

                    if (!foo.jpost_seven.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_seven);
                    }

                    if (!foo.jpost_eight.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_eight);
                    }

                    if (!foo.jpost_nine.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_nine);
                    }

                    if (!foo.jpost_ten.equalsIgnoreCase("0")){
                        keyvalue.add(foo.jpost_ten);
                    }




                    Intent intent = new Intent(mcontext, StoriesActivity.class);
                    intent.putStringArrayListExtra("DATA", keyvalue);

                    intent.putExtra("apply",foo.jexpstatus);
                    intent.putExtra("star",foo.isstarred);
                    intent.putExtra("uid",foo.rid);
                    intent.putExtra("key",foo.pid);
                    intent.putExtra("cid",foo.cid);
                    mcontext.startActivity(intent);



                    // work india is working alot to get the data handling  //

                }



            }
        });



    }





    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }




    //  public interface clickHandler {
    public  interface clickHandler{

       void datarefresh();

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



}
