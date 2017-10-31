package com.oneconnect.leadership.library.activities;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.FirebaseApp;
import com.oneconnect.leadership.library.BuildConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Nkululeko on 2017/09/05.
 */

public class CompanyApp extends Application {

    protected String userAgent;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        StringBuilder sb = new StringBuilder();
        sb.append("#############################################\n");
        sb.append("######## COMPANY APP started");
        sb.append("#############################################\n");
        Log.d(TAG, sb.toString());
        //  Twitter.initialize(this);

        FirebaseApp.initializeApp(this);
        Log.w(TAG, "onCreate: FirebaseApp initializeApp complete" );

        userAgent = Util.getUserAgent(this, "Leadership");
    }

    public DataSource.Factory buildDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        Log.d(TAG, "buildDataSourceFactory: ".concat(bandwidthMeter.toString()));
        return new DefaultDataSourceFactory(this, bandwidthMeter,
                buildHttpDataSourceFactory(bandwidthMeter));
    }

    public HttpDataSource.Factory buildHttpDataSourceFactory(DefaultBandwidthMeter bandwidthMeter) {
        Log.d(TAG, "buildHttpDataSourceFactory: ".concat(bandwidthMeter.toString()));
        return new DefaultHttpDataSourceFactory(userAgent, bandwidthMeter);
    }

    public boolean useExtensionRenderers() {
        return BuildConfig.FLAVOR.equals("withExtensions");
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    static final String TAG = CompanyApp.class.getSimpleName();
}
