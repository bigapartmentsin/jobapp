
package com.abln.futur.models;


import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Result {

    @SerializedName("name")
    private String mName;

    public String getName() {
        return mName;
    }

    public static class Builder {

        private String mName;

        public Result.Builder withName(String name) {
            mName = name;
            return this;
        }

        public Result build() {
            Result result = new Result();
            result.mName = mName;
            return result;
        }

    }

}
