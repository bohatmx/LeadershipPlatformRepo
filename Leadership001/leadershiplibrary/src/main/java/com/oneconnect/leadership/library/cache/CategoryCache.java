package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class CategoryCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = CategoryCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<CategoryDTO> categories);

        void onError(String message);
    }

    public static void addCategories(List<CategoryDTO> categories, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(categories, listener);
        task.execute();
    }

    public static void getCategories(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<CategoryDTO> categories = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_CATEGORIES = 1, GET_CATEGORIES = 2;
        static final String CATEGORY_KEY = "category";

        public MTask(List<CategoryDTO> categories, WriteListener listener) {
            this.categories = categories;
            type = ADD_CATEGORIES;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_CATEGORIES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_CATEGORIES:
                        for (CategoryDTO u : categories) {
                            String json1 = gson.toJson(u);
                            snappydb.put(CATEGORY_KEY + u.getCategoryID(), json1);
                            Log.i(TAG, ".......doInBackground: category cached: " + u.getCategoryName());
                        }
                        break;
                    case GET_CATEGORIES:
                        String[] keys = snappydb.findKeys(CATEGORY_KEY);
                        categories = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            categories.add(gson.fromJson(json2, CategoryDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + categories.size() + " categories");
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
                case ADD_CATEGORIES:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_CATEGORIES:
                    if (readListener != null)
                        readListener.onDataRead(categories);
                    break;
            }
        }
    }
}
