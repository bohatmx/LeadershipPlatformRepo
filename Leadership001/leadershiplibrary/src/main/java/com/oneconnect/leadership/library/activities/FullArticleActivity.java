package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.NewsDTO;

public class FullArticleActivity extends AppCompatActivity {

    TextView articleTitleTxt, articleSubTitleTxt, bodyTxt;
    Context ctx;
    NewsDTO newsArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setLogo(R.drawable.harmony);

        ctx = getApplicationContext();

        if (getIntent().getSerializableExtra("newsArticle") != null) {
                //  type = ResponseBag.URLS;
                newsArticle = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
        }


        articleTitleTxt = (TextView) findViewById(R.id.articleTitleTxt);
        articleSubTitleTxt = (TextView) findViewById(R.id.articleSubTitleTxt);
        bodyTxt = (TextView) findViewById(R.id.bodyTxt);

        articleTitleTxt.setText(newsArticle.getTitle());
        articleSubTitleTxt.setText(newsArticle.getSubtitle());
        bodyTxt.setText(newsArticle.getBody());
    }

}
