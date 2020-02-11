package com.abln.futur.module.global.activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;

import java.util.ArrayList;


public class FilterValRecyclerAdapter extends RecyclerView.Adapter<FilterValRecyclerAdapter.ValueViewHolder> {
    private final FragmentActivity context;
    private final ArrayList<FilterDefaultMultipleListModel> filterModels;
    private final int resource;
    private final int type;
    OnItemClickListener mItemClickListener;

    public FilterValRecyclerAdapter(FragmentActivity context, int filter_list_item_layout, ArrayList<FilterDefaultMultipleListModel> filterModels, int type) {
        this.context = context;
        this.filterModels = filterModels;
        this.resource = filter_list_item_layout;
        this.type = type;
    }

    @Override
    public ValueViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(this.context)
                .inflate(resource, viewGroup, false);
        return new ValueViewHolder(v, this.type);
    }

    @Override
    public void onBindViewHolder(ValueViewHolder personViewHolder, int i) {
        personViewHolder.subCategoryName.setText(filterModels.get(i).getName());
        personViewHolder.cbSelected.setChecked(filterModels.get(i).isChecked());

    }

    @Override
    public int getItemCount() {
        return filterModels.size();
    }

    public void setItemSelected(int position) {
        if (position != -1) {
            filterModels.get(position).setChecked(!filterModels.get(position).isChecked());
            notifyDataSetChanged();
        }
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    public class ValueViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public int type;
        TextView subCategoryName;
        CheckBox cbSelected;
        View colorView;

        ValueViewHolder(View itemView, int type) {
            super(itemView);
            itemView.setOnClickListener(this);
            subCategoryName = itemView.findViewById(R.id.txt_item_list_title);
            cbSelected = itemView.findViewById(R.id.cbSelected);
            colorView = itemView.findViewById(R.id.colored_bar);
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
