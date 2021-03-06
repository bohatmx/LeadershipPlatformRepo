package com.oneconnect.leadership.library;

/**
 * Created by aubreymalabie on 7/13/16.
 */

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;
import com.google.firebase.FirebaseApp;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

/**
 * Created by aubreymalabie on 7/11/16.
 */

public class App extends Application {
    static final String TAG = App.class.getSimpleName();
    protected String userAgent;

    @Override
    public void onCreate() {
        super.onCreate();
        StringBuilder sb = new StringBuilder();
        sb.append("####################################################\n");
        sb.append("########## LEADERSHIP App Starting ......   ##########\n");
        sb.append("####################################################\n");
        Log.d(TAG, "onCreate: \n" + sb.toString());

        FirebaseApp.initializeApp(this);
        Log.w(TAG, "onCreate: FirebaseApp initializeApp complete" );

        userAgent = Util.getUserAgent(this, "Leadership");
    }

    private static DB snappyDB;
    static Kryo kryo;
    public  static DB getSnappyDB(Context ctx) {
        kryo = new Kryo();
        try {

            kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
            if (snappyDB == null || !snappyDB.isOpen()) {
                snappyDB = DBFactory.open(ctx, kryo);
            }
        } catch (SnappydbException e) {
            Log.e(TAG, "getSnappyDB: ", e );
        }

        return snappyDB;
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

}

