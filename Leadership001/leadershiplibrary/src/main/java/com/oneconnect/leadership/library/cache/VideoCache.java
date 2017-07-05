package com.oneconnect.leadership.library.cache;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.oneconnect.leadership.library.App;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.snappydb.DB;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 12/15/16.
 */

public class VideoCache {

    static DB snappydb;
    static final Gson gson = new Gson();
    public static final String VIDEO_KEY = "video";
    public static final String TAG = VideoCache.class.getSimpleName();

    static void setSnappyDB(Context ctx) {
        snappydb = App.getSnappyDB(ctx);
    }

    public interface WriteListener {
        void onDataWritten();

        void onError(String message);
    }

    public interface ReadListener {
        void onDataRead(List<VideoDTO> videos);

        void onError(String message);
    }

    public static void addVideo(VideoDTO video, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(video, listener);
        task.execute();
    }

    public static void getVideos(Context ctx,ReadListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(listener);
        task.execute();
    }

    public static void deleteVideo(String videoKey, Context ctx, WriteListener listener) {
        setSnappyDB(ctx);
        MTask task = new MTask(videoKey, listener);
        task.execute();
    }

    static class MTask extends AsyncTask<Void, Void, Integer> {

        List<VideoDTO> videos = new ArrayList<>();
        VideoDTO video;
        WriteListener writeListener;
        ReadListener readListener;
        String videoKey;
        int type;
        static final int ADD_VIDEO = 1, GET_VIDEOS = 2, DELETE_VIDEO = 3;

        public MTask(VideoDTO video, WriteListener listener) {
            this.video = video;
            type = ADD_VIDEO;
            writeListener = listener;
        }

        public MTask(ReadListener listener) {
            type = GET_VIDEOS;
            readListener = listener;
        }

        public MTask(String videoKey, WriteListener listener) {
            this.videoKey = videoKey;
            type = DELETE_VIDEO;
            writeListener = listener;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                switch (type) {
                    case ADD_VIDEO:
                        video.setVideoID(VIDEO_KEY + System.currentTimeMillis());
                        String json1 = gson.toJson(video);
                        snappydb.put(video.getVideoID(), json1);
                        Log.i(TAG, "doInBackground: video cached");
                        break;
                    case GET_VIDEOS:
                        String[] keys = snappydb.findKeys(VIDEO_KEY);
                        videos = new ArrayList<>();
                        for (String key : keys) {
                            String json2 = snappydb.get(key);
                            videos.add(gson.fromJson(json2, VideoDTO.class));
                        }
                        Log.i(TAG, "doInBackground: found " + videos.size() + " videos");
                        break;
                    case DELETE_VIDEO:
                        try {
                            String json3 = snappydb.get(videoKey);
                            snappydb.del(videoKey);
                            Log.e(TAG, "doInBackground: video deleted from cache");
                        } catch (Exception e) {
                            Log.w(TAG, "doInBackground: no video found for delete, ignore" );
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
                case ADD_VIDEO:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case DELETE_VIDEO:
                    if (writeListener != null)
                        writeListener.onDataWritten();
                    break;
                case GET_VIDEOS:
                    if (readListener != null)
                        readListener.onDataRead(videos);
                    break;
            }
        }
    }


}
