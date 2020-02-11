package com.abln.futur.common;

import java.util.Collection;

public class CollectionUtils {

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.size() == 0;
    }
}
