package com.oneconnect.leadership.admin.camera;

import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.VideoDTO;

/**
 * Created by aubreymalabie on 3/29/17.
 */

public class VideoUploadPresenter implements VideoUploadContract.Presenter {
    VideoUploadContract.View view;
    FirebaseStorageAPI api;

    public VideoUploadPresenter(VideoUploadContract.View view) {
        this.view = view;
        api = new FirebaseStorageAPI();
    }

    @Override
    public void uploadVideo(VideoDTO video) {
        api.uploadVideo(video, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                view.onVideoUploaded(key);
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
}
