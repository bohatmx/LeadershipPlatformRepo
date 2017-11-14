package com.ocg.leadership.company;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oneconnect.leadership.library.data.FCMData;


public class NotifHandlerActivity extends AppCompatActivity {

    private String title,body;
    private FCMData fcmData;
    private Snackbar snackbar;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_handler);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = getIntent().getStringExtra("title");
        body = getIntent().getStringExtra("body");
        if (getIntent().getSerializableExtra("data") != null) {
            fcmData = (FCMData) getIntent().getSerializableExtra("data");
            Log.d(TAG, "we have fcm data");
        }

        if (title != null) {
            showSnackbar(title,"OK","green");
        }

    }
    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }

    static final String TAG = NotifHandlerActivity.class.getSimpleName();
}
