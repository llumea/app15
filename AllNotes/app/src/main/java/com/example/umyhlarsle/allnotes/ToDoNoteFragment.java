package com.example.umyhlarsle.allnotes;




import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoNoteFragment extends Fragment {

    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout1;
    ScrollView scrollView;
    LinearLayout linLayout;
    CheckBox checkBox;
    ImageButton addTaskButton;
    ImageButton removeTaskButton;
    TextView completedText;
    EditText editTaskText;
    int completedTaskCount;



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
        relativeLayout1 = (RelativeLayout)view.findViewById(R.id.relativelayout1);
        scrollView = (ScrollView)relativeLayout.findViewById(R.id.scroll_view);
        linLayout = (LinearLayout)scrollView.findViewById(R.id.custom_inflated_list_layout);

        completedText=(TextView)relativeLayout1.findViewById(R.id.completed_text);
        addTaskButton = (ImageButton)view.findViewById(R.id.add_task_button);
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///Lägger till task när man trycker på knappen
                MySingleton mySingleton = MySingleton.getInstance();

                ToDoTask tmpToDoTask = new ToDoTask(false, "");
                Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                tmpToDoNote.taskList.add(tmpToDoTask);
                tmpToDoNote.totalTasks++;
                ((SecondActivity)getActivity()).showToDoNoteFragment();
            }
        });


        ///Visar antal slutförda uppgifter/totalt antal uppgifter
        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;


    ///Start inflating checkbox, edittext and removebutton

        for (int i =0;i<tmpToDoNote.taskList.size();i++) {

            View inflatedView = inflater.inflate(R.layout.inflated_layout, linLayout, false);
            editTaskText = (EditText)inflatedView.findViewById(R.id.edit_task_text);
            editTaskText.setTag(tmpToDoNote.taskList.get(i));
            Log.i("TAG", "TAG för editTaskText: " + editTaskText.getTag());
            removeTaskButton = (ImageButton)inflatedView.findViewById(R.id.remove_task_button);
            if (tmpToDoNote.taskList.size()==1){removeTaskButton.setAlpha(0.5f);
            } else{removeTaskButton.setAlpha(1f);}
            removeTaskButton.setTag(tmpToDoNote.taskList.get(i));
            removeTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ///REMOVE task och visa count/total tasks
                    MySingleton mySingleton = MySingleton.getInstance();
                    Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                    ToDoNote tmpToDoNote = (ToDoNote) tmpNote;

                    for (int i=0;i<tmpToDoNote.taskList.size();i++) {
                        removeTaskButton = (ImageButton) v.findViewById(R.id.remove_task_button);
                        if (tmpToDoNote.taskList.size() > 1) {
                            if (removeTaskButton == v.findViewWithTag(tmpToDoNote.taskList.get(i))) {
                                tmpToDoNote.taskList.remove(tmpToDoNote.taskList.get(i));
                                ///Log.i("TAG", "TAR BORT: " + tmpToDoNote.taskList.get(i) + "index" + i);
                                tmpToDoNote.totalTasks = tmpToDoNote.taskList.size();

                                ((SecondActivity) getActivity()).showToDoNoteFragment();
                                Log.i("TAG", "Remove from list!!!");
                                showTaskList();
                                break;
                            }

                        }
                    }
                }
            });
            checkBox = (CheckBox)inflatedView.findViewById(R.id.check_box);
            checkBox.setTag(tmpToDoNote.taskList.get(i));

            ///Fyller i checkboxvärden från MySingleton

            for (int j = 0; j < tmpToDoNote.taskList.size(); j++) {
                if (checkBox.getTag() == tmpToDoNote.taskList.get(j)) {
                    editTaskText.setText(tmpToDoNote.taskList.get(i).taskString);
                    if (tmpToDoNote.taskList.get(j).taskDone == false) {

                        checkBox.setChecked(false);
                    } else if(tmpToDoNote.taskList.get(j).taskDone == true) {
                        checkBox.setChecked(true);
                        completedTaskCount++;
                    }
                }
            }
            tmpToDoNote.completedCount=completedTaskCount;

            checkBox.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                       ///Kollar bara den sista förekomsten, setTag eller id på checkbox.
                  MySingleton mySingleton = MySingleton.getInstance();
                  Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                  ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                  for (int i = 0; i < tmpToDoNote.taskList.size(); i++) {
                      checkBox = (CheckBox) v.findViewById(R.id.check_box);
                      if (checkBox == v.findViewWithTag(tmpToDoNote.taskList.get(i))) {
                          if (tmpToDoNote.taskList.get(i).taskDone == true) {
                              tmpToDoNote.taskList.get(i).taskDone = false;
                              Log.i("TAG", "UNChecked" + tmpToDoNote.taskList.get(i).taskDone);
                              ((SecondActivity) getActivity()).showToDoNoteFragment();
                          } else if (tmpToDoNote.taskList.get(i).taskDone == false) {
                              tmpToDoNote.taskList.get(i).taskDone = true;
                              Log.i("TAG", "Checked" + tmpToDoNote.taskList.get(i).taskDone);
                              ((SecondActivity) getActivity()).showToDoNoteFragment();
                          }


                      }
                  }

              }}


            );


                linLayout.addView(inflatedView);
            }
        completedText.setText("Completed: "+tmpToDoNote.completedCount+"/"+tmpToDoNote.totalTasks);

        ///End inflation


            return view;
    }
    public void onPause() {

        super.onPause();
        ///Sparar innehållet, titeln sparas i SecondActivity
        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;


        for (int i = 0; i < tmpToDoNote.taskList.size(); i++) {



        }
        showTaskList();
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
    //Testar listan
    public void showTaskList(){

        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
        for (int i = 0; i < tmpToDoNote.taskList.size(); i++) {
            Log.i("TAG", "I tasklist taskString: "+i+tmpToDoNote.taskList.get(i).taskString);
            Log.i("TAG", "I tasklist taskDone: "+i+tmpToDoNote.taskList.get(i).taskDone);
        }
    }


}
