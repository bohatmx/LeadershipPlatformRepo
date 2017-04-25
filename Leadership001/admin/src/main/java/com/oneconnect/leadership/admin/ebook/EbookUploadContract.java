package com.oneconnect.leadership.admin.ebook;

import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;

/**
 * Created by Nkululeko on 2017/04/12.
 */

public class EbookUploadContract {

    public interface Presenter {
        void uploadEbook(EBookDTO eBook);
    }
    public interface View {
        void onEbookUploaded(String key);
        void onProgress(long transferred, long size);
        void onError(String message);
    }
}
