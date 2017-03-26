package com.oneconnect.leadership.library.api;

import android.os.AsyncTask;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aubreymalabie on 2/11/17.
 */

public class CloudinaryAPI {

    public static final String
            API_KEY = "626742792878829",
            CLOUD_NAME = "oneconnect-technologies",
            API_SECRET = "OS1lw143mGPbaVtV30GslVxwImw";

    public interface CloudinaryListener {
        void onFileUploaded(PhotoDTO photo);

        void onVideoUploaded(VideoDTO video);

        void onError(String message);
    }

    static final String TAG = CloudinaryAPI.class.getSimpleName();

    /**
     * Upload photo to CDN (Cloudinary at this time). On return of the CDN response, a call is made
     * to the backend to add the metadata of the photo to the backend database
     *
     * @param photo
     * @param listener
     * @see PhotoDTO
     */
    public static void uploadPhoto(final PhotoDTO photo,
                                   CloudinaryListener listener) {
        Log.d(TAG, "##### starting CDNUploader uploadPhoto: " + photo.getFilePath());
        new PhotoTask(photo, listener).execute();
    }

    /**
     * Upload video clip to Cloudinary CDN
     *
     * @param video
     * @param listener
     */
    public static void uploadVideo(final VideoDTO video,
                                   CloudinaryListener listener) {
        Log.d(TAG, "##### starting CloudinaryAPI uploadVideo: ".concat(video.getFilePath()));
        new PhotoTask(video, listener).execute();
    }


    static class PhotoTask extends AsyncTask<Void, Void, Integer> {

        PhotoDTO photo;
        VideoDTO video;
        CloudinaryListener listener;
        long start, end;

        public PhotoTask(PhotoDTO photo, CloudinaryListener listener) {
            this.photo = photo;
            this.video = null;
            this.listener = listener;
        }

        public PhotoTask(VideoDTO video, CloudinaryListener listener) {
            this.video = video;
            this.photo = null;
            this.listener = listener;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: #######################################");
            start = System.currentTimeMillis();
            Map config = new HashMap();
            config.put("cloud_name", CLOUD_NAME);
            config.put("api_key", API_KEY);
            config.put("api_secret", API_SECRET);
            config.put("resource_type", "video");
            config.put("chunk_size", 6000000);

            Cloudinary cloudinary = new Cloudinary(config);

            if (photo != null) {
                File file = new File(photo.getFilePath());
                Log.w(TAG, "PhotoTask doInBackground: ###### size of photo file: " + file.length());
                Map map;
                try {
                    map = cloudinary.uploader().upload(file, config);
                    end = System.currentTimeMillis();
                    Log.i(TAG, "----> photo uploaded: " + map.get("url") + " elapsed: "
                            + getElapsed(start, end) + " seconds");

                    photo.setUrl((String) map.get("secure_url"));
                    photo.setHeight((int) map.get("height"));
                    photo.setWidth((int) map.get("width"));
                    photo.setImageSize((Long) map.get("bytes"));
                    photo.setDate(new Date().getTime());
                    return 0;

                } catch (Exception e) {
                    Log.e(TAG, "CDN uploadToYouTube Failed", e);
                    return 9;
                }
            }
            if (video != null) {
                try {
                    File file = new File(video.getFilePath());
                    Log.w(TAG, "PhotoTask doInBackground: $$$$$$$$$ size of video file: " + file.length());
                    //File size too large. Got 190752142. Maximum is 41943040.
                    if (file.length() > 41943040) {
                        Log.e(TAG, "doInBackground: REJECTED - file too large " + file.length() );
                        return 9;
                    }
                    Map map = cloudinary.uploader().uploadLarge(file, config);
                    end = System.currentTimeMillis();
                    Log.i(TAG, "----> video uploaded: " + map.get("url") + " elapsed: "
                            + getElapsed(start, end) + " seconds");

                    video.setUrl((String) map.get("secure_url"));
                    video.setDate(new Date().getTime());
                    //*********
                    for (Object key : map.keySet()) {
                        String mKey = (String) key;
                        Log.d(TAG, "cloudinary map: mKey: ".concat(mKey)
                                .concat((String) map.get(key)));
                    }
                    return 0;
                } catch (Exception e) {
                    Log.e(TAG, "CDN uploadToYouTube Failed", e);
                    return 9;
                }
            }

            Log.e(TAG, "doInBackground: ERROR!! - neither photo or video uploaded. Fuck it!");
            return 9;
        }

        @Override
        protected void onPostExecute(final Integer result) {
            Log.e(TAG, "+++++++ PhotoTask onPostExecute: result = " + result);
            if (result > 0) {
                listener.onError("Error uploading image/video to CDN");
                return;
            }
            if (video != null) {
                listener.onVideoUploaded(video);
            }
            if (photo != null) {
                listener.onFileUploaded(photo);
            }
        }
    }

    private static double getElapsed(long start, long end) {
        Double d = new Double("" + start);
        Double e = new Double("" + end);
        Double f = (e - d) / 1000;

        return f;

    }
}


