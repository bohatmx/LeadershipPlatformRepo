package com.oneconnect.leadership.library.photo;

import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.PhotoDTO;

/**
 * Created by Nkululeko on 2017/05/11.
 */

public class PhotoUploadPresenter implements PhotoUploadContract.Presenter {

    PhotoUploadContract.View view;
    FirebaseStorageAPI api;

    public PhotoUploadPresenter(PhotoUploadContract.View view) {
        this.view = view;
        api = new FirebaseStorageAPI();
    }

    @Override
    public void uploadPhoto(PhotoDTO photo) {
        api.uploadPhoto(photo, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                view.onPhotoUploaded(key);
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
}
