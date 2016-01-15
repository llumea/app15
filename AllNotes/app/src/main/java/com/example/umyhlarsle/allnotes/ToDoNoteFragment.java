package com.example.umyhlarsle.allnotes;




import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class ToDoNoteFragment extends Fragment implements TextWatcher {

    RelativeLayout relativeLayout1;
    ImageButton addTaskButton;
    TextView completedText;
    ListView todoListView;
    MyToDoAdapter myToDoAdapter;
    RelativeLayout inflatedRelativeLayout;

    public ToDoNoteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_to_do_note, container, false);

        relativeLayout1 = (RelativeLayout)view.findViewById(R.id.relativelayout1);
        inflatedRelativeLayout = (RelativeLayout)view.findViewById(R.id.inflated_relative_layout);
        completedText=(TextView)relativeLayout1.findViewById(R.id.completed_text);
        addTaskButton = (ImageButton)view.findViewById(R.id.add_task_button);
        MySingleton mySingleton = MySingleton.getInstance(getActivity());
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
        myToDoAdapter = new MyToDoAdapter(getActivity(),R.layout.inflated_layout,R.id.inflated_relative_layout,tmpToDoNote.taskList);
        todoListView = (ListView)view.findViewById(R.id.todo_list_view);

        todoListView.setAdapter(myToDoAdapter);
        todoListView.setItemsCanFocus(true);
        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ///Används inte för tillfället




            }
        });
        int count=0;
        for (int i=0;i<tmpToDoNote.taskList.size();i++){
            if (tmpToDoNote.taskList.get(i).taskDone==true){
                count++;
            }

        }
        tmpToDoNote.completedCount=count;
        tmpToDoNote.totalTasks=tmpToDoNote.taskList.size();

        ///Visar antal slutförda uppgifter/totalt antal uppgifter
        completedText.setText("Completed: "+tmpToDoNote.completedCount+"/"+tmpToDoNote.totalTasks);

        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySingleton mySingleton = MySingleton.getInstance(getActivity());
                Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                ToDoTask tmpTask = new ToDoTask();
                tmpToDoNote.taskList.add(tmpTask);
                tmpToDoNote.totalTasks++;

                myToDoAdapter.notifyDataSetChanged();
                ///todoListView.requestFocus(todoListView.getAdapter().getCount()-1);



            }
        });




            return view;
    }
    public void onPause() {

        super.onPause();

        ///Sparar datum här i stället för SecondActivity, datum för todonote kanske ändras till deadline
        MySingleton mySingleton = MySingleton.getInstance(getActivity());
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        long tmpTime = date.getTime();
        ((ToDoNote)mySingleton.myNoteList.get(mySingleton.listPosition)).date= tmpTime;

    }
    @Override
    public void onResume() {
        super.onResume();


    }


    public class MyToDoAdapter extends ArrayAdapter<ToDoTask> {
        LayoutInflater myToDoInflater;

        public MyToDoAdapter(Context context, int resource, int textViewResourceId, ArrayList<ToDoTask> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        //Denna metod kommer köras när ett element i listan skapas
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {


            MySingleton mySingleton = MySingleton.getInstance(getContext());
            Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
            ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
            myToDoInflater = getActivity().getLayoutInflater();
            View v;
            if (convertView != null) {
                v = convertView;



            } else {
                v = myToDoInflater.inflate(R.layout.inflated_layout, todoListView, false); ///inflatedlayout är namnet på xml-filen


            }

            ImageButton removeTaskButton = (ImageButton)v.findViewById(R.id.remove_task_button);
            CheckBox checkBox = (CheckBox)v.findViewById(R.id.check_box);
            EditText editTaskText = (EditText)v.findViewById(R.id.edit_task_text);

            editTaskText.addTextChangedListener(ToDoNoteFragment.this); ///WATCHER
            editTaskText.setText(tmpToDoNote.taskList.get(position).taskString);
            editTaskText.setTag(tmpToDoNote.taskList.get(position));


            if (tmpToDoNote.taskList.get(position).taskDone==true){
                checkBox.setChecked(true);
            }
            if (tmpToDoNote.taskList.get(position).taskDone==false){
                checkBox.setChecked(false);
            }
            int count=0;
            for (int i=0;i<tmpToDoNote.taskList.size(); i++){

                if (tmpToDoNote.taskList.get(i).taskDone==true){
                    count++;
                    ///checkBox.setChecked(true);
                }
                if (tmpToDoNote.taskList.get(i).taskDone==false){
                   /// checkBox.setChecked(false);
                }

            }
            tmpToDoNote.completedCount=count;
            tmpToDoNote.totalTasks=tmpToDoNote.taskList.size();
            completedText.setText("Completed: "+tmpToDoNote.completedCount+"/"+tmpToDoNote.totalTasks);


            if (tmpToDoNote.taskList.size()==1){removeTaskButton.setAlpha(0.5f);
            } else {removeTaskButton.setAlpha(1f);}
            checkBox.setTag(tmpToDoNote.taskList.get(position));
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MySingleton mySingleton = MySingleton.getInstance(getContext());
                    Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                    ToDoNote tmpToDoNote = (ToDoNote) tmpNote;


                    for (int i=0;i<tmpToDoNote.taskList.size();i++) {
                        CheckBox checkBox = (CheckBox) v.findViewById(R.id.check_box);
                        if (checkBox.getTag()==tmpToDoNote.taskList.get(i)) {


                            if (tmpToDoNote.taskList.get(i).taskDone == true) {
                                tmpToDoNote.taskList.get(i).taskDone = false;
                                checkBox.setChecked(false);
                                tmpToDoNote.taskList.get(i).taskDone = false;
                            } else if (tmpToDoNote.taskList.get(i).taskDone == false){
                                tmpToDoNote.taskList.get(i).taskDone = true;
                                checkBox.setChecked(true);
                            }
                        }
                    }

                    myToDoAdapter.notifyDataSetChanged();

                }
            });
            removeTaskButton.setTag(tmpToDoNote.taskList.get(position));
            removeTaskButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MySingleton mySingleton = MySingleton.getInstance(getContext());
                    Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                    ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                    if (tmpToDoNote.taskList.size() > 1) {
                        ImageButton removeTaskButton = (ImageButton) v.findViewById(R.id.remove_task_button);
                        tmpToDoNote.taskList.remove(removeTaskButton.getTag());
                        tmpToDoNote.totalTasks = tmpToDoNote.totalTasks - 1;
                        completedText.setText("Completed: " + tmpToDoNote.completedCount + "/" + tmpToDoNote.totalTasks);
                        myToDoAdapter.notifyDataSetChanged();
                    }

                }
            });
            ///Räknar och visar antal slutförda uppgifter


            return v;               //här returnerar jag blowup.xmls root-layout
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        MySingleton mySingleton = MySingleton.getInstance(getActivity());
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
        View v;

        int visibleChildCount = (todoListView.getLastVisiblePosition() - todoListView.getFirstVisiblePosition()) + 1;

        for (int i=0;i<visibleChildCount;i++){
            v=todoListView.getChildAt(i);
            int tmpPosition = todoListView.getPositionForView(v);

            EditText editTaskText =(EditText)v.findViewById(R.id.edit_task_text);
            if (editTaskText.getText().hashCode() == s.hashCode()){
                String text=editTaskText.getText().toString();
                tmpToDoNote.taskList.get(tmpPosition).taskString = text;
            }
        }
        }
    }



