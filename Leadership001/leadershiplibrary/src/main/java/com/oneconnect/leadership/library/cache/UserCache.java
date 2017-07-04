package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.UserDTO;
import com.snappydb.DB;
import com.snappydb.SnappydbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class UserCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String TAG = UserCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<UserDTO> users);

        void onError(String message);
    }

    public static void addUsers(List<UserDTO> users, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(users, listener);
        task.execute();
    }

    public static void getUsers(Context ctx, ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }


    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<UserDTO> users = new ArrayList<>();
        WriteListener writeListener;
        ReadListener readListener;
        int type;
        static final int ADD_USERS = 1, GET_USERS = 2;
        static final String USER_KEY = "user";

        public MTask(List<UserDTO> users, WriteListener listener) {
            this.users = users;
            type = ADD_USERS;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_USERS;
            readListener = listener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_USERS:
                        for (UserDTO u : users) {
                            String json1 = gson.toJson(u);
                            snappydb.put(USER_KEY + u.getUserID(), json1);
                            Log.i(TAG, ".......doInBackground: user cached: " + u.getFullName());
                        }
                        break;
                    case GET_USERS:
                        String[] keys = snappydb.findKeys(USER_KEY);
                        users = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            users.add(gson.fromJson(json2, UserDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + users.size() + " users");
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
                case ADD_USERS:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;

                case GET_USERS:
                    if (readListener != null)
                        readListener.onDataRead(users);
                    break;
            }
        }
    }
}
