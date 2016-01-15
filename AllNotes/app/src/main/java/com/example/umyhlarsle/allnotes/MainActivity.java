package com.example.umyhlarsle.allnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    ListView listView;
    ImageButton createTextNoteButton;
    ImageButton createToDoNoteButton;
    TextView sortButton;
    SavedNotes savedNotes;
    ImageView instructionView;

    public static final String SAVE_FILE = "filename_notes";
    public static final String SAVE_KEY = "key_notes";
    int noteImage = R.drawable.newnote;
    int todoImage = R.drawable.newtodo;
    Gson gson;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        gson = new Gson();
        pref = getSharedPreferences(SAVE_FILE, 0);
        savedNotes = new SavedNotes();


        MySingleton mySingleton = MySingleton.getInstance(this);
        if (mySingleton.myNoteList.size()==0){load();}
        instructionView = (ImageView)findViewById(R.id.instruction_view);


        createTextNoteButton = (ImageButton)findViewById(R.id.new_note_button);
        createTextNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Om anteckningen skapas i SecondActivity räcker det att skicka "newtextnote" här
                MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
                Bundle bundle = new Bundle();
                bundle.putString("SHOW_FRAGMENT", "textnote");
                bundle.putInt("SEND_POSITION", mySingleton.myNoteList.size()-1);
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtras(bundle);
               /// intent.putExtra(SHOW_FRAGMENT,"textnote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                startActivity(intent);
            }
        });
        createToDoNoteButton = (ImageButton)findViewById(R.id.new_todo_button);
        createToDoNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("SHOW_FRAGMENT", "todonote");
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
               /// intent.putExtra(SHOW_FRAGMENT,"todonote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        sortButton = (TextView)findViewById(R.id.sort_button);
        showHideInstructions();
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
                if (mySingleton.myNoteList.size()>0) {
                    if (mySingleton.sortOrder == mySingleton.SORT_BY_TYPE) {
                        sortListViewByDate();

                        mySingleton.sortOrder = mySingleton.SORT_BY_DATE;
                        sortButton.setText("SORT BY TYPE");
                    } else if (mySingleton.sortOrder == mySingleton.SORT_BY_DATE) {
                        sortListViewByType();

                        mySingleton.sortOrder = mySingleton.SORT_BY_TYPE;
                        sortButton.setText("SORT BY DATE");
                    }
                }


            }
        });

        adapter = new MyAdapter(this,R.layout.list_layout,R.id.list_layout_id,mySingleton.myNoteList);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ///showFragment();
                MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
                ///Obs båda är instance of textnote. Börjar därför med det underordnade objektet
                if (mySingleton.myNoteList.get(position) instanceof ToDoNote) {

                    Bundle bundle = new Bundle();
                    bundle.putString("SHOW_FRAGMENT", "oldtodonote");
                    bundle.putInt("SEND_POSITION", position);
                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                    intent.putExtras(bundle);
                    /// intent.putExtra(SHOW_FRAGMENT,"textnote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                    startActivity(intent);


                }
                else if (mySingleton.myNoteList.get(position) instanceof TextNote) {

                Bundle bundle = new Bundle();
                bundle.putString("SHOW_FRAGMENT", "oldtextnote");
                bundle.putInt("SEND_POSITION", position);
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtras(bundle);
                /// intent.putExtra(SHOW_FRAGMENT,"textnote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                startActivity(intent);


            }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

      save();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ///MySingleton mySingleton = MySingleton.getInstance(this);
       /// mySingleton.save(this);
        ///save();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());

        if (mySingleton.myNoteList.size()>mySingleton.listPosition) {
    ///Börjar med todotnote!
            if (mySingleton.myNoteList.get(mySingleton.listPosition) instanceof ToDoNote) {
                ToDoNote tmpToDoNote;
                tmpToDoNote = (ToDoNote) mySingleton.myNoteList.get(mySingleton.listPosition);

                if (tmpToDoNote.title.equals("")&& !tmpToDoNote.taskList.get(0).taskString.equals("")){

                    String tmpTitle = tmpToDoNote.taskList.get(0).taskString;
                    tmpTitle = tmpTitle.replace("\n"," ");
                    if (tmpTitle.length()>25){tmpTitle=tmpTitle.substring(0,20);}
                    tmpToDoNote.title = tmpTitle;
                }
                else if (tmpToDoNote.title.equals("") && tmpToDoNote.taskList.get(0).taskString.equals("")) {
                    mySingleton.myNoteList.remove(mySingleton.listPosition);
                    Toast.makeText(getApplicationContext(), "Title and first task fields were empty. The todo note was deleted.", Toast.LENGTH_LONG).show();

                }
            }
            else if (mySingleton.myNoteList.get(mySingleton.listPosition) instanceof TextNote) {
                TextNote tmpTextNote;
                tmpTextNote = (TextNote) mySingleton.myNoteList.get(mySingleton.listPosition);

                if (tmpTextNote.title.equals("")&& !tmpTextNote.content.equals("")){

                    String tmpTitle = tmpTextNote.content;
                    tmpTitle = tmpTitle.replace("\n"," ");
                    if (tmpTitle.length()>25){tmpTitle=tmpTitle.substring(0,20);}
                    tmpTextNote.title = tmpTitle;
                }
                else if (tmpTextNote.title.equals("") && tmpTextNote.content.equals("")) {
                    mySingleton.myNoteList.remove(mySingleton.listPosition);
                    Toast.makeText(getApplicationContext(), "Title and content fields were empty. The text note was deleted.", Toast.LENGTH_LONG).show();



                }
            }
        }

        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onSaveInstanceState(Bundle outInstanceState) {
        outInstanceState.putInt("value", 1); ///Ser till att värdena inte laddas igen
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
    public class MyAdapter extends ArrayAdapter<Note> {
        LayoutInflater myInflater;

        public MyAdapter(Context context, int resource, int textViewResourceId, ArrayList<Note> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        //Denna metod kommer köras när ett element i listan skapas
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            showHideInstructions();

            myInflater = getLayoutInflater();
            View v = myInflater.inflate(R.layout.list_layout, listView, false); ///inflatedlayout är namnet på xml-filen
            ImageButton removeButton = (ImageButton)v.findViewById(R.id.remove_button);
            TextView title = (TextView)v.findViewById(R.id.note_title);
            TextView date = (TextView)v.findViewById(R.id.note_date);
            TextView completed = (TextView)v.findViewById(R.id.note_completed);
            ImageView noteIcon = (ImageView)v.findViewById(R.id.note_icon);
            MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());



            if (mySingleton.myNoteList.get(position) instanceof TextNote) {
                Note tmpNote = mySingleton.myNoteList.get(position);
                TextNote tmpTextNote = (TextNote) tmpNote;
                title.setText(tmpTextNote.title);
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
                String formatedDate = sdf.format(tmpTextNote.date);
                date.setText(formatedDate);
                noteIcon.setImageResource(noteImage);
                completed.setText("");
            }
            if (mySingleton.myNoteList.get(position) instanceof ToDoNote) {
                Note tmpNote = mySingleton.myNoteList.get(position);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                title.setText(tmpToDoNote.title);
                SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy HH:mm:ss");
                String formatedDate = sdf.format(tmpToDoNote.date);
                date.setText(formatedDate);
                noteIcon.setImageResource(todoImage);
                completed.setText(tmpToDoNote.completedCount+"/"+tmpToDoNote.totalTasks);
            }
            removeButton.setTag(mySingleton.myNoteList.get(position));

            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
                    ImageButton removeButton = (ImageButton)v.findViewById(R.id.remove_button);
                    mySingleton.myNoteList.remove(removeButton.getTag());
                    adapter.notifyDataSetChanged();
                    showHideInstructions();

                }
            });

            return v;               //här returnerar jag blowup.xmls root-layout
        }
    }

    public void load(){
        pref = getSharedPreferences(SAVE_FILE, 0);
        String tmpString = pref.getString(SAVE_KEY, null);



        if (tmpString==null){
            ///Do nothing
        }else{

            savedNotes=gson.fromJson(tmpString,SavedNotes.class);
            MySingleton mySingleton = MySingleton.getInstance(this);
            for (int i=0;i<savedNotes.myTextNotes.size();i++){
                mySingleton.myNoteList.add(savedNotes.myTextNotes.get(i));
            }
            for (int i=0;i<savedNotes.myToDoNotes.size();i++){
                mySingleton.myNoteList.add(savedNotes.myToDoNotes.get(i));
            }

        }


    }
    public void save(){

        savedNotes.myTextNotes.clear();
        savedNotes.myToDoNotes.clear();

       MySingleton saveSingleton = MySingleton.getInstance(this);
        for (int i=0;i<saveSingleton.myNoteList.size();i++){

            if (saveSingleton.myNoteList.get(i) instanceof ToDoNote){
                ToDoNote tmpToDoNote;
                tmpToDoNote = (ToDoNote)saveSingleton.myNoteList.get(i);
                savedNotes.myToDoNotes.add(tmpToDoNote);
            }
            else if (saveSingleton.myNoteList.get(i) instanceof TextNote){
                TextNote tmpTextNote;
                tmpTextNote = (TextNote)saveSingleton.myNoteList.get(i);
                savedNotes.myTextNotes.add(tmpTextNote);
            }

        }
        ///savedNotes.mySavedNotes = saveSingleton.myNoteList; ///Uppdaterar listan innan sparandet
        String jsonString = gson.toJson(savedNotes);

        SharedPreferences.Editor editor =pref.edit();
        editor.putString(SAVE_KEY, jsonString);
        editor.commit();

    }
    public void sortListViewByDate(){
        ArrayList<Long> tmpArrayList = new ArrayList<>();

        MySingleton mySingleton = MySingleton.getInstance(this);
        ///Lägger till alla datum i en arraylist
        for (int i=0;i<mySingleton.myNoteList.size();i++){
            if (mySingleton.myNoteList.get(i) instanceof TextNote){
                tmpArrayList.add(((TextNote)mySingleton.myNoteList.get(i)).date);
            }
            if (mySingleton.myNoteList.get(i) instanceof ToDoNote){
                tmpArrayList.add(((ToDoNote)mySingleton.myNoteList.get(i)).date);
            }
        }
        ///Sorterar alla datum i arraylistan
        Collections.sort(tmpArrayList);
        ///Kollar vilket objekt datumet tillhör och lägger till i en tmpArrayList
        for (int i=0;i<tmpArrayList.size();i++){
            for (int j=0;j<mySingleton.myNoteList.size();j++) {
                if (mySingleton.myNoteList.get(j) instanceof TextNote) {
                    if (tmpArrayList.get(i)==((TextNote)mySingleton.myNoteList.get(j)).date){
                        TextNote tmpTextNote;
                        tmpTextNote = (TextNote)mySingleton.myNoteList.get(j);
                        mySingleton.myNoteList.remove(j);
                        mySingleton.myNoteList.add(tmpTextNote);

                    }
                 else if (mySingleton.myNoteList.get(j) instanceof ToDoNote) {
                     if (tmpArrayList.get(i) == ((ToDoNote) mySingleton.myNoteList.get(j)).date) {
                         ToDoNote tmpToDoNote;
                         tmpToDoNote = (ToDoNote)mySingleton.myNoteList.get(j);
                         mySingleton.myNoteList.remove(j);
                         mySingleton.myNoteList.add(tmpToDoNote);
                     }
                 }
                }

            }
        }

        adapter.notifyDataSetChanged();
        tmpArrayList.clear();


    }
    public void sortListViewByType(){

        MySingleton mySingleton = MySingleton.getInstance(this);
        ///Lägger till alla datum i en arraylist
        int count=0;
        for (int i=0;i<mySingleton.myNoteList.size();i++){

            if (mySingleton.myNoteList.get(i) instanceof ToDoNote){
                ToDoNote tmpToDoNote;
                tmpToDoNote = (ToDoNote)mySingleton.myNoteList.get(i);
                mySingleton.myNoteList.remove(i);
                mySingleton.myNoteList.add(count,tmpToDoNote);
                count++;

            }
        }

        adapter.notifyDataSetChanged();

    }



public void showHideInstructions(){
    MySingleton mySingleton = MySingleton.getInstance(getApplicationContext());
    if (mySingleton.myNoteList.size()==0){
        instructionView.setVisibility(View.VISIBLE);

    }
    else{
        instructionView.setVisibility(View.INVISIBLE);
    }
    ///Text i Sorteringen
    if (mySingleton.myNoteList.size()>0) {

        if (mySingleton.sortOrder == mySingleton.SORT_BY_DATE) {
            sortButton.setText("SORT BY TYPE");
        }
        if (mySingleton.sortOrder == mySingleton.SORT_BY_TYPE) {
            sortButton.setText("SORT BY DATE");
        }
    }else{
        sortButton.setText("NO NOTES AVAILABLE");
    }

}

}
