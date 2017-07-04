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
    String  linkUrl;
    TextView txtName;
    Intent intent;

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
        linkUrl = intent.getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public void onReceivedSslError (WebView view, SslErrorHandler handler, SslError error) {
                        handler.proceed();
                    }
                });
        webView.loadUrl(linkUrl);
        setContentView(webView);
    }
    private static final String TEXT = "text/html", UTF = "UTF-8";





    static final String LOG = WebViewActivity.class.getSimpleName();

}
