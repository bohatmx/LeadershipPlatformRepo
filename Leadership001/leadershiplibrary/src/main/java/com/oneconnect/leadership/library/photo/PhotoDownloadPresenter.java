package com.oneconnect.leadership.library.photo;

import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.api.ListAPI;
import com.oneconnect.leadership.library.data.ResponseBag;

/**
 * Created by christiannhlabano on 2017/07/05.
 */

public class PhotoDownloadPresenter implements  PhotoDownloadContract.Presenter{
    PhotoDownloadContract.View view;
    private ListAPI listAPI;

    public  PhotoDownloadPresenter(PhotoDownloadContract.View view) {
        this.view = view;
        listAPI = new ListAPI();
    }

    @Override
    public void getAllPhotos() {
        listAPI.getAllPhotos(new ListAPI.DataListener() {
            @Override
            public void onResponse(ResponseBag bag) {
                view.onAllPhotos(bag.getPhotos());
            }

            @Override
            public void onError(String messsage) {
                view.onError(messsage);
            }
        });
    }
}
