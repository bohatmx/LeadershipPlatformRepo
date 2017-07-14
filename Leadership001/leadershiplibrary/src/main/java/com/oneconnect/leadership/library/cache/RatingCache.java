package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kurisani on 2017/07/14.
 */

public class RatingCache {
    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = RatingCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<RatingDTO> rating);

        void onError(String message);
    }

    public static void addRatings(List<RatingDTO> rating, Context ctx, RatingCache.WriteListener listener) {
        setSnappyDB(ctx);
        RatingCache.MTask task = new RatingCache.MTask(rating, listener);
        task.execute();
    }

    public static void getRatings(Context ctx, RatingCache.ReadListener listener) {
        setSnappyDB(ctx);
        RatingCache.MTask task = new RatingCache.MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<RatingDTO> rating = new ArrayList<>();
        RatingCache.WriteListener writeListener;
        RatingCache.ReadListener readListener;
        int type;
        static final int ADD_RATINGS = 1, GET_RATINGS = 2;
        static final String RATING_KEY = "rating";

        public MTask(List<RatingDTO> rating, RatingCache.WriteListener listener) {
            this.rating = rating;
            type = ADD_RATINGS;
            writeListener = listener;
        }

        public MTask(RatingCache.ReadListener listener) {
            type = GET_RATINGS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_RATINGS:
                        for (RatingDTO u : rating) {
                            String json1 = gson.toJson(u);
                            snappydb.put(RATING_KEY + u.getRatingID(), json1);
                            Log.i(TAG, ".......doInBackground: rating cached: " + u.getRatingID());
                        }
                        break;
                    case GET_RATINGS:
                        String[] keys = snappydb.findKeys(RATING_KEY);
                        rating = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            rating.add(gson.fromJson(json2, RatingDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + rating.size() + " rating");
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
                case ADD_RATINGS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_RATINGS:
                    if (readListener != null)
                        readListener.onDataRead(rating);
                    break;
            }
        }
    }
}
