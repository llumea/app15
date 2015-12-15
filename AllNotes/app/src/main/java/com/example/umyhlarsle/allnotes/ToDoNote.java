package com.example.umyhlarsle.allnotes;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by umyhlarsle on 2015-12-09.
 */
public class ToDoNote extends TextNote {

    ///String content kan vara första punkten i listan?
    ArrayList<ToDoTask> taskList = new ArrayList<>();

    public ToDoNote(){}

    public ToDoNote(String title, String content, long date, ArrayList<ToDoTask> taskList){
        super.title = title;
        super.content = content;
        super.date = date;
        this.taskList = taskList;
    }

}
