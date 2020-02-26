package com.abln.futur.activites;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

class DatabaseHolder extends RecyclerView.Adapter<DatabaseHolder.DatabaseViewHolder> implements Filterable {


    // ArrayList<SearchDatamodel>
    ArrayList<SearchDatamodel> mItems;
    private final LayoutInflater mInflater;
    private Context mcontext;
    EventHandler eventHandler;

    public ArrayList<SearchDatamodel> originalList;
    private String mSearchText = "";
    ArrayList<SearchDatamodel> selectedelement;


    public DatabaseHolder(Context mcontext, ArrayList<SearchDatamodel> items, EventHandler handler) {
        this.mcontext = mcontext;
        this.mItems = items;
        this.originalList = items;
        mInflater = LayoutInflater.from(mcontext);
        this.eventHandler = handler;

    }


    @NonNull
    @Override
    public DatabaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View mainGroup = mInflater.inflate(R.layout.list_user_layout, parent, false);
        return new DatabaseViewHolder(mainGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull DatabaseViewHolder holder, int position) {
        final SearchDatamodel foo = mItems.get(position);

        int endPos;
        int startPos;
        String nameFullText = foo.occupation;
        startPos = nameFullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
        endPos = startPos + mSearchText.length();


        if (startPos != -1) {
            Spannable spannable = new SpannableString(nameFullText);
            ColorStateList _color = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#282f3f")});//Color.parseColor("#e6282f3f")
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan("@font/mulibold", Typeface.NORMAL, -1, _color, null);
            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.userDesignation.setText(spannable);
        } else {
            holder.userDesignation.setText(nameFullText);
        }


        holder.userNameText.setText(foo.first_name);
        holder.distance.setText(foo.distance);
        ImageLoader.loadImage(foo.avatar, holder.userimage);


        System.out.println("------------------------bug enterd------"+holder.openresume.getTag().toString()+"bug name"+foo.first_name);



        //gettingthe initial tag of the system

//
//        holder.openresume.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                holder.detailwrap.setVisibility(View.VISIBLE);
//
//            }
//        });


        holder.expreience.setText(foo.experience);
        if (foo.gender.equals("Male") || foo.gender.equals("male")) {

            holder.detailwrap.setBackground(mcontext.getDrawable(R.drawable.male_resume));

        } else {
            holder.detailwrap.setBackground(mcontext.getDrawable(R.drawable.female_resume));

        }


        holder.detailwrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                eventHandler.showpdf(foo.resume, foo.filename);


            }
        });





        //handling the inforamtion of the datasets ;

//
//        holder.openresume.setOnClickListener(view -> {
//
//
//            String _tag = holder.openresume.getTag().toString();
//
//
//
//            if (_tag.equals("0")) {
//
//                holder.openresume.setTag(1);
//
//                holder.rlActionImage.setRotation(270);
//                holder.detailwrap.setVisibility(View.VISIBLE);
//
//            } else {
//                holder.openresume.setTag(0);
//                holder.rlActionImage.setRotation(90);
//                holder.detailwrap.setVisibility(View.GONE);
//            }
//
//        });


//        String closeTag = holder.openresume.getTag().toString();
//        if (closeTag.equals("0")){
//
//            holder.detailwrap.setVisibility(View.GONE);
//
//        }else{
//
//            holder.detailwrap.setVisibility(View.VISIBLE);
//        }


//        holder.layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.openresume.setTag(0);
//                holder.rlActionImage.setRotation(90);
//                holder.detailwrap.setVisibility(View.GONE);
//            }
//        });


        // adding the main Stream of the infromation to work on it .

        // loging


        if (foo.isSelected()) {
            holder.checkBox.setBackground(mcontext.getDrawable(R.drawable.ic_oval_selected));

        } else {
            holder.checkBox.setBackground(mcontext.getDrawable(R.drawable.ic_oval_unselected));
        }


        holder.checkBox.setChecked(foo.isSelected());

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foo.setSelected(!foo.isSelected());


                if (foo.isSelected()) {

                    eventHandler.onItemCheck(foo);



                } else {


                    System.out.println("Sorry unselected data ");
                    eventHandler.onItemUncheck(foo);
                }


                notifyDataSetChanged();


            }
        });



        //handling the view clicked out the information

        if (foo.isViewClicked()){
            holder.detailwrap.setVisibility(View.VISIBLE);
            holder.rlActionImage.setRotation(270);

        }else{
            holder.detailwrap.setVisibility(View.GONE);
            holder.rlActionImage.setRotation(90);
        }


        holder.openresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                foo.setIsviewedclicked(!foo.isViewClicked());


                if (foo.isViewClicked()){

                    holder.rlActionImage.setRotation(270);
                    holder.detailwrap.setVisibility(View.VISIBLE);


                    // user data is clicked
                }else{

                    // unchecked
                    holder.rlActionImage.setRotation(90);
                    holder.detailwrap.setVisibility(View.GONE);

                }

                notifyDataSetChanged();


            }
        });



        /*
        *
        *       holder.openresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.rlActionImage.setRotation(270);
                holder.detailwrap.setVisibility(View.VISIBLE);
                holder.openresume.setTag("1");

            }
        });
        * */





    }

    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }

    class DatabaseViewHolder extends RecyclerView.ViewHolder {


        private CircleImageView userimage;//userIcon
        private TextView userNameText, userDesignation, distance, expreience;
        private ImageView rlActionImage;
        private LinearLayout layout;
        private CheckedTextView checkBox;
        private LinearLayout openresume;
        private ConstraintLayout detailwrap;
        private View itemview;


        public DatabaseViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemview = itemView;
            userimage = itemView.findViewById(R.id.userIcon);
            userNameText = itemView.findViewById(R.id.userNameText);
            userDesignation = itemView.findViewById(R.id.userDesignation);
            rlActionImage = itemView.findViewById(R.id.rlActionImage);
           openresume = itemView.findViewById(R.id.openresume);
            distance = itemView.findViewById(R.id.distance);
            detailwrap = itemView.findViewById(R.id.details_wrap);
            expreience = itemView.findViewById(R.id.expreience);

            layout = itemView.findViewById(R.id.bottomBarBtn);

            checkBox = itemView.findViewById(R.id.radioBtnSelectUser);


        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }

    }


    interface EventHandler {

        void showpdf(String data, String name);

        void onItemCheck(SearchDatamodel item);

        void onItemUncheck(SearchDatamodel item);


    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                mSearchText = charSequence.toString();
                List<SearchDatamodel> list;
                if (mSearchText.isEmpty()) {
                    list = originalList;
                } else {
                    List<SearchDatamodel> filteredList = new ArrayList<>();
                    for (SearchDatamodel row : originalList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.occupation.toLowerCase().contains(mSearchText.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mItems = (ArrayList<SearchDatamodel>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };

    }


}
