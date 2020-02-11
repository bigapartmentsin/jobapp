package com.abln.futur.common.searchjob;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Data implements Serializable {

    private String pagelimit;
    private String pagecount;
    private ArrayList<SearchResult> searchResults = null;

    private ArrayList<Result> results = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getPagelimit() {
        return pagelimit;
    }

    public void setPagelimit(String pagelimit) {
        this.pagelimit = pagelimit;
    }

    public String getPagecount() {
        return pagecount;
    }

    public void setPagecount(String pagecount) {
        this.pagecount = pagecount;
    }

    public ArrayList<SearchResult> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(ArrayList<SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
