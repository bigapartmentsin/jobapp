package com.abln.futur.common.newview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.abln.futur.R;
import com.abln.futur.common.Name;
import com.abln.futur.common.Title;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SearchFilter extends ArrayAdapter<Name> {



    Context mContext;
    int layoutResourceId;
    private ArrayList<Name> data = null;

    public SearchFilter(Context mContext, int layoutResourceId, ArrayList<Name> data) {
        super(mContext, layoutResourceId, data);
        this.data = data;
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
    }

    @Override
    public View getView(int position, View convertView, @NotNull ViewGroup parent) {
        if (convertView == null) {
            // inflate the layout
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }
        Name objectItem = data.get(position);
        TextView textViewItem = convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(objectItem.name);
        return convertView;

    }








//  private fun setData(symptomList: ArrayList<SymptomData>) {
//        searchTxt.threshold = 1
//        val diagnosisSearchAdapter = DiagnosisSearchAdapter(activity, R.layout.listview_row, symptomList)
//      val arrayAdapter = ArrayAdapter(activity!!.applicationContext, android.R.layout.simple_list_item_1, symptomList)
//        searchTxt.setAdapter(diagnosisSearchAdapter)
//        searchTxt.setOnItemClickListener { p0, p1, p2, p3 ->
//            val symptomData = diagnosisSearchAdapter.getItem(p2)
//            makeSUggestionList(symptomData!!)
//        }
//    }








}
