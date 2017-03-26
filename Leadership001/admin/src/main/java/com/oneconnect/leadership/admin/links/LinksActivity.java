package com.oneconnect.leadership.admin.links;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

public class LinksActivity extends AppCompatActivity {
    public static final String TAG = LinksActivity.class.getSimpleName();
    private TextView txtTitle;
    private ImageView iconAdd;
    private WebView webView;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private WeeklyMessageDTO weeklyMessage;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private FloatingActionButton fab;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
    }

    private void setup() {
        webView = (WebView) findViewById(R.id.webView);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        iconAdd = (ImageView) findViewById(R.id.iconAdd);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void confirm() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to add this link?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addLink();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }
    private void addLink() {
        Log.d(TAG, "addLink: ............".concat(webView.getTitle()));
    }
}
