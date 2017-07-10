package com.oneconnect.leadership.library.photo;

import com.oneconnect.leadership.library.data.PhotoDTO;

import java.util.List;

/**
 * Created by christiannhlabano on 2017/07/05.
 */

public class PhotoDownloadContract {
    public interface Presenter {
        void getAllPhotos();
    }

    public interface View {
        void onAllPhotos(List<PhotoDTO> list);
        void onError(String message);
    }

}
