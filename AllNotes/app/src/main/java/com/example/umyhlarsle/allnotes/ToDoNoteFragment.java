package com.example.umyhlarsle.allnotes;




import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoNoteFragment extends Fragment {

    RelativeLayout relativeLayout;
    ScrollView scrollView;
    LinearLayout linLayout;
    CheckBox checkBox;
    int checkBoxId =0;



    public ToDoNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_note, container, false);
        relativeLayout = (RelativeLayout)view.findViewById(R.id.relativelayout2);
        scrollView = (ScrollView)relativeLayout.findViewById(R.id.scroll_view);
        linLayout = (LinearLayout)scrollView.findViewById(R.id.custom_inflated_list_layout);



    ///for-loop för varje item i todonote
        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;

        for (int i =0;i<tmpToDoNote.taskList.size();i++) {

            View inflatedView = inflater.inflate(R.layout.inflated_layout, linLayout, false);
            checkBox = (CheckBox)inflatedView.findViewById(R.id.check_box);
            checkBox.setTag(tmpToDoNote.taskList.get(i));
        if (checkBox.getTag()==(tmpToDoNote.taskList.get(i))) {
            if (tmpToDoNote.taskList.get(i).taskDone == false) {
                checkBox.setChecked(false);
            } else {checkBox.setChecked(true);}
        }

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///Kollar bara den sista förekomsten, setTag eller id på checkbox.

                       if (checkBox.isChecked()) {
                           Log.i("TAG", "Checked");
                       } else {
                           Log.i("TAG", "Not Checked");
                       }


                }
            });


            linLayout.addView(inflatedView);
        }


        return view;
    }
    public void onPause() {

        super.onPause();
        ///Sparar innehållet, titeln sparas i SecondActivity
        MySingleton mySingleton = MySingleton.getInstance();
        ///String currentContent = editContent.getText().toString();
        ///((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).content= currentContent;

        ///Sparar datum här i stället för SecondActivity, datum för todonote kanske ändras till deadline
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long tmpTime = date.getTime();
        ((ToDoNote)mySingleton.myNoteList.get(mySingleton.listPosition)).date= tmpTime;


    }
    @Override
    public void onResume() {
        super.onResume();

        ///String tmpTask = tmpToDoNote...



    }


}
