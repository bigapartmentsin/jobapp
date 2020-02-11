package com.abln.futur.common.savedlist;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class SingleThread implements Serializable, Cloneable {


    private static volatile SingleThread instance;

//
//    private SingleThread() {
//        if (instance != null) {
//            throw new RuntimeException(){
//
//                 // System.out.println("Single thread has be init.");
//
//
//        }
//    }


    static SingleThread getInstance() {
        if (instance == null) {
            //if there is no instance available... create new one
            synchronized (SingleThread.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (instance == null) instance = new SingleThread();
            }
        }
        return instance;
    }


    protected Object readResolve() {
        return getInstance();
    }


    @NonNull
    @Override
    protected Object clone() {
        return instance;
    }


}
