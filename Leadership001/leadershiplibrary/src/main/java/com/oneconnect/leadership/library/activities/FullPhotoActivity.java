package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullPhotoActivity extends AppCompatActivity {

    Context ctx;
    ImageView fullPhoto;
    PhotoDTO photo;
    Bitmap source;
    float angle=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.GONE);
       // toolbar.setLogo(R.drawable.harmony);

        ctx = getApplicationContext();

        fullPhoto = (ImageView) findViewById(R.id.fullPhoto);


        if (getIntent().getSerializableExtra("photo") != null) {
            photo = (PhotoDTO) getIntent().getSerializableExtra("photo");
            getSupportActionBar().setSubtitle(photo.getCaption());
            Glide.with(ctx)
                    .load(photo.getUrl())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(fullPhoto);
           // getBitmapFromURL(photo.getUrl());
           // fullPhoto.setImageBitmap(getBitmapFromURL(photo.getUrl()));

           /* fullPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    angle+=90;
                    Bitmap rotatedImage = rotateImage(getBitmapFromURL(photo.getUrl()),angle);
                    fullPhoto.setImageBitmap(rotatedImage);

                }
            });*/
        }
    }



    /*public static Bitmap getBitmapFromURL(final String src) {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap myBitmap = null;
                try {
                    URL url = new URL(src);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    myBitmap = BitmapFactory.decodeStream(input);
                //    return myBitmap;
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
        thread.start();
        return myBitmap;
    }*/

    public static Bitmap rotateImage(Bitmap sourceImage, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(sourceImage, 0, 0, sourceImage.getWidth(), sourceImage.getHeight(), matrix, true);
    }

    }


