package com.example.umyhlarsle.allnotes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyAdapter adapter;
    ListView listView;
    ImageButton createTextNoteButton;
    ImageButton createToDoNoteButton;
    TextView sortButton;
    static ArrayList<Note> myNoteList = new ArrayList<>();
    public static final String SHOW_FRAGMENT = "show_fragment_key";
    public static final String SEND_POSITION = "send_position_key";
    public static final String SAVE_FILE = "filename_notes";
    public static final String SAVE_KEY = "key_notes";
    int noteImage = R.drawable.newnote;
    int todoImage = R.drawable.newtodo;
    Gson gson;
    SharedPreferences pref;
    static SavedNotes savedNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        gson = new Gson();
        pref = getSharedPreferences(SAVE_FILE, 0);
        savedNotes=new SavedNotes();

        ///Testar användning av ArrayList i ListView
        if(savedInstanceState == null) {

            ///Initial LOAD från disk här och sedan ladda från MySingleton medan appen lever???

            TextNote textNote1 = new TextNote("Idea", "My fabulous idea", 125);
            myNoteList.add(textNote1);
            HashMap<String, Boolean> tmpMap = new HashMap<>();
            tmpMap.put("My First Task", false);
            ToDoNote todonote1 = new ToDoNote("My Grocery List", "", 145, tmpMap);
            myNoteList.add(todonote1);
            TextNote textNote2 = new TextNote("No idea yet", "My fabulous idea", 125);
            myNoteList.add(textNote2);
            HashMap<String, Boolean> tmpMap2 = new HashMap<>();
            tmpMap2.put("Another First Task", false);
            ToDoNote todonote2 = new ToDoNote("My X-mas list", "", 145, tmpMap2);
            myNoteList.add(todonote2);
        }
        createTextNoteButton = (ImageButton)findViewById(R.id.new_note_button);
        createTextNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Om anteckningen skapas i SecondActivity räcker det att skicka "newtextnote" här
                TextNote textNote = new TextNote("Title","Content",125);
                myNoteList.add(textNote);
                adapter.notifyDataSetChanged();///Inte så snyggt att det skrivs ut innan aktivitetsbytet. Lägg till i onPause i SecondActivity?

                ///Testar
                Note tmpNote = myNoteList.get(myNoteList.size()-1);
                TextNote tmpTextNote = (TextNote) tmpNote;
                int tmpposition = myNoteList.size()-1;
                Log.i("TAG", "Kollar index på ny item i ArrayList: " +tmpposition+tmpTextNote.title+tmpTextNote.content);
                ///
                Bundle bundle = new Bundle();
                bundle.putString("SHOW_FRAGMENT", "textnote");
                bundle.putInt("SEND_POSITION", myNoteList.size()-1);
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
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ///SORTERA LISTA, ändra ordning på myNoteList
                Toast.makeText(getApplicationContext(), "Sorting list...", Toast.LENGTH_SHORT).show();

            }
        });
        adapter = new MyAdapter(this,R.layout.list_layout,R.id.list_layout_id,myNoteList);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ///showFragment();

                if (myNoteList.get(position) instanceof TextNote) { Bundle bundle = new Bundle();
                bundle.putString("SHOW_FRAGMENT", "oldtextnote");
                bundle.putInt("SEND_POSITION", position);
                Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                intent.putExtras(bundle);
                /// intent.putExtra(SHOW_FRAGMENT,"textnote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                startActivity(intent);

                Log.i("TAG", "List item clicked: " +myNoteList.get(position));
                Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();

            }
                if (myNoteList.get(position) instanceof ToDoNote) {
                    Bundle bundle = new Bundle();
                    bundle.putString("SHOW_FRAGMENT", "oldtodonote");
                    bundle.putInt("SEND_POSITION", position);
                    Intent intent = new Intent(MainActivity.this,SecondActivity.class);
                    intent.putExtras(bundle);
                    /// intent.putExtra(SHOW_FRAGMENT,"textnote");///Så här hade jag kunna skicka utan bundle till SecondActivity
                    startActivity(intent);

                    Log.i("TAG", "List item clicked: " +myNoteList.get(position));
                    Toast.makeText(getApplicationContext(), "CLICKED", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///saveToFile();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ///loadFromFile();
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
            //convertView i det här fallet verkar vara rootvyn i list_layout.xml
            //parent verkar vara ListView
            //position är positionen i myList
            ///Log.i("TAG", "Antal objekt i getView: " + sparadeObjekt.minaSparadeObjekt.size());
            myInflater = getLayoutInflater();
            View v = myInflater.inflate(R.layout.list_layout, listView, false); ///inflatedlayout är namnet på xml-filen
            ImageButton removeButton = (ImageButton)v.findViewById(R.id.remove_button);
            TextView title = (TextView)v.findViewById(R.id.note_title);
            TextView date = (TextView)v.findViewById(R.id.note_date);
            TextView completed = (TextView)v.findViewById(R.id.note_completed);
            ImageView noteIcon = (ImageView)v.findViewById(R.id.note_icon);

            if (myNoteList.get(position) instanceof TextNote) {
                Note tmpNote = myNoteList.get(position);
                TextNote tmpTextNote = (TextNote) tmpNote;
                title.setText(tmpTextNote.title);
                noteIcon.setImageResource(noteImage);
                completed.setText("");
            }
            if (myNoteList.get(position) instanceof ToDoNote) {
                Note tmpNote = myNoteList.get(position);
                ToDoNote tmpToDoNote = (ToDoNote) tmpNote;
                title.setText(tmpToDoNote.title);
                noteIcon.setImageResource(todoImage);
                completed.setText("0/17");
            }
            removeButton.setTag(myNoteList.get(position));
           /// editText.setText(sparadeObjekt.minaSparadeObjekt.get(position).minString);

           /// editText.setTag(sparadeObjekt.minaSparadeObjekt.get(position));

           /// button.setTag(sparadeObjekt.minaSparadeObjekt.get(position));
            removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ImageButton removeButton = (ImageButton)v.findViewById(R.id.remove_button);
                    ///sparadeObjekt.minaSparadeObjekt.remove(button.getTag());
                    myNoteList.remove(removeButton.getTag());
                    adapter.notifyDataSetChanged();

                }
            });

            return v;               //här returnerar jag blowup.xmls root-layout
        }
    }

    public void loadFromFile(){

        String tmpString = pref.getString(SAVE_KEY, null);

        if (tmpString==null){
            ///Show instruction image?
            Log.i("TAG", "Nytt objekt skapas " + tmpString);
        }else{
            savedNotes=gson.fromJson(tmpString,SavedNotes.class);
            ///Fylla MySingleton?
            Log.i("TAG", "LADDAR");
        }

    }
    public void saveToFile(){

        String jsonString = gson.toJson(savedNotes);
        SharedPreferences.Editor editor =pref.edit();
        editor.putString(SAVE_KEY, jsonString);
        editor.commit();

    }
}
