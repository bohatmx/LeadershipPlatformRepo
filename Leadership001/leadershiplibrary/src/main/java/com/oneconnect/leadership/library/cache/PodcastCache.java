package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class PodcastCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = PodcastCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<PodcastDTO> podcasts);

        void onError(String message);
    }

    public static void addPodcasts(List<PodcastDTO> podcasts, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(podcasts, listener);
        task.execute();
    }

    public static void getPodcasts(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<PodcastDTO> podcasts = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_PODCASTS = 1, GET_PODCASTS = 2;
        static final String PODCAST_KEY = "podcast";

        public MTask(List<PodcastDTO> podcasts, WriteListener listener) {
            this.podcasts = podcasts;
            type = ADD_PODCASTS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_PODCASTS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_PODCASTS:
                        for (PodcastDTO u : podcasts) {
                            String json1 = gson.toJson(u);
                            snappydb.put(PODCAST_KEY + u.getPodcastID(), json1);
                            Log.i(TAG, ".......doInBackground: podcast cached: " + u.getTitle());
                        }
                        break;
                    case GET_PODCASTS:
                        String[] keys = snappydb.findKeys(PODCAST_KEY);
                        podcasts = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            podcasts.add(gson.fromJson(json2, PodcastDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + podcasts.size() + " podcasts");
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
                case ADD_PODCASTS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_PODCASTS:
                    if (readListener != null)
                        readListener.onDataRead(podcasts);
                    break;
            }
        }
    }
}
