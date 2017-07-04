package com.oneconnect.leadership.admin;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.oneconnect.leadership.library.App;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class AdminApp extends App {
    public static final String TAG = AdminApp.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## ADMIN APP started\n");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }


}
