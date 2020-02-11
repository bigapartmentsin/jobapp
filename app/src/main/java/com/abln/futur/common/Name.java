package com.abln.futur.common;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Name implements Serializable {
    public String name;


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
