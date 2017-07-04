package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.PodcastAdapter;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.lists.DailyThoughtListFragment;
import com.oneconnect.leadership.library.lists.PodcastListFragment;

public class PodcastActivity extends AppCompatActivity implements PodcastListFragment.PodcastListener {


    private Context ctx;
    private PodcastListFragment podcastListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podcast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        podcastListFragment = (PodcastListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

    }

    @Override
    public void onPodcastTapped(PodcastDTO podcast) {

    }
}
