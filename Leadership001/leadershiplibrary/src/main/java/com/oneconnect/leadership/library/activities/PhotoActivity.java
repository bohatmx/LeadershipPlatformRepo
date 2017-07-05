package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.lists.PhotoListFragment;

public class PhotoActivity extends AppCompatActivity implements PhotoListFragment.PhotoListener{

    PhotoListFragment photoListFragment;
    Context ctx;
    private ListAPI listAPI;
    public static EBookDTO eBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Book Cover Image Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ctx = getApplicationContext();
        listAPI = new ListAPI();

        photoListFragment = (PhotoListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getAllPhotos();
    }

    private void getAllPhotos(){
        listAPI.getAllPhotos(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                PhotoListFragment photoListFragmentFragment = PhotoListFragment.newInstance(bag.getPhotos(), eBook);
            }

            @Override
            public void onError(String messsage) {
                //view.onError(messsage);
            }
        });
    }


    @Override
    public void onPhotoTapped(PhotoDTO photo) {

    }
}
