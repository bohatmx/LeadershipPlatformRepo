package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.EbookAdapter;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.lists.EBookListFragment;
import com.oneconnect.leadership.library.lists.VideoListFragment;

public class eBookActivity extends AppCompatActivity implements EBookListFragment.EBookListener {

    private Context ctx;
    private EBookListFragment eBooksListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_book);
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


        eBooksListFragment = (EBookListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);

    }

    @Override
    public void onEBookTapped(EBookDTO eBook) {

    }
}
