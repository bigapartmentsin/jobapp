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
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
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


       private String mSearchText="";


    ArrayList<SearchDatamodel> selectedelement;



    public  DatabaseHolder(Context mcontext, ArrayList<SearchDatamodel> items,EventHandler handler){

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






        String nameFullText = foo.occupation;
        int startPos = nameFullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
        int endPos = startPos + mSearchText.length();

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
       // holder.userDesignation.setText(foo.occupation);


        ImageLoader.loadImage(foo.avatar,holder.userimage);


        holder.openresume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.detailwrap.setVisibility(View.VISIBLE);






            }
        });


        holder.expreience.setText(foo.experience);



        if (foo.gender.equals("Male")){

            holder.detailwrap.setBackground(mcontext.getDrawable(R.drawable.male_resume));

        }else{

            holder.detailwrap.setBackground(mcontext.getDrawable(R.drawable.female_resume));

        }


        holder.detailwrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    eventHandler.showpdf(foo.resume,foo.filename);



            }
        });




        //handling the inforamtion of the datasets ;

        final AnimationSet animSet = new AnimationSet(true);
        animSet.setInterpolator(new DecelerateInterpolator());
        animSet.setFillAfter(true);
        animSet.setFillEnabled(true);
        final Animation slide_down = AnimationUtils.loadAnimation(mcontext,
                R.anim.slide_down);

        final Animation slide_up = AnimationUtils.loadAnimation(mcontext,
                R.anim.slide_up);

        holder.openresume.setOnClickListener(view -> {
            String _tag = holder.openresume.getTag().toString();
            if (_tag.equals("0")) {
                holder.openresume.setTag(1);

                holder.rlActionImage.setRotation(270);

                holder.detailwrap.setVisibility(View.VISIBLE);
            } else {
                holder.openresume.setTag(0);
                holder.rlActionImage.setRotation(90);
                holder.detailwrap.setVisibility(View.GONE);
            }

        });


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.openresume.setTag(0);
                holder.rlActionImage.setRotation(90);
                holder.detailwrap.setVisibility(View.GONE);
            }
        });



        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.checkBox.isChecked()){

                    System.out.println("Checked");
                    eventHandler.onItemCheck(foo);


                }else{
                    System.out.println("Unchecked");
                    eventHandler.onItemUncheck(foo);
                }

            }
        });













    }

    @Override
    public int getItemCount() {
        return (null != mItems ? mItems.size() : 0);
    }

     class DatabaseViewHolder extends RecyclerView.ViewHolder  {


        private CircleImageView userimage;//userIcon
        private TextView userNameText,userDesignation,distance,expreience;
        private ImageView rlActionImage;
        private LinearLayout layout;
        private CheckBox checkBox;
        private View openresume;
        private ConstraintLayout detailwrap;
        private View itemview;




        public DatabaseViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemview = itemView;
         userimage =    itemView.findViewById(R.id.userIcon);
          userNameText =   itemView.findViewById(R.id.userNameText);
         userDesignation =    itemView.findViewById(R.id.userDesignation);
         rlActionImage =    itemView.findViewById(R.id.rlActionImage);
         openresume =    itemView.findViewById(R.id.openresume);
         distance = itemView.findViewById(R.id.distance);
         detailwrap = itemView.findViewById(R.id.pat_details_wrap);
         expreience = itemView.findViewById(R.id.expreience);

         layout = itemView.findViewById(R.id.bottomBarBtn);

         checkBox = itemView.findViewById(R.id.radioBtnSelectUser);








        }

         public void setOnClickListener(View.OnClickListener onClickListener) {
             itemView.setOnClickListener(onClickListener);
         }

    }


    interface EventHandler{

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
