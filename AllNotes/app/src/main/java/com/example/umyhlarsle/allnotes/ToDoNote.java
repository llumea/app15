package com.example.umyhlarsle.allnotes;

import java.util.HashMap;

/**
 * Created by umyhlarsle on 2015-12-09.
 */
public class ToDoNote extends TextNote {

    ///String content kan vara första punkten i listan?
    HashMap<String, Boolean> taskMap = new HashMap<>();

    public ToDoNote(String title, String content, long date, HashMap<String,Boolean> taskMap){
        super.title = title;
        super.content = content;
        super.date = date;
        this.taskMap = taskMap;
    }

}
