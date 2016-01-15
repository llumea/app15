package com.example.umyhlarsle.allnotes;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
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
        MySingleton mySingleton = MySingleton.getInstance(this);
        actionBarTitle = (TextView)findViewById(R.id.actionbar_title);
        editTitle = (EditText)findViewById(R.id.edit_title);
        editTitle.setTypeface(null, Typeface.BOLD);
        MyTextWatcher myTextWatcher = new MyTextWatcher(this);
        editTitle.addTextChangedListener(myTextWatcher);
        sendButton = (ImageButton)findViewById(R.id.send_mail_button);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
                ///Obs båda är instance of textnote. Börjar därför med det underordnade objektet
                if (mySingleton.myNoteList.get(mySingleton.listPosition) instanceof TextNote) {
                    String tmpTitle = ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).title;
                    String tmpContent = ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).content;

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);

                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + tmpTitle);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "" + tmpContent +"\n" + "\n"+"Sent from All Notes app");
                    emailIntent.setData(Uri.parse("mailto:")); // just "mailto:" for blank
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(SecondActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }
                if (mySingleton.myNoteList.get(mySingleton.listPosition) instanceof ToDoNote) {
                    StringBuilder stringBuilder = new StringBuilder();
                    String tmpTitle = ((TextNote)mySingleton.myNoteList.get(mySingleton.listPosition)).title;
                    Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                    ToDoNote tmpToDoNote = (ToDoNote) tmpNote;

                    for (int i=0;i<tmpToDoNote.taskList.size();i++){
                        String tmpDone="Something";
                        if (tmpToDoNote.taskList.get(i).taskDone==true){tmpDone="(done) ";}
                        else if (tmpToDoNote.taskList.get(i).taskDone==false){tmpDone="(todo) ";}
                        stringBuilder.append(""+tmpDone + tmpToDoNote.taskList.get(i).taskString +"\n");
                    }
                    String tmpContent = stringBuilder.toString();
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    ///emailIntent.setData(Uri.parse("mailto:" + "lennilarsson@hotmail.com"));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + tmpTitle);
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "" + tmpContent +"\n" + "\n"+"Sent from All Notes app");
                    emailIntent.setData(Uri.parse("mailto:")); // just "mailto:" for blank
                    emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send email using..."));
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(SecondActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
                    }

                }

                Toast.makeText(getApplicationContext(), "Sending mail...", Toast.LENGTH_SHORT).show();
                ///If textNote - Send mail with title and content
                ///If todoNote - Send mail with title, tasks and status
            }
        });

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String message = b.getString("SHOW_FRAGMENT");
        int notePosition = b.getInt("SEND_POSITION");

        if(savedInstanceState == null) {
            if (message.equals("textnote")) {
                TextNote textNote = new TextNote();
                mySingleton.myNoteList.add(textNote);
                mySingleton.listPosition = mySingleton.myNoteList.size() - 1;

            }
            if (message.equals("oldtextnote")){
                mySingleton.listPosition = notePosition;
            }
            if (message.equals("todonote")){
                ToDoNote toDoNote = new ToDoNote();
                mySingleton.myNoteList.add(toDoNote);
                mySingleton.listPosition = mySingleton.myNoteList.size() - 1;

                ToDoTask tmpToDoTask = new ToDoTask(false, "");

                Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                tmpToDoNote.taskList.add(tmpToDoTask);
                tmpToDoNote.totalTasks=1;

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
        MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
        if (mySingleton.myNoteList.size()>0) {
            String currentTitle = editTitle.getText().toString();
            ((TextNote) mySingleton.myNoteList.get(mySingleton.listPosition)).title = currentTitle;

            ///Testar sparade värdet
            Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
            TextNote tmpTextNote = (TextNote) tmpNote;
            String tmpTitle = tmpTextNote.title;

        }
        ///saveToMySingleton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
        if (mySingleton.myNoteList.size()>0) {
            Note tmpNote = mySingleton.myNoteList.get(mySingleton.listPosition);
            TextNote tmpTextNote = (TextNote) tmpNote;
            String tmpTitle = tmpTextNote.title;
            editTitle.setText(tmpTitle);

        }
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
