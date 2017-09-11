package com.ocg.leadership.company;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nkululeko on 2017/09/05.
 */

public class CompanyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## SUBSCRIBER APP started");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());
        //  Twitter.initialize(this);

        FirebaseApp.initializeApp(this);
        Log.w(TAG, "onCreate: FirebaseApp initializeApp complete" );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    static final String TAG = CompanyApp.class.getSimpleName();
}
