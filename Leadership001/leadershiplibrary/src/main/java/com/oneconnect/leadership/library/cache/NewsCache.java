package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class NewsCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = NewsCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<NewsDTO> news);

        void onError(String message);
    }

    public static void addNews(List<NewsDTO> news, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(news, listener);
        task.execute();
    }

    public static void getNews(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<NewsDTO> news = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_NEWS = 1, GET_NEWS = 2;
        static final String NEWS_KEY = "news";

        public MTask(List<NewsDTO> news, WriteListener listener) {
            this.news = news;
            type = ADD_NEWS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_NEWS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_NEWS:
                        for (NewsDTO u : news) {
                            String json1 = gson.toJson(u);
                            snappydb.put(NEWS_KEY + u.getNewsID(), json1);
                            Log.i(TAG, ".......doInBackground: news cached: " + u.getTitle());
                        }
                        break;
                    case GET_NEWS:
                        String[] keys = snappydb.findKeys(NEWS_KEY);
                        news = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            news.add(gson.fromJson(json2, NewsDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + news.size() + " news");
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
                case ADD_NEWS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_NEWS:
                    if (readListener != null)
                        readListener.onDataRead(news);
                    break;
            }
        }
    }
}
