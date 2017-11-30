package com.oneconnect.leadership.library.audio;

import com.oneconnect.leadership.library.api.DataAPI;
import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.PodcastDTO;

/**
 * Created by Nkululeko on 2017/04/11.
 */

public class PodcastUploadPresenter implements PodcastUploadContract.Presenter {
    PodcastUploadContract.View view;
    FirebaseStorageAPI api;
    DataAPI dataAPI;

    public PodcastUploadPresenter(PodcastUploadContract.View view) {
        this.view = view;
        api = new FirebaseStorageAPI();
        dataAPI = new DataAPI();
    }

    @Override
    public void uploadPodcast(PodcastDTO podcast) {
        api.uploadPodcast(podcast, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                view.onPodcastUploaded(key);
            }

            @Override
            public void onProgress(long transferred, long size) {
                view.onProgress(transferred,size);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void uploadPodcastRecording(PodcastDTO podcast) {
        api.uploadPodcastRecording(podcast, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                view.onPodcastUploaded(key);
            }

            @Override
            public void onProgress(long transferred, long size) {
                view.onProgress(transferred, size);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void addPodcastToEntity(PodcastDTO podcast) {
        dataAPI.addPodcastToEntity(podcast, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                view.onPodcastAddedToEntity(key);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }
}
