package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;

public class FullPhotoActivity extends AppCompatActivity {

    Context ctx;
    ImageView fullPhoto;
    PhotoDTO photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.harmony);

        ctx = getApplicationContext();

        fullPhoto = (ImageView) findViewById(R.id.fullPhoto);

        if (getIntent().getSerializableExtra("photo") != null) {
            photo = (PhotoDTO) getIntent().getSerializableExtra("photo");
            getSupportActionBar().setSubtitle(photo.getCaption());
            Glide.with(ctx)
                    .load(photo.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fullPhoto);
        }

    }

}
