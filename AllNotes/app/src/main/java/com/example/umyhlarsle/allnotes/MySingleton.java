package com.example.umyhlarsle.allnotes;

import android.content.Context;
import java.util.ArrayList;

/**
 * Created by umyhlarsle on 2015-12-14.
 */
public class MySingleton {

    int listPosition;
    int sortOrder;

    final static int SORT_BY_DATE=0;
    final static int SORT_BY_TYPE=1;
    ArrayList<Note> myNoteList = new ArrayList<>();


    //create an object of SingleObject
    private static MySingleton instance = new MySingleton();

    //make the constructor private so that this class cannot be
    //instantiated
    private MySingleton(){

    }

    //Get the only object available
    //Tanken var att ladda/spara skulle ske här. Därför finns Context context kvar här. Vänligen ignorera det.
    public static MySingleton getInstance(Context context){

        return instance;
    }


}
