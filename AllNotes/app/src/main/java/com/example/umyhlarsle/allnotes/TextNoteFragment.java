package com.example.umyhlarsle.allnotes;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TextNoteFragment extends Fragment {

    EditText editContent;

    public TextNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_note, container, false);
        editContent = (EditText)view.findViewById(R.id.edit_content);

        return view;
    }
    @Override
    public void onPause() {

        super.onPause();
        ///Sparar innehållet, titeln sparas i SecondActivity
        MySingleton mySingleton = MySingleton.getInstance();
        String currentContent = editContent.getText().toString();
        ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).content= currentContent;

        ///Sparar datum här i stället för SecondActivity, datum för todonote kanske ändras till deadline
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long tmpTime = date.getTime();
        ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).date= tmpTime;

        ///Testar sparade värdet
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
       TextNote tmpTextNote = (TextNote) tmpNote;
        String tmpContent = tmpTextNote.content;
        Log.i("TAG", "Ändrat värde?"+tmpContent+"listPosition: "+mySingleton.listPosition);

    }
    @Override
    public void onResume() {
        super.onResume();
        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        TextNote tmpTextNote = (TextNote) tmpNote;
        String tmpContent = tmpTextNote.content;
        editContent.setText(tmpContent);
        Log.i("TAG", "Resume värde?"+tmpContent+"listPosition: "+mySingleton.listPosition);


    }


}
