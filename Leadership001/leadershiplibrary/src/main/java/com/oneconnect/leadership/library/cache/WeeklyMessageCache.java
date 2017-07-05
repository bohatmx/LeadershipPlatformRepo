package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class WeeklyMessageCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = WeeklyMessageCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<WeeklyMessageDTO> weeklyMessages);

        void onError(String message);
    }

    public static void addWeeklyMessages(List<WeeklyMessageDTO> weeklyMessages, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(weeklyMessages, listener);
        task.execute();
    }

    public static void getWeeklyMessages(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<WeeklyMessageDTO> weeklyMessages = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_WEEKLY_MESSAGES = 1, GET_WEEKLY_MESSAGES = 2;
        static final String WEEKLY_MESSAGE_KEY = "weeklyMessage";

        public MTask(List<WeeklyMessageDTO> weeklyMessages, WriteListener listener) {
            this.weeklyMessages = weeklyMessages;
            type = ADD_WEEKLY_MESSAGES;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_WEEKLY_MESSAGES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_WEEKLY_MESSAGES:
                        for (WeeklyMessageDTO u : weeklyMessages) {
                            String json1 = gson.toJson(u);
                            snappydb.put(WEEKLY_MESSAGE_KEY + u.getWeeklyMessageID(), json1);
                            Log.i(TAG, ".......doInBackground: weeklyMessage cached: " + u.getTitle());
                        }
                        break;
                    case GET_WEEKLY_MESSAGES:
                        String[] keys = snappydb.findKeys(WEEKLY_MESSAGE_KEY);
                        weeklyMessages = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            weeklyMessages.add(gson.fromJson(json2, WeeklyMessageDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + weeklyMessages.size() + " weeklyMessages");
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
                case ADD_WEEKLY_MESSAGES:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_WEEKLY_MESSAGES:
                    if (readListener != null)
                        readListener.onDataRead(weeklyMessages);
                    break;
            }
        }
    }
}
