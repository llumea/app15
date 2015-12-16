package com.example.umyhlarsle.allnotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class SecondActivity extends AppCompatActivity {

    EditText editTitle;
    TextView actionBarTitle;
    ImageButton sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();
        MySingleton mySingleton = MySingleton.getInstance();
        actionBarTitle = (TextView)findViewById(R.id.actionbar_title);
        editTitle = (EditText)findViewById(R.id.edit_title);
        editTitle.setTypeface(null, Typeface.BOLD);
        MyTextWatcher myTextWatcher = new MyTextWatcher(this);
        editTitle.addTextChangedListener(myTextWatcher);
        sendButton = (ImageButton)findViewById(R.id.send_mail_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Sending mail...", Toast.LENGTH_SHORT).show();
                ///If textNote - Send mail with title and content
                ///If todoNote - Send mail with title, tasks and status
            }
        });

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String message = b.getString("SHOW_FRAGMENT");
        int notePosition = b.getInt("SEND_POSITION");
        ///String message = intent.getStringExtra(MainActivity.SHOW_FRAGMENT);

        Log.i("TAG", "String sent in intent: " + message);
        ///OBS list position blir fel vid resume...
        if(savedInstanceState == null) {
            if (message.equals("textnote")) {
                TextNote textNote = new TextNote();
                mySingleton.myNoteList.add(textNote);
                mySingleton.listPosition = mySingleton.myNoteList.size() - 1;
                Log.i("TAG", "OnCreate, new textnote" +"listPosition: " +mySingleton.listPosition);
            }
            if (message.equals("oldtextnote")){
                mySingleton.listPosition = notePosition;
            }
            if (message.equals("todonote")){
                ToDoNote toDoNote = new ToDoNote();
                mySingleton.myNoteList.add(toDoNote);
                mySingleton.listPosition = mySingleton.myNoteList.size() - 1;

                ToDoTask tmpToDoTask = new ToDoTask(true, "I create a task");
                ToDoTask tmpToDoTask2 = new ToDoTask(false, "Second task");
                ToDoTask tmpToDoTask3 = new ToDoTask(true, "Third task");
                Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                tmpToDoNote.taskList.add(tmpToDoTask);
                tmpToDoNote.taskList.add(tmpToDoTask2);
                tmpToDoNote.taskList.add(tmpToDoTask3);
                tmpToDoNote.totalTasks=3;
                Log.i("TAG", "OnCreate, new TODO note" +"listPosition: " +mySingleton.listPosition);
            }
            if (message.equals("oldtodonote")){
                mySingleton.listPosition = notePosition;
            }
        }
            if (message.equals("textnote") || message.equals("oldtextnote")) {
                actionBarTitle.setText("Text Note");
                showTextNoteFragment();
            }
            if (message.equals("todonote") || message.equals("oldtodonote")) {
                actionBarTitle.setText("ToDo Note");
                showToDoNoteFragment();
            }


    }
    @Override
    protected void onPause() {
        super.onPause();
        MySingleton mySingleton = MySingleton.getInstance();
        String currentTitle = editTitle.getText().toString();
        ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).title= currentTitle;

        ///Testar sparade värdet
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        TextNote tmpTextNote = (TextNote) tmpNote;
        String tmpTitle = tmpTextNote.title;
        Log.i("TAG", "Ändrad title?" + tmpTitle + "listPosition: " + mySingleton.listPosition);
        ///saveToMySingleton();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MySingleton mySingleton = MySingleton.getInstance();
        Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
        TextNote tmpTextNote = (TextNote) tmpNote;
        String tmpTitle = tmpTextNote.title;
        editTitle.setText(tmpTitle);
        Log.i("TAG", "Resume title?" + tmpTitle + "listPosition: " + mySingleton.listPosition);
        ///loadFromMySingleton();
    }
    public void onSaveInstanceState(Bundle outInstanceState) {
        outInstanceState.putInt("value", 1); ///Ser till att värdena inte laddas igen
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showTextNoteFragment(){

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment textNoteFragment = new TextNoteFragment(); //typen bör vara Fragment
        transaction.replace(R.id.container_fragments, textNoteFragment, "tag"); //ANVÄND REPLACE ISTÄLLET FÖR ADD lägger till fragmentet till denna ViewGroup
        transaction.addToBackStack(null); //lägger till förändringen till backstack
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //frivillig animation
        transaction.commit(); //slutför förändringen, skall vara sist

    }
    public void showToDoNoteFragment(){

        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        Fragment toDoNoteFragment = new ToDoNoteFragment(); //typen bör vara Fragment
        transaction.replace(R.id.container_fragments, toDoNoteFragment, "tag"); //ANVÄND REPLACE ISTÄLLET FÖR ADD lägger till fragmentet till denna ViewGroup
        transaction.addToBackStack(null); //lägger till förändringen till backstack
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN); //frivillig animation
        transaction.commit(); //slutför förändringen, skall vara sist

    }

}
