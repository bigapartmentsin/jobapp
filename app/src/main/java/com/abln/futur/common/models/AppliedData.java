package com.abln.futur.common.models;

import com.abln.futur.activites.DatabaseUserSearch;
import com.abln.futur.activites.SearchDatamodel;

import java.io.Serializable;
import java.util.ArrayList;

public class AppliedData implements Serializable {


  public   String tstat ;

  public String avatar_url;

  public ArrayList<AppliedInfo> user_list;


  public ArrayList<SearchDatamodel> results;
}
