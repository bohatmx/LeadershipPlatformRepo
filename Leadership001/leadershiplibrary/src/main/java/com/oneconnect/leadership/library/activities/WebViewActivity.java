package com.oneconnect.leadership.library.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;
import android.widget.TextView;
import android.net.http.*;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.HashMap;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    ScrollView scrollView;
    WebView webView;
    Context ctx;
    int position;
    String podcasts, videoUrl,/* videos,*/ links, linkUrl, photos;
    TextView txtName;
    Intent intent;
    List<VideoDTO> videos ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        webView = new WebView(this);
        intent = getIntent();


        scrollView = (ScrollView) findViewById(R.id.scrollView);
        txtName = (TextView) findViewById(R.id.txtName);
        //
        linkUrl = intent.getStringExtra("links");
//        txtName.setText(linkUrl);

       // WebSettings settings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setDomStorageEnabled(true);
        /*webView.getSettings().setPluginState(WebSettings.PluginState.ON);*/
        webView.getSettings().setLoadWithOverviewMode(true);
        //webView.setWebViewClient(new WebViewClient());
        webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();
                    }
                });
        webView.loadUrl(/*"www.google.com"*/linkUrl);
        setContentView(webView);
       // setWebView(position);
    }

    private class leadershipWebViewClient extends  WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url/*WebResourceRequest request*/) {
            webView.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    private static final String TEXT = "text/html", UTF = "UTF-8";

    private void setWebView(int position){
        switch (position){
            case 0:
                webView.loadUrl(linkUrl);
               // webView.loadData(linkUrl/*txtName.getText().toString()*//*videoUrl*//*podcasts.toString()*/, TEXT, UTF);
                break;
            /*case 1:
                webView.loadData(videos.*//*getBytes().*//*toString(), TEXT, UTF);
                break;
            case 2:
                webView.loadData(links.*//*getBytes().*//*toString(), TEXT, UTF);
                break;
            case 3:
                webView.loadData(photos.*//*getBytes().*//*toString(), TEXT, UTF);
                break;*/
        }

    }



    static final String LOG = WebViewActivity.class.getSimpleName();

}
