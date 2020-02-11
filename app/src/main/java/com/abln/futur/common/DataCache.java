package com.abln.futur.common;

import java.util.HashMap;

public class DataCache {
    private static final DataCache ourInstance = new DataCache();
    private static HashMap<String, Object> list;

    private DataCache() {
        list = new HashMap<>();
    }

    public static DataCache getInstance() {
        return ourInstance;
    }

    public static final void put(String key, Object object) {
        list.put(key, object);
    }

    public static final Object get(String key) {
        return list.get(key);
    }

}
