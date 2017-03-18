package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class SubscriptionCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = SubscriptionCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<SubscriptionDTO> subscriptions);

        void onError(String message);
    }

    public static void addSubscriptions(List<SubscriptionDTO> subscriptions, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(subscriptions, listener);
        task.execute();
    }

    public static void getSubscriptions(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<SubscriptionDTO> subscriptions = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_SUBSCRIPTIONS = 1, GET_SUBSCRIPTIONS = 2;
        static final String SUBSCRIPTION_KEY = "subscription";

        public MTask(List<SubscriptionDTO> subscriptions, WriteListener listener) {
            this.subscriptions = subscriptions;
            type = ADD_SUBSCRIPTIONS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_SUBSCRIPTIONS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_SUBSCRIPTIONS:
                        for (SubscriptionDTO u : subscriptions) {
                            String json1 = gson.toJson(u);
                            snappydb.put(SUBSCRIPTION_KEY + u.getSubscriptionID(), json1);
                            Log.i(TAG, ".......doInBackground: subscription cached: " + u.getDescription());
                        }
                        break;
                    case GET_SUBSCRIPTIONS:
                        String[] keys = snappydb.findKeys(SUBSCRIPTION_KEY);
                        subscriptions = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            subscriptions.add(gson.fromJson(json2, SubscriptionDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + subscriptions.size() + " subscriptions");
                        break;


                }
            } catch (SnappydbException e) {
                Log.e(TAG, "doInBackground: ", e);
                return 9;
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result > 0) {
                if (writeListener != null) {
                    writeListener.onError("Unable to process data cache");
                    return;
                }
                if (readListener != null) {
                    readListener.onError("Unable to read data cache");
                    return;
                }
                return;
            }
            switch (type) {
                case ADD_SUBSCRIPTIONS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_SUBSCRIPTIONS:
                    if (readListener != null)
                        readListener.onDataRead(subscriptions);
                    break;
            }
        }
    }
}
