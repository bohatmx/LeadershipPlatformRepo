package com.oneconnect.leadership.library.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.cache.VideoCache;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class VideoUploaderService extends IntentService {

    public VideoUploaderService() {
        super("VideoUploaderService");
    }

    public static final String TAG = VideoUploaderService.class.getSimpleName();
    DataAPI dataAPI;
    FirebaseStorageAPI storageAPI;
    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<VideoDTO> videos;
    int index = 0, success = 0, failed = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent:  ####  starting video upload if videos in cache");
        try {
            dataAPI = new DataAPI();
            storageAPI = new FirebaseStorageAPI();
            VideoCache.getVideos(getApplicationContext(), new VideoCache.ReadListener() {
                @Override
                public void onDataRead(List<VideoDTO> list) {
                    Log.d(TAG, "onDataRead: videos found: " + list.size());
                    if (list.isEmpty()) {
                        Log.w(TAG, "onDataRead: no need to upload videos. empty cache found");
                        return;
                    }
                    videos = list;
                    index = 0;
                    control();
                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (Exception ex) {
            FirebaseCrash.report(new Exception("Failed to upload videos: " + ex.getMessage()));
        }

    }

    private void control() {
        if (index < videos.size()) {
            uploadVideo(videos.get(index));
        } else {
            broadcast();
        }
    }

    private void uploadVideo(final VideoDTO video) {
        Log.d(TAG, "################## uploadVideo: ........starting to upload: "
                .concat(gson.toJson(video)));
        final String videoKey = video.getVideoID();
        try {
            storageAPI.uploadVideo(video, new FirebaseStorageAPI.StorageListener() {
                @Override
                public void onResponse(String key) {
                    deleteCachedVideo(videoKey, new DeleteListener() {
                        @Override
                        public void onDeleteComplete() {
                            Log.i(TAG, "onDeleteComplete: we cool, video entry cleared from cache");
                            success++;
                            setAnalyticsEvent(video);
                            if (videos != null) {
                                index++;
                                control();
                            } else {
                                broadcast();
                            }
                        }
                    });
                }

                @Override
                public void onError(String message) {
                    FirebaseCrash.report(new Exception("Video file upload failed: "
                            .concat(video.getTitle())));
                    Log.e(TAG, "uploadVideo: Failed, but continuing ...".concat(message));
                    index++;
                    failed++;
                    control();

                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadVideo: Failed, but continuing ...", e);
            failed++;
            index++;
            control();
        }
    }

    private interface DeleteListener {
        void onDeleteComplete();
    }

    private void deleteCachedVideo(String key, final DeleteListener listener) {
        VideoCache.deleteVideo(key, getApplicationContext(), new VideoCache.WriteListener() {
            @Override
            public void onDataWritten() {
                Log.d(TAG, "onDataWritten: video removed from cache");
                listener.onDeleteComplete();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public static final String BROADCAST_VIDEO_UPLOADED = "COM.ONECONNECT.BROADCAST_video_uploaded";


    private void broadcast() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(
                getApplicationContext());
        Intent m = new Intent(BROADCAST_VIDEO_UPLOADED);
        m.putExtra("videos", success);
        m.putExtra("failed",failed);
        broadcastManager.sendBroadcast(m);
        Log.d(TAG, "BROADCAST_VIDEO_UPLOADED: ##################" + videos +
                " videos uploaded - broadcast sent");

    }

    private void setAnalyticsEvent(VideoDTO video) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics
                .getInstance(getApplicationContext());
        try {
            Bundle bundle = new Bundle();
            bundle.putString("eventType", "videoTaken");
            bundle.putString("video", video.toString());
            mFirebaseAnalytics.logEvent("Videos", bundle);
            Log.w(TAG, "analytics video event sent .....");
        } catch (Exception e) {
            Log.e(TAG, "setAnalytics: ", e);
        }


    }


}
