package com.ignite.vishrut.multipleactivitesdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Second_Activity extends AppCompatActivity {
    public void goBack() {
        Intent i;
        i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_);

        Intent intent = getIntent();
        Toast.makeText(this, "You have clicked on " +  intent.getStringExtra("name"), Toast.LENGTH_SHORT).show();
    }
}
