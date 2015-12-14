package com.example.umyhlarsle.allnotes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by umyhlarsle on 2015-12-14.
 */
public class MySingleton {

    private static MySingleton sMySingleton;
    private ArrayList<Note> mNotes;

    public static MySingleton get(Context context){
        if (sMySingleton==null){
            sMySingleton = new MySingleton(context);
        }
        return sMySingleton;
    }
    private MySingleton (Context context){

        mNotes = new ArrayList<>();


    }


}
