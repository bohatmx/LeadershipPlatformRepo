package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FullArticleActivity extends AppCompatActivity {

    TextView articleTitleTxt, articleSubTitleTxt, bodyTxt;
    Context ctx;
    NewsDTO newsArticle;
    ImageView articleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setVisibility(View.GONE);
       // toolbar.setLogo(R.drawable.harmony);

        ctx = getApplicationContext();
        articleImage = (ImageView) findViewById(R.id.articleImage);

        if (getIntent().getSerializableExtra("newsArticle") != null) {
                //  type = ResponseBag.URLS;
                newsArticle = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
                if (newsArticle.getPhotos() != null) {
                    List<PhotoDTO> urlList = new ArrayList<>();

                    Map map = newsArticle.getPhotos();
                    PhotoDTO vDTO;
                    String photoUrl;
                    for (Object value : map.values()) {
                        vDTO = (PhotoDTO) value;
                        photoUrl = vDTO.getUrl();
                        urlList.add(vDTO);

                        Glide.with(ctx)
                                .load(photoUrl)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(articleImage);
                        //   dvh.captiontxt.setText(vDTO.getCaption());
                    }
                }
        }


        articleTitleTxt = (TextView) findViewById(R.id.articleTitleTxt);
        articleSubTitleTxt = (TextView) findViewById(R.id.articleSubTitleTxt);
        bodyTxt = (TextView) findViewById(R.id.bodyTxt);

        articleTitleTxt.setText(newsArticle.getTitle());
        articleSubTitleTxt.setText(newsArticle.getSubtitle());
        bodyTxt.setText(newsArticle.getBody());
    }

}
