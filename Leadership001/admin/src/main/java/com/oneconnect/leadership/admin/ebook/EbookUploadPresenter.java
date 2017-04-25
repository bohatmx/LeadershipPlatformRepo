package com.oneconnect.leadership.admin.ebook;

import com.oneconnect.leadership.library.api.FirebaseStorageAPI;
import com.oneconnect.leadership.library.data.EBookDTO;

/**
 * Created by Nkululeko on 2017/04/12.
 */

public class EbookUploadPresenter implements EbookUploadContract.Presenter {
    EbookUploadContract.View view;
    FirebaseStorageAPI api;

    public EbookUploadPresenter(EbookUploadContract.View view) {
        this.view = view;
        api = new FirebaseStorageAPI();
    }

    @Override
    public void uploadEbook(EBookDTO eBook) {
        api.uploadEbook(eBook, new FirebaseStorageAPI.StorageListener() {
            @Override
            public void onResponse(String key) {
                view.onEbookUploaded(key);
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
