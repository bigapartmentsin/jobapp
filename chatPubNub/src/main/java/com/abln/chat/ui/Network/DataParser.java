package com.abln.chat.ui.Network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class DataParser {
    private static final String TAG = "DataParser";



    /**
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     * @throws JsonSyntaxException
     */


    public static <T> T parseJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        try {
            return new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create()
                    .fromJson(json, classOfT);

        } catch (Exception e) {
            Log.i(TAG, "JSON Exception : " + e.getLocalizedMessage());
            return null;
        }

    }



    public static String toJsonString(Object object) {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(object);
    }
}
