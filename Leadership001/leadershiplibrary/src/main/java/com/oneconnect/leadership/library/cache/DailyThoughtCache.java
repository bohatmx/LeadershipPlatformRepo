package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class DailyThoughtCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = DailyThoughtCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<DailyThoughtDTO> dailyThoughts);

        void onError(String message);
    }

    public static void addDailyThoughts(List<DailyThoughtDTO> dailyThoughts, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(dailyThoughts, listener);
        task.execute();
    }

    public static void getDailyThoughts(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<DailyThoughtDTO> dailyThoughts = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_DAILY_THOUGHTS = 1, GET_DAILY_THOUGHTS = 2;
        static final String DAILY_THOUGHT_KEY = "dailyThought";

        public MTask(List<DailyThoughtDTO> dailyThoughts, WriteListener listener) {
            this.dailyThoughts = dailyThoughts;
            type = ADD_DAILY_THOUGHTS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_DAILY_THOUGHTS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_DAILY_THOUGHTS:
                        for (DailyThoughtDTO u : dailyThoughts) {
                            String json1 = gson.toJson(u);
                            snappydb.put(DAILY_THOUGHT_KEY + u.getDailyThoughtID(), json1);
                            Log.i(TAG, ".......doInBackground: dailyThought cached: " + u.getCategoryName());
                        }
                        break;
                    case GET_DAILY_THOUGHTS:
                        String[] keys = snappydb.findKeys(DAILY_THOUGHT_KEY);
                        dailyThoughts = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            dailyThoughts.add(gson.fromJson(json2, DailyThoughtDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + dailyThoughts.size() + " dailyThoughts");
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
                case ADD_DAILY_THOUGHTS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_DAILY_THOUGHTS:
                    if (readListener != null)
                        readListener.onDataRead(dailyThoughts);
                    break;
            }
        }
    }
}
