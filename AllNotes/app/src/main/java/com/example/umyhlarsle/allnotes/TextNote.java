package com.example.umyhlarsle.allnotes;

/**
 * Created by umyhlarsle on 2015-12-09.
 */
public class TextNote extends Note {

    String title;
    String content;
    long date;

    public TextNote(){}

    public TextNote(String title, String content, long date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

}
