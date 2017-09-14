package com.oneconnect.leadership.library.activities;

import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDexApplication;
import android.util.Log;


import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;


import java.io.File;


/**
 * Created by aubreyM on 15/02/21.
 */

public class LeadershipApplication extends MultiDexApplication {
    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
    }

    public static Picasso picasso;
    static final long MAX_CACHE_SIZE = 1024 * 1024 * 1024; // 1 GB cache on device
    public static final String PROPERTY_ID = "UA-53661372-3";



    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n#######################################\n");
        sb.append("#######################################\n");
        sb.append("###\n");
        sb.append("###  Leadership Platform App has started\n");
        sb.append("###\n");
        sb.append("#######################################\n\n");

        Log.d(LOG, sb.toString());

       // Firebase.setAndroidContext(getApplicationContext());
        boolean isDebuggable = 0 != (getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE);
        // create Picasso.Builder object
        File picassoCacheDir = getCacheDir();
        Log.w(LOG, "####### images in picasso cache: " + picassoCacheDir.listFiles().length);
        Picasso.Builder picassoBuilder = new Picasso.Builder(getApplicationContext());
        picassoBuilder.downloader(new OkHttpDownloader(picassoCacheDir, MAX_CACHE_SIZE));
        picasso = picassoBuilder.build();
        try {
            Picasso.setSingletonInstance(picasso);
        } catch (IllegalStateException ignored) {
            // Picasso instance was already set
            // cannot set it after Picasso.with(Context) was already in use
        }
        if (isDebuggable) {
            Picasso.with(getApplicationContext())
                    .setIndicatorsEnabled(true);
            Picasso.with(getApplicationContext())
                    .setLoggingEnabled(true);
        }


    }


    public final static int THEME_BLUE = 20;
    public final static int THEME_INDIGO = 1;
    public final static int THEME_RED = 2,
            THEME_TEAL = 3,
            THEME_BLUE_GRAY = 4,
            THEME_ORANGE = 5,
            THEME_PINK = 6,
            THEME_CYAN = 7,
            THEME_GREEN = 8,
            THEME_LIGHT_GREEN = 9,
            THEME_LIME = 10,
            THEME_AMBER = 11,
            THEME_GREY = 12,
            THEME_BROWN = 14,
            THEME_PURPLE = 15;


    static final String LOG = LeadershipApplication.class.getSimpleName();
}
