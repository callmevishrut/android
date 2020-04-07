package com.ignite.vishrut.webviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true); // JavaScripts are enabled in the webView
        webView.setWebViewClient(new WebViewClient()); // to show the website in the webview itself
        webView.loadUrl("http://www.google.com");
        // webView.loadData("<html>......</html>","text/html","UTF-8"); // this can also be used to display the html data

    }
}
