package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.snappydb.DB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class PhotoCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String PHOTO_KEY = "photo";
    public static final String TAG = PhotoCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<PhotoDTO> photos);

        void onError(String message);
    }

    public static void addPhoto(PhotoDTO photo, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(photo, listener);
        task.execute();
    }

    public static void getPhotos(Context ctx,ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }

    public static void deletePhoto(String photoKey, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(photoKey, listener);
        task.execute();
    }

    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<PhotoDTO> photos = new ArrayList<>();
        PhotoDTO photo;
        WriteListener writeListener;
        ReadListener readListener;
        String photoKey;
        int type;
        static final int ADD_PHOTO = 1, GET_PHOTOS = 2, DELETE_PHOTO = 3;

        public MTask(PhotoDTO photo, WriteListener listener) {
            this.photo = photo;
            type = ADD_PHOTO;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_PHOTOS;
            readListener = listener;
        }

        public MTask(String photoKey, WriteListener listener) {
            this.photoKey = photoKey;
            type = DELETE_PHOTO;
            writeListener = listener;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_PHOTO:
                        photo.setPhotoID(PHOTO_KEY + System.currentTimeMillis());
                        String json1 = gson.toJson(photo);
                        snappydb.put(photo.getPhotoID(), json1);
                        Log.i(TAG, "doInBackground: photo cached");
                        break;
                    case GET_PHOTOS:
                        String[] keys = snappydb.findKeys(PHOTO_KEY);
                        photos = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            photos.add(gson.fromJson(json2, PhotoDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + photos.size() + " photos");
                        break;
                    case DELETE_PHOTO:
                        try {
                            String json3 = snappydb.get(photoKey);
                            snappydb.del(photoKey);
                            Log.e(TAG, "doInBackground: photo deleted from cache");
                        } catch (Exception e) {
                            Log.w(TAG, "doInBackground: no photo found for delete, ignore" );
                        }
                        break;

                }
            } catch (Exception e) {
                Log.e(TAG, "snappydb problem: ", e);
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
                case ADD_PHOTO:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case DELETE_PHOTO:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case GET_PHOTOS:
                    if (readListener != null)
                        readListener.onDataRead(photos);
                    break;
            }
        }
    }


}
