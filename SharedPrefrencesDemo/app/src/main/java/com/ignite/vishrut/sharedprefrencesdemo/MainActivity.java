package com.ignite.vishrut.sharedprefrencesdemo;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.SharedPreferencesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.ignite.vishrut.sharedprefrencesdemo", Context.MODE_PRIVATE);
        // to save an item in sharedPreferences
        //sharedPreferences.edit().putString("username","vishrut").apply();

        //to access data in sharedPreferences
       // String username = sharedPreferences.getString("username",""); // the second item is default value

        //Log.i("username",username);

        //Now for arrayList
        ArrayList<String> friends = new ArrayList<>();

        friends.add("monica");
        friends.add("ross");
        try {
            sharedPreferences.edit().putString("friends",ObjectSerializer.serialize(friends)).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> newFriends = new ArrayList<>();
        try {
            newFriends = (ArrayList<String>)ObjectSerializer.deserialize(sharedPreferences.getString("friends",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("newFriends",newFriends.toString());
    }
}
