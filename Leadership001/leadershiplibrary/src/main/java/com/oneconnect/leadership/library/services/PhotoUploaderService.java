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
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.cache.PhotoCache;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.VideoDTO;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PhotoUploaderService extends IntentService {

    public PhotoUploaderService() {
        super("PhotoUploaderService");
    }

    public static final String TAG = PhotoUploaderService.class.getSimpleName();
    FirebaseStorageAPI storageAPI;
    static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    long start, end;
    List<PhotoDTO> photos;
    List<VideoDTO> videos;
    int index = 0, success = 0, failed = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: %%%%%%%%%%%%%%%%%%  starting photo upload if photos in cache");
        storageAPI = new FirebaseStorageAPI();
        try {
            PhotoCache.getPhotos(getApplicationContext(), new PhotoCache.ReadListener() {
                @Override
                public void onDataRead(List<PhotoDTO> list) {
                    Log.d(TAG, "onDataRead: photos found: " + list.size());
                    if (list.isEmpty()) {
                        Log.w(TAG, "onDataRead: no need to upload photos. empty cache found");
                        return;
                    }
                    photos = list;
                    index = 0;
                    control();
                }

                @Override
                public void onError(String message) {

                }
            });
        } catch (Exception ex) {
            FirebaseCrash.report(new Exception("Failed to upload photos: " + ex.getMessage()));
        }

    }

    private void control() {
        if (index < photos.size()) {
            uploadPhoto(photos.get(index));
        } else {
            broadcast();
        }
    }

    private void uploadPhoto(final PhotoDTO photo) {
        final String photoKey = photo.getPhotoID();
        Log.d(TAG, "uploadPhoto: ........starting to upload: "
                .concat(gson.toJson(photo)));
        try {
            storageAPI.uploadPhoto(photo,
                    new FirebaseStorageAPI.StorageListener() {
                        @Override
                        public void onResponse(String key) {
                            Log.w(TAG, "onResponse: photo has been uploaded to storage and addded to database: "
                                    + photo.getUrl());
                            setAnalyticsEvent(photo);
                            deleteCachedPhoto(photoKey, new DeleteListener() {
                                @Override
                                public void onDeleteComplete() {
                                      success++;
                                }
                            });
                            if (photos != null) {
                                index++;
                                control();
                            } else {
                                broadcast();
                            }

                        }

                        @Override
                        public void onProgress(long transferred, long size) {

                        }

                        @Override
                        public void onError(String message) {
                            FirebaseCrash.report(new Exception(
                                    "Unable to upload photo: " + gson.toJson(photo)));
                            Log.e(TAG, "onError: unable to do da bizness: " + message);
                            failed++;
                            if (photos != null) {
                                index++;
                                control();
                            } else {
                                broadcast();
                            }
                            ;
                        }
                    });
        } catch (Exception e) {
            Log.e(TAG, "uploadPhoto: Failed, but continuing ...", e);
            index++;
            control();
        }
    }

    private interface DeleteListener {
        void onDeleteComplete();
    }

    private void deleteCachedPhoto(String key, final DeleteListener listener) {
        PhotoCache.deletePhoto(key, getApplicationContext(), new PhotoCache.WriteListener() {
            @Override
            public void onDataWritten() {
                Log.d(TAG, "onDataWritten: photo removed from cache");
                listener.onDeleteComplete();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    public static final String BROADCAST_PHOTO_UPLOADED = "COM.ONECONNECT.BROADCAST_photo_uploaded";


    private void broadcast() {
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(
                getApplicationContext());
        Intent m = new Intent(BROADCAST_PHOTO_UPLOADED);
        m.putExtra("photos", success);
        m.putExtra("failed",failed);
        broadcastManager.sendBroadcast(m);
        Log.d(TAG, "BROADCAST_PHOTO_UPLOADED: ##################" + photos +
                " photos uploaded - broadcast sent");

    }

    private void setAnalyticsEvent(PhotoDTO photo) {
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics
                .getInstance(getApplicationContext());
        try {
            Bundle bundle = new Bundle();
            bundle.putString("eventType", "photoTaken");
            bundle.putString("photo", photo.toString());
            mFirebaseAnalytics.logEvent("Photos", bundle);
            Log.w(TAG, "analytics photo event sent .....");
        } catch (Exception e) {
            Log.e(TAG, "setAnalytics: ", e);
        }


    }


}
