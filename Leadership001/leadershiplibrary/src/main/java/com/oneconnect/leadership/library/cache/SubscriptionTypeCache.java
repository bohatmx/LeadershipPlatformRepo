package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.SubscriptionTypeDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class SubscriptionTypeCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = SubscriptionTypeCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<SubscriptionTypeDTO> subscriptionTypes);

        void onError(String message);
    }

    public static void addSubscriptions(List<SubscriptionTypeDTO> subscriptionTypes, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(subscriptionTypes, listener);
        task.execute();
    }

    public static void getSubscriptions(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<SubscriptionTypeDTO> subscriptionTypes = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_SUBSCRIPTION_TYPES = 1, GET_SUBSCRIPTION_TYPES = 2;
        static final String SUBSCRIPTION_TYPE_KEY = "subscriptionType";

        public MTask(List<SubscriptionTypeDTO> subscriptionTypes, WriteListener listener) {
            this.subscriptionTypes = subscriptionTypes;
            type = ADD_SUBSCRIPTION_TYPES;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_SUBSCRIPTION_TYPES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_SUBSCRIPTION_TYPES:
                        for (SubscriptionTypeDTO u : subscriptionTypes) {
                            String json1 = gson.toJson(u);
                            snappydb.put(SUBSCRIPTION_TYPE_KEY + u.getSubscriptionTypeID(), json1);
                            Log.i(TAG, ".......doInBackground: subscriptionType cached: ");
                        }
                        break;
                    case GET_SUBSCRIPTION_TYPES:
                        String[] keys = snappydb.findKeys(SUBSCRIPTION_TYPE_KEY);
                        subscriptionTypes = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            subscriptionTypes.add(gson.fromJson(json2, SubscriptionTypeDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + subscriptionTypes.size() + " subscriptionTypes");
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
                case ADD_SUBSCRIPTION_TYPES:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_SUBSCRIPTION_TYPES:
                    if (readListener != null)
                        readListener.onDataRead(subscriptionTypes);
                    break;
            }
        }
    }
}
