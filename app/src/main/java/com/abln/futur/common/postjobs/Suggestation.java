package com.abln.futur.common.postjobs;

//public class Suggestation  extends ArrayAdapter<String> implements Filterable {

//    private List<Result> mlistData ;
//
//    public Suggestation(@NonNull Context context, int resource, @NonNull String[] objects) {
//        super(context, resource, objects);
//
//    }
//
//
//    public void setData(List<String> list) {
//        mlistData.clear();
//        mlistData.addAll(list);
//    }
//    @Override
//    public int getCount() {
//        return mlistData.size();
//    }
//
//
//
//    @Nullable
//    @Override
//    public String getItem(int position) {
//        return mlistData.get(position);
//    }
//    /**
//     * Used to Return the full object directly from adapter.
//     *
//     * @param position
//     * @return
//     */
//    public String getObject(int position) {
//        return mlistData.get(position);
//    }
//
//
//
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        Filter dataFilter = new Filter() {
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults filterResults = new FilterResults();
//                if (constraint != null) {
//                    filterResults.values = mlistData;
//                    filterResults.count = mlistData.size();
//                }
//                return filterResults;
//            }
//            @Override
//            protected void publishResults(CharSequence constraint, FilterResults results) {
//                if (results != null && (results.count > 0)) {
//                    notifyDataSetChanged();
//                } else {
//                    notifyDataSetInvalidated();
//                }
//            }
//        };
//        return dataFilter;
//    }


//}


// 8867732201