package com.ignite.vishrut.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users",MODE_PRIVATE,null);

            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS nU (id INTEGER PRIMARY KEY,name VARCHAR,age INTEGER(3))");


            myDatabase.execSQL("INSERT INTO nU (name,age) VALUES ('Vishrut',20)");
            myDatabase.execSQL("INSERT INTO nU (name,age) VALUES ('Ishan',21)");
            myDatabase.execSQL("INSERT INTO nU (name,age) VALUES ('Papa',53)");
            myDatabase.execSQL("INSERT INTO nU (name,age) VALUES ('Mom',50)");




            Cursor c = myDatabase.rawQuery("SELECT * FROM nU",null);
            int idIndex = c.getColumnIndex("id");
            int nameIndex = c.getColumnIndex("name");
            int ageIndex = c.getColumnIndex("age");

            c.moveToFirst();
            while ( c != null) {
                Log.i("id :: name :: age",Integer.toString(c.getInt(idIndex))+" "+c.getString(nameIndex)+" "+ Integer.toString(c.getInt(ageIndex)));


                c.moveToNext();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
