package com.ignite.vishrut.multipleactivitesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.friendList);

        final ArrayList<String> friends = new ArrayList<>();
        friends.add("Monica");
        friends.add("Chandler");
        friends.add("Joey");
        friends.add("Rachel");
        friends.add("Pheobe");
        friends.add("Ross");
        friends.add("Vishrut");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,friends);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),Second_Activity.class);
                intent.putExtra("name", friends.get(i));

                startActivity(intent);
            }
        });


    }
}
