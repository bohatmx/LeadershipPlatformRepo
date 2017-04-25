package com.oneconnect.leadership.library.camera;

import com.oneconnect.leadership.library.data.VideoDTO;

/**
 * Created by aubreymalabie on 3/29/17.
 */

public class VideoUploadContract {
    public interface Presenter {
          void uploadVideo(VideoDTO video);
    }
    public interface View {
        void onVideoUploaded(String key);
        void onProgress(long transferred, long size);
        void onError(String message);
    }
}
