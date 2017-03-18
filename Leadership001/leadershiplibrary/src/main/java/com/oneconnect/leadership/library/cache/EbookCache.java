package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class EbookCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = EbookCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<EBookDTO> ebooks);

        void onError(String message);
    }

    public static void addEbooks(List<EBookDTO> ebooks, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(ebooks, listener);
        task.execute();
    }

    public static void getEbooks(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<EBookDTO> ebooks = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_EBOOKS = 1, GET_EBOOKS = 2;
        static final String EBOOK_KEY = "eBook";

        public MTask(List<EBookDTO> ebooks, WriteListener listener) {
            this.ebooks = ebooks;
            type = ADD_EBOOKS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_EBOOKS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_EBOOKS:
                        for (EBookDTO u : ebooks) {
                            String json1 = gson.toJson(u);
                            snappydb.put(EBOOK_KEY + u.geteBookID(), json1);
                            Log.i(TAG, ".......doInBackground: eBook cached: " + u.getTitle());
                        }
                        break;
                    case GET_EBOOKS:
                        String[] keys = snappydb.findKeys(EBOOK_KEY);
                        ebooks = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            ebooks.add(gson.fromJson(json2, EBookDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + ebooks.size() + " ebooks");
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
                case ADD_EBOOKS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_EBOOKS:
                    if (readListener != null)
                        readListener.onDataRead(ebooks);
                    break;
            }
        }
    }
}
