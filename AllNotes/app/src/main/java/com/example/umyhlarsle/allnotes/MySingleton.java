package com.example.umyhlarsle.allnotes;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by umyhlarsle on 2015-12-14.
 */
public class MySingleton {

    int listPosition;
    ArrayList<Note> myNoteList = new ArrayList<>();

    //create an object of SingleObject
    private static MySingleton instance = new MySingleton();

    //make the constructor private so that this class cannot be
    //instantiated
    private MySingleton(){

    }

    //Get the only object available
    public static MySingleton getInstance(){
        return instance;
    }




}
