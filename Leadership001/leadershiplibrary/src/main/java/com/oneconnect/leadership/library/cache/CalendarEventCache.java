package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nkululeko on 2017/05/07.
 */

public class CalendarEventCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = CalendarEventCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<CalendarEventDTO> calendarEvents);

        void onError(String message);
    }

    public static void addCalendarEvents(List<CalendarEventDTO> calendarEvents, Context ctx, CalendarEventCache.WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(calendarEvents, listener);
        task.execute();
    }

    public static void getCalendarEvents(Context ctx, CalendarEventCache.ReadListener listener) {
        setSnappyDB(ctx);
        CalendarEventCache.MTask task = new CalendarEventCache.MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<CalendarEventDTO> calendarEvents = new ArrayList<>();
        CalendarEventCache.WriteListener writeListener;
        CalendarEventCache.ReadListener readListener;
        int type;
        static final int ADD_CALENDAR_EVENTS = 1, GET_CALENDAR_EVENTS = 2;
        static final String CALENDAR_EVENT_KEY = "calendarEvent";

        public MTask(List<CalendarEventDTO> calendarEvents, CalendarEventCache.WriteListener listener) {
            this.calendarEvents = calendarEvents;
            type = ADD_CALENDAR_EVENTS;
            writeListener = listener;
        }

        public MTask(CalendarEventCache.ReadListener listener) {
            type = GET_CALENDAR_EVENTS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_CALENDAR_EVENTS:
                        for (CalendarEventDTO u : calendarEvents) {
                            String json1 = gson.toJson(u);
                            snappydb.put(CALENDAR_EVENT_KEY + u.getCalendarEventID(), json1);
                            Log.i(TAG, ".......doInBackground: calendarEvent cached: " + u.getTitle());
                        }
                        break;
                    case GET_CALENDAR_EVENTS:
                        String[] keys = snappydb.findKeys(CALENDAR_EVENT_KEY);
                        calendarEvents = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            calendarEvents.add(gson.fromJson(json2, CalendarEventDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + calendarEvents.size() + " calendarEvents");
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
                case ADD_CALENDAR_EVENTS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_CALENDAR_EVENTS:
                    if (readListener != null)
                        readListener.onDataRead(calendarEvents);
                    break;
            }
        }
    }
}
