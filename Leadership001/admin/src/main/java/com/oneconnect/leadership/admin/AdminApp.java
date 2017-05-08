package com.oneconnect.leadership.admin;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.oneconnect.leadership.library.App;

/**
 * Created by aubreymalabie on 3/16/17.
 */

public class AdminApp extends App {
    public static final String TAG = AdminApp.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();

        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## ADMIN APP started\n");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());

    }


}
