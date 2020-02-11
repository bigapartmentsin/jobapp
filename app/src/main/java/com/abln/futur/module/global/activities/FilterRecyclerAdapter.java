package com.abln.futur.module.global.activities;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;

import java.util.ArrayList;


public class FilterRecyclerAdapter extends RecyclerView.Adapter<FilterRecyclerAdapter.PersonViewHolder> {

    private final int resource;
    private final FragmentActivity context;
    ArrayList<MainFilterModel> filterModels;
    OnItemClickListener mItemClickListener;


    public FilterRecyclerAdapter(FragmentActivity context, int filter_list_item_layout, ArrayList<MainFilterModel> filterModels) {
        this.context = context;
        this.filterModels = filterModels;
        this.resource = filter_list_item_layout;
    }


    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context)
                .inflate(resource, viewGroup, false);
        return new PersonViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {

        personViewHolder.parentView.setSelected(filterModels.get(i).isSelected());

        if (personViewHolder.personName.isSelected()) {
            if (filterModels.get(i).getTitle().equals("Sort")) {
                // personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.sizeblack, 0,0 );
                personViewHolder.personName.setText("Sort");
            } else if (filterModels.get(i).getTitle().equals("Company")) {
                //  personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.colorblack, 0, 0);
                personViewHolder.personName.setText("Company");
            } else if (filterModels.get(i).getTitle().equals("Other")) {
                //  personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.styleblack, 0, 0);
                personViewHolder.personName.setText("Other");
            }
        } else {
            if (filterModels.get(i).getTitle().equals("Sort")) {
                //   personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.sizepink, 0, 0);
                personViewHolder.personName.setText("Sort");
            } else if (filterModels.get(i).getTitle().equals("Company")) {
                //  personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.colorpink, 0, 0);
                personViewHolder.personName.setText("Company");
            } else if (filterModels.get(i).getTitle().equals("Other")) {
                //  personViewHolder.personName.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.stylepink, 0, 0);
                personViewHolder.personName.setText("Other");
            }
        }


    }

    @Override
    public int getItemCount() {
        return filterModels.size();
    }

    public void setItemSelected(int position) {
        for (MainFilterModel filterModel : filterModels) {
            filterModel.setIsSelected(false);


        }
        if (position != -1) {
            filterModels.get(position).setIsSelected(true);
            notifyDataSetChanged();
        }

    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position);


    }

    public class PersonViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public View parentView;
        TextView personName;

        PersonViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            personName = itemView.findViewById(R.id.txt_item_list_title);
            parentView = itemView;

        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }


}

