package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.NewsArticleAdapter;
import com.oneconnect.leadership.library.cache.CachePresenter;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.lists.EntityListFragment;
import com.oneconnect.leadership.library.lists.NewsListFragment;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.List;

/**
 * Created by Kurisani on 2017/06/26.
 */

public class NewsArticleActivity extends AppCompatActivity implements
        NewsArticleAdapter.NewsArticleListener, NewsListFragment.NewsArticleListener {

    NewsListFragment newsListFragment;
    SubscriberPresenter presenter;
    private CachePresenter cachePresenter;
    private Context ctx;
    ResponseBag bag;
    private UserDTO user;
    private int type;
    EntityListFragment entityListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        newsListFragment = (NewsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);


        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        //fab.setVisibility(View.GONE);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment();

            }
        });*/

        user = SharedPrefUtil.getUser(this);

    }

    private void setFragment() {
        Log.w(TAG, "setFragment: starting new fragment");
        SharedPrefUtil.saveFragmentType(bag.getType(), this);
        type = bag.getType();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        entityListFragment = EntityListFragment.newInstance(bag);

        ft.replace(R.id.frame, entityListFragment);
        ft.commit();
    }



    @Override
    public void onArticleSelected(NewsDTO newsArticle) {

    }

    @Override
    public void onPldpRequested(NewsDTO news) {

    }

    @Override
    public void onPhotoRequired(PhotoDTO photo) {

    }

    @Override
    public void onVideoRequired(VideoDTO video) {

    }

    @Override
    public void onPodcastRequired(PodcastDTO podcast) {

    }

    @Override
    public void onUrlRequired(UrlDTO url) {

    }

    @Override
    public void onPhotosRequired(List<PhotoDTO> list) {

    }

    static final String TAG = NewsArticleActivity.class.getSimpleName();

    @Override
    public void onNewsArticleTapped(NewsDTO article) {

    }
}
