package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class PriceCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = PriceCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<PriceDTO> prices);

        void onError(String message);
    }

    public static void addPrices(List<PriceDTO> prices, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(prices, listener);
        task.execute();
    }

    public static void getPrices(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<PriceDTO> prices = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_PRICES = 1, GET_PRICES = 2;
        static final String PRICE_KEY = "price";

        public MTask(List<PriceDTO> prices, WriteListener listener) {
            this.prices = prices;
            type = ADD_PRICES;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_PRICES;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_PRICES:
                        for (PriceDTO u : prices) {
                            String json1 = gson.toJson(u);
                            snappydb.put(PRICE_KEY + u.getPriceID(), json1);
                            Log.i(TAG, ".......doInBackground: price cached: " + u.getAmount());
                        }
                        break;
                    case GET_PRICES:
                        String[] keys = snappydb.findKeys(PRICE_KEY);
                        prices = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            prices.add(gson.fromJson(json2, PriceDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + prices.size() + " prices");
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
                case ADD_PRICES:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_PRICES:
                    if (readListener != null)
                        readListener.onDataRead(prices);
                    break;
            }
        }
    }
}
