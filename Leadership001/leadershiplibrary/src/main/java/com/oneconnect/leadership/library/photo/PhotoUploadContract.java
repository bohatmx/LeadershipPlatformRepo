package com.oneconnect.leadership.library.photo;

import com.oneconnect.leadership.library.data.PhotoDTO;

/**
 * Created by Nkululeko on 2017/05/11.
 */

public class PhotoUploadContract {
    public interface Presenter {
        void uploadPhoto(PhotoDTO photo);
    }
    public interface View {
        void onPhotoUploaded(String key);
        void onProgress(long transferred, long size);
        void onError(String message);
    }
}
