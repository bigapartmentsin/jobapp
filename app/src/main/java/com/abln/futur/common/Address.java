package com.abln.futur.common;

import com.abln.futur.common.models.PostDataModel;
import com.abln.futur.common.newview.FinalDataSets;
import com.abln.futur.common.savedlist.Savedlist;
import com.abln.futur.common.savedlist.UserList;
import com.abln.futur.common.searchjob.SearchResult;

import java.io.Serializable;
import java.util.ArrayList;

public class Address implements Serializable {

    public ArrayList<Addinfo> address;

    public ArrayList<SearchResult> search_results;

    public ArrayList<FinalDataSets> results;

    public ArrayList<Savedlist> result;


    public ArrayList<UserList>   user_list;

    public ArrayList<ModChat> post_list;



}
