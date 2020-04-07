package com.ignite.vishrut.languagepreferences;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView langView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.language_menu,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.english :
                setLanguage("English");
                return true;
            case R.id.spanish :
                setLanguage("Spanish");
                return true;
            default: return false;
        }
    }

    public void setLanguage(String lang){
        //save the language permanantly and show it in the text view

        sharedPreferences.edit().putString("language",lang).apply();


        langView.setText(lang);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = this.getSharedPreferences("com.ignite.vishrut.languagepreferences",MODE_PRIVATE);
        langView = (TextView) findViewById(R.id.langView);

        String language = sharedPreferences.getString("language","");

        if(language == "")
        {
            //we dont want the alert dialog to pop up every time we run the app
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Choose a language")
                    .setMessage("which language would you like ?")
                    .setPositiveButton("English", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("English");

                        }

                    })
                    .setNegativeButton("Spanish", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setLanguage("Spanish");
                        }
                    })
                    .show();
        }
        else
            langView.setText(language);

    }
}
