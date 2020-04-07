package com.ignite.vishrut.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.GenericArrayType;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //database for articles
    SQLiteDatabase articlesDB;

    //content array list
    ArrayList<String> content = new ArrayList<>();

    //Llist for news articles
    ListView newsList ;

    ArrayList<String> titles = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsList = (ListView) findViewById(R.id.newsList);
        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,titles);
        newsList.setAdapter(arrayAdapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),ArticleActivity.class);
                intent.putExtra("content", content.get(i));
                startActivity(intent);
            }
        });

        articlesDB = this.openOrCreateDatabase("Articles",MODE_PRIVATE,null);
        articlesDB.execSQL("CREATE TABLE IF NOT EXISTS articles (id INTEGER PRIMARY KEY,articleId INTEGER,title VARCHAR,content VARCHAR)");

        updateListView();
        // to get the ids of all the news articles which are new (as decided by the Hacker news api)
        DownloadTask task = new DownloadTask();
        try {

            task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
        }
        catch(Exception e ) {
            e.printStackTrace();
        }
    }
    public void updateListView() {
        Cursor c = articlesDB.rawQuery("SELECT * FROM articles",null);

        int contentIndex = c.getColumnIndex("content");
        int titleIndex = c.getColumnIndex("title");
        if(c.moveToFirst()) {
            titles.clear();
            content.clear();
            do {
                titles.add(c.getString(titleIndex));
                content.add(c.getString(contentIndex));

            }while (c.moveToNext());
            arrayAdapter.notifyDataSetChanged();
        }
    }

    //we have ot download the api and to get the url we need a download class
    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }


                int numberOfArticles = 20;

                JSONArray jsonArray = new JSONArray(result);
                if(jsonArray.length() < 20) {
                    numberOfArticles = jsonArray.length();
                }
                // since the db can be appended , we need it to clear it first
                articlesDB.execSQL("DELETE FROM articles");
                for(int i = 0; i < numberOfArticles ; i++) {
                    //we get about 500 articles id
                    String articleId = jsonArray.getString(i);
                    url = new URL("https://hacker-news.firebaseio.com/v0/item/"+articleId +".json?print=pretty");

                    urlConnection = (HttpURLConnection) url.openConnection();
                    in = urlConnection.getInputStream();
                    reader = new InputStreamReader(in);
                    data = reader.read();
                    String articleInfo = "";
                    while (data != -1) {
                        char current = (char) data;
                        articleInfo += current;
                        data = reader.read();

                    }
                    //here we get the jsons for the first 20 articles to be displayed
                    JSONObject jsonObject = new JSONObject(articleInfo);
                    // we create the json s in a bigger json

                    //this api is not correct for the last article hence we need to provide manual check
                    if(!jsonObject.isNull("title") && !jsonObject.isNull("url")) {
                        String articleTitle = jsonObject.getString("title");
                        String articleURL = jsonObject.getString("url");
                        url = new URL(articleURL);
                        urlConnection = (HttpURLConnection) url.openConnection();

                        in = urlConnection.getInputStream();
                        reader = new InputStreamReader(in);
                        data = reader.read();
                        String articleContent= "";
                        while (data != -1) {
                            char current = (char) data;
                            articleInfo += current;
                            data = reader.read();

                        }
                        //we get the article content here //html data
                        String sql = "INSERT INTO articles (articleId, title, content) VALUES (? ,? , ?)";
                        SQLiteStatement statement = articlesDB.compileStatement(sql);

                        statement.bindString(1, articleId);
                        statement.bindString(2, articleTitle);
                        statement.bindString(3, articleContent);
                        statement.execute();
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override //after downloading task
        protected void onPostExecute(String s) {
            updateListView();

            super.onPostExecute(s);
        }
    }

}
