package com.abln.futur.common.searchjob;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SearchJob implements Serializable {

    private Integer statuscode;
    private String statusMessage;
    private Data data;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(Integer statuscode) {
        this.statuscode = statuscode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
