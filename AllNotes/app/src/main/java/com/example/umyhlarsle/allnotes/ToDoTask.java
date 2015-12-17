package com.example.umyhlarsle.allnotes;

import java.util.ArrayList;

/**
 * Created by umyhlarsle on 2015-12-15.
 */
public class ToDoTask {

    boolean taskDone;
    String taskString;
    Tag editTextTag = new Tag();

    public ToDoTask(){}

    public ToDoTask(boolean taskDone, String taskString){

        this.taskDone = taskDone;
        this.taskString = taskString;
    }


}
