package com.oneconnect.leadership.subscriber;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class SubscriberApp extends Application {
     public static final String TAG = SubscriberApp.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## SUBSCRIBER APP started");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());


        FirebaseApp.initializeApp(this);
        Log.w(TAG, "onCreate: FirebaseApp initializeApp complete" );
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
