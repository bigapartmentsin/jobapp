
package com.abln.futur.models;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Data {

    @SerializedName("result")
    private List<Result> mResult;

    public List<Result> getResult() {
        return mResult;
    }

    public static class Builder {

        private List<Result> mResult;

        public Data.Builder withResult(List<Result> result) {
            mResult = result;
            return this;
        }

        public Data build() {
            Data data = new Data();
            data.mResult = mResult;
            return data;
        }

    }

}
