package com.oneconnect.leadership.library.util;

import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.DeviceDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PaymentDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.List;

/**
 * Created by Kurisani on 2017/08/30.
 */

public class DeleteContract {
    public interface Presenter {

        void deleteUser(UserDTO user);
        void deleteDailyThought(DailyThoughtDTO dailyThought);
        void deleteWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void deleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void deleteVideo(VideoDTO video);
        void deletePodcast(PodcastDTO podcast);
        void deleteNews(NewsDTO news);
        void deletePhoto(PhotoDTO photo);
        void deleteEbook(EBookDTO eBook);
    }

    public interface View {
        void onUserDeleted(UserDTO user);
        void onDailyThoughtDeleted(DailyThoughtDTO dailyThought);
        void onWeeklyMessageDeleted(WeeklyMessageDTO weeklyMessage);
        void onWeeklyMasterClassDeleted(WeeklyMasterClassDTO masterClass);
        void onVideoDeleted(VideoDTO video);
        void onPodcastDeleted(PodcastDTO podcast);
        void onNewsDeleted(NewsDTO news);
        void onPhotoDeleted(PhotoDTO photo);
        void onEbookDeleted(EBookDTO eBook);
        void onError(String message);
    }
}
