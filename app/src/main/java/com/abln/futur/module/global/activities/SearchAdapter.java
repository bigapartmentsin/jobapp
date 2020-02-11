package com.abln.futur.module.global.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.abln.futur.common.Name;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchAdapter extends ArrayAdapter<Name> {

    private Context context;
    private ArrayList<Name> nameArrayList;
    private ArrayList<Name> oldList;
    private String mSearchText;

    public SearchAdapter(Context context, ArrayList<Name> arrayList) {
        super(context, android.R.layout.simple_expandable_list_item_1, arrayList);
        this.context = context;
        this.nameArrayList = new ArrayList<>(arrayList);
        this.oldList = new ArrayList<>(arrayList);
    }


    public int getCount() {
        return nameArrayList.size();
    }

    public Name getItem(int position) {
        return nameArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView = (TextView) super.getView(i, view, viewGroup);
        Name item = getItem(i);
        String nameFullText = item.name;
        int startPos = nameFullText.toLowerCase(Locale.US).indexOf(mSearchText.toLowerCase(Locale.US));
        int endPos = startPos + mSearchText.length();
        if (startPos != -1) {
            Spannable spannable = new SpannableString(nameFullText);
            ColorStateList _color = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.parseColor("#282f3f")});//Color.parseColor("#e6282f3f")
            TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, _color, null);
            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(spannable);

        } else {
            textView.setText(nameFullText);
        }
        return textView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((Name) resultValue).name;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                mSearchText = constraint.toString();
                FilterResults filterResults = new FilterResults();
                List<Name> departmentsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (Name department : oldList) {
                        if (department.name.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            departmentsSuggestion.add(department);
                        }
                    }
                    filterResults.values = departmentsSuggestion;
                    filterResults.count = departmentsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                nameArrayList.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof Name) {
                            nameArrayList.add((Name) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                    nameArrayList.addAll(oldList);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}
