package com.oneconnect.leadership.library.crud;

import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.BaseDTO;
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
 * Created by aubreymalabie on 3/17/17.
 */

public class CrudContract {
    public interface Presenter {
        void addEntity(BaseDTO entity);
        void updateEntity(BaseDTO entity);
        void updateUser(UserDTO user);
        void updateDailyThought(DailyThoughtDTO dailyThought);
        void updateWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void updateWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void updateNews(NewsDTO news);
        void updateCategory(CategoryDTO category);
        void createUser(UserDTO user);
        void deleteUser(UserDTO user);
        void deleteDailyThought(DailyThoughtDTO dailyThought);
        void deleteWeeklyMessage(WeeklyMessageDTO weeklyMessage);
        void deleteWeeklyMasterClass(WeeklyMasterClassDTO masterClass);
        void deleteVideo(VideoDTO video);
        void deletePodcast(PodcastDTO podcast);
        void deleteNews(NewsDTO news);
        void deletePhoto(PhotoDTO photo);
        void deleteEbook(EBookDTO eBook);
        void deleteCategory(CategoryDTO category);
        void getCategories(String companyID);
        void getCompanies(String companyID);
        void getDailyThoughts(String companyID);
        void getEbooks(String companyID);
        void getPayments(String companyID);
        void getPodcasts(String companyID);
        void getPhotos(String companyID);
        void getPrices(String companyID);
        void getUsers(String companyID);
        void getNews(String companyID);
        void getSubscriptions(String companyID);
        void getVideos(String companyID);
        void getWeeklyMasterclasses(String companyID);
        void getWeeklyMessages(String companyID);
        void getDevices(String companyID);
    }

    public interface View {
        void onEntityAdded(String key);
        void onEntityUpdated();
        void onUserCreated(UserDTO user);
        void onUserUpdated(UserDTO user);
        void onDailyThoughtUpdated(DailyThoughtDTO dailyThought);
        void onWeeklyMasterClassUpdated(WeeklyMasterClassDTO masterClass);
        void onWeeklyMessageUpdated(WeeklyMessageDTO weeklyMessage);
        void onNewsUpdated(NewsDTO news);
        void onUserDeleted(UserDTO user);
        void onDailyThoughtDeleted(DailyThoughtDTO dailyThought);
        void onWeeklyMessageDeleted(WeeklyMessageDTO weeklyMessage);
        void onWeeklyMasterClassDeleted(WeeklyMasterClassDTO masterClass);
        void onVideoDeleted(VideoDTO video);
        void onPodcastDeleted(PodcastDTO podcast);
        void onNewsDeleted(NewsDTO news);
        void onPhotoDeleted(PhotoDTO photo);
        void onEbookDeleted(EBookDTO eBook);
        void onCategoryDeleted(CategoryDTO category);
        void onCategories(List<CategoryDTO> list);
        void onCompanies(List<CompanyDTO> list);
        void onDailyThoughts(List<DailyThoughtDTO> list);
        void onEbooks(List<EBookDTO> list);
        void onPayments(List<PaymentDTO> list);
        void onPodcasts(List<PodcastDTO> list);
        void onPhotos(List<PhotoDTO> list);
        void onPrices(List<PriceDTO> list);
        void onUsers(List<UserDTO> list);
        void onNews(List<NewsDTO> list);
        void onSubscriptions(List<SubscriptionDTO> list);
        void onVideos(List<VideoDTO> list);
        void onWeeklyMasterclasses(List<WeeklyMasterClassDTO> list);
        void onWeeklyMessages(List<WeeklyMessageDTO> list);
        void onDevices(List<DeviceDTO> companyID);
        void onError(String message);
        void onCategoryUpdated(CategoryDTO category);
    }
}