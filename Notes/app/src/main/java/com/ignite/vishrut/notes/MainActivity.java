package com.ignite.vishrut.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList notes = new ArrayList<String>();
    static ArrayAdapter arrayAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.newNote) {
            // jump to new acitivity
            Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView noteList = (ListView) findViewById(R.id.noteList);




        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.ignite.vishrut.notes",Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes",null);

        if(set == null|| set.isEmpty()) {
            notes.add("Click here to add you first note");

        }else {
            notes = new ArrayList<String>(set);
        }
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);
        noteList.setAdapter(arrayAdapter);

        // any list item will jump to a new activity

        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteId",i); // i tells us the row no.

                startActivity(intent);
            }
        });

        noteList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete = i;
                new AlertDialog.Builder(MainActivity.this) //here the application context is gonna create problem
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                         notes.remove(itemToDelete);

                                        arrayAdapter.notifyDataSetChanged();
                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.ignite.vishrut.notes", Context.MODE_PRIVATE);
                                        HashSet<String> set = new HashSet<String>(MainActivity.notes);

                                        sharedPreferences.edit().putStringSet("notes",set).apply();

                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });


        }

    }
