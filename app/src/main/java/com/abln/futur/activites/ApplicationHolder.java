package com.abln.futur.activites;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AppliedInfo;
import com.abln.futur.common.savedlist.SavedItem;
import com.abln.futur.common.savedlist.Savedlist;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApplicationHolder  extends RecyclerView.Adapter<ApplicationHolder.ViewHolder> {

    private final LayoutInflater mInflater;
    Context mcontext;
    viewHandler handler;
    private ArrayList<AppliedInfo> mItems;

    private AlertDialog alert;

  public  ApplicationHolder(Context mcontext, ArrayList<AppliedInfo> info ,viewHandler handler){
        this.mcontext = mcontext;
        mInflater = LayoutInflater.from(mcontext);
        this.handler = handler;
        this.mItems = info;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        View mainGroup = mInflater.inflate(R.layout.application_user, parent, false);
        return new ViewHolder(mainGroup);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        final AppliedInfo foo = mItems.get(position);

        holder.user_textview.setText(foo.can_details.fname);
        holder.timestamp.setText(foo.time);


//        holder.user_accept_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//
//            }
//        });



        if (foo.acc.equals("1")){

            holder.frameLayout.setBackground(mcontext.getDrawable(R.drawable.accepted_bg));
            holder.revert.setVisibility(View.GONE);
            holder.acceptjob.setVisibility(View.GONE);
            holder.declinedjob.setVisibility(View.GONE);

            holder.chat_move.setVisibility(View.VISIBLE);
        }else if (foo.rej.equals("1")){


            holder.frameLayout.setBackground(mcontext.getDrawable(R.drawable.declined_bg));
            holder.revert.setVisibility(View.VISIBLE);
            holder.declinedjob.setVisibility(View.GONE);
            holder.acceptjob.setText("Declined");


        }


        holder.viewpdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(mcontext,"Viewing Resume");

                handler.onReviewd(foo);

            }
        });


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });



        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

      //  ImageLoader.loadImage(foo.can_details.avatar,holder.user_name);


        holder.revert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setMessage("Revert Status ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        holder.revert.setVisibility(View.GONE);
                        holder.frameLayout.setBackground(mcontext.getDrawable(R.drawable.pending));
                        holder.declinedjob.setVisibility(View.VISIBLE);
                        holder.acceptjob.setVisibility(View.VISIBLE);
                        holder.declinedjob.setText("ACCEPT");
                        holder.acceptjob.setText("DECLINE");
                        handler.onRevert(foo);
                        dialog.dismiss();

                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


                alert = builder.create();
                alert.show();



            }
        });







        holder.acceptjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(mcontext,"Decline");
                holder.frameLayout.setBackground(mcontext.getDrawable(R.drawable.declined_bg));
                holder.revert.setVisibility(View.VISIBLE);
                holder.declinedjob.setVisibility(View.GONE);
                holder.acceptjob.setText("Declined");
                handler.onDecline(foo);

            }
        });


        holder.declinedjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                builder.setMessage("Are you sure you want to Accept ?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        UIUtility.showToastMsg_withAlertInfoShort(mcontext,"Accept");
                        holder.frameLayout.setBackground(mcontext.getDrawable(R.drawable.accepted_bg));
                        holder.revert.setVisibility(View.GONE);
                        holder.acceptjob.setVisibility(View.GONE);
                        holder.declinedjob.setVisibility(View.GONE);

                        holder.chat_move.setVisibility(View.VISIBLE);
                        handler.onAccept(foo);

                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });


                alert = builder.create();
                alert.show();








            }
        });



        holder.chat_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO chat function
                    handler.onChat(foo);



                //ChatHelper.openChatUserListActivity(mContext, mContextName, mContextId);


            }
        });

    }

    @Override
    public int getItemCount()

    {


        return (null != mItems ? mItems.size() : 0);


    }





    public interface viewHandler {

        void onAccept(AppliedInfo info);

        void onDecline(AppliedInfo info);

        void onRevert(AppliedInfo info);


        void onPdfview(AppliedInfo info);


        void onChat(AppliedInfo info);

        void onReviewd(AppliedInfo info);


        void onVstat(AppliedInfo info);


        void mApp(AppliedInfo info);

    }



    public class ViewHolder extends RecyclerView.ViewHolder {



        CircleImageView user_name;
        TextView user_textview;
        TextView timestamp;
        ImageView user_accept_button;
        Button accept;
        Button decline;
        View viewpdf ;
        TextView   acceptjob ,declinedjob;
        FrameLayout frameLayout ;
        TextView revert;

        private TextView chat_move;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            user_name = itemView.findViewById(R.id.user_name);
            user_textview = itemView.findViewById(R.id.user_textview);
            timestamp = itemView.findViewById(R.id.timestamp);
            user_accept_button = itemView.findViewById(R.id.user_accept_button);
            accept = itemView.findViewById(R.id.accept);
            decline = itemView.findViewById(R.id.decline);

            viewpdf = itemView.findViewById(R.id.viewpdf);
            acceptjob  = itemView.findViewById(R.id.acceptbutton);
            declinedjob = itemView.findViewById(R.id.deletebutton);
            frameLayout = itemView.findViewById(R.id.framelayout);
            revert = itemView.findViewById(R.id.revert);
            chat_move = itemView.findViewById(R.id.chat_move);


        }
    }




}
