package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class WeeklyMasterclassCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = WeeklyMasterclassCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<WeeklyMasterClassDTO> weeklyMasterclasses);

        void onError(String message);
    }

    public static void addWeeklyMasterclasses(List<WeeklyMasterClassDTO> weeklyMasterclasses, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(weeklyMasterclasses, listener);
        task.execute();
    }

    public static void getWeeklyMasterclasses(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<WeeklyMasterClassDTO> weeklyMasterclasses = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_WEEKLY_MASTERCLASSES = 1, GET_WEEKLY_MASTERCLASSES = 2;
        static final String WEEKLY_MASTERCLASS_KEY = "weeklyMasterclass";

        public MTask(List<WeeklyMasterClassDTO> weeklyMasterclasses, WriteListener listener) {
            this.weeklyMasterclasses = weeklyMasterclasses;
            type = ADD_WEEKLY_MASTERCLASSES;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_WEEKLY_MASTERCLASSES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_WEEKLY_MASTERCLASSES:
                        for (WeeklyMasterClassDTO u : weeklyMasterclasses) {
                            String json1 = gson.toJson(u);
                            snappydb.put(WEEKLY_MASTERCLASS_KEY + u.getWeeklyMasterClassID(), json1);
                            Log.i(TAG, ".......doInBackground: weeklyMasterclass cached: " + u.getTitle());
                        }
                        break;
                    case GET_WEEKLY_MASTERCLASSES:
                        String[] keys = snappydb.findKeys(WEEKLY_MASTERCLASS_KEY);
                        weeklyMasterclasses = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            weeklyMasterclasses.add(gson.fromJson(json2, WeeklyMasterClassDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + weeklyMasterclasses.size() + " weeklyMasterclasses");
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
                case ADD_WEEKLY_MASTERCLASSES:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_WEEKLY_MASTERCLASSES:
                    if (readListener != null)
                        readListener.onDataRead(weeklyMasterclasses);
                    break;
            }
        }
    }
}
