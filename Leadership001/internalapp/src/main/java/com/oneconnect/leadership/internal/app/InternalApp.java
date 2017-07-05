package com.oneconnect.leadership.internal.app;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class InternalApp extends Application {
     public static final String TAG = InternalApp.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## INTERNAL APP started");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());

        FirebaseApp.initializeApp(this);
        Log.w(TAG, "onCreate: FirebaseApp initializeApp complete" );
    }

}
