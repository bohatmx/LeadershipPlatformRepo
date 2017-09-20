package com.oneconnect.leadership.library.photo;

import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.UserDTO;

/**
 * Created by Nkululeko on 2017/05/11.
 */

public class PhotoUploadContract {
    public interface Presenter {
        void uploadPhoto(PhotoDTO photo);
        void uploadUserPhoto(UserDTO user);
    }
    public interface View {
        void onPhotoUploaded(String key);
        void onPhotoUserUploaded(String key);
        void onProgress(long transferred, long size);
        void onError(String message);

    }
}
