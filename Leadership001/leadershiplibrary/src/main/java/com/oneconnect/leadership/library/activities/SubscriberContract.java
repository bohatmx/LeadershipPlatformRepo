package com.oneconnect.leadership.library.activities;

import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CalendarEventDTO;
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
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/04/18.
 */

public class SubscriberContract {

    public interface Presenter {

        void getCategories(String companyID);
        void getCompanies(String companyID);
        void getDailyThoughts(String companyID);
        void getAllCompanyDailyThoughts(String companyID);
        void getDailyThoughtsRating(String dailyThoughtID);
        void getDailyThoughtsByUser(String userID);
        void getWeeklyMessageRating(String weeklyMessageID);
        void getWeeklyMasterClassRating(String weeklyMasterClassID);
        void getCategorisedWeeklyMessages(String categoryID);
        void getCategorisedWeeklyMasterClasses(String categoryID);
        void getCurrentUser(String email);
        void getCategorisedCalenderEvents();
        void getAllStaff();
        void getAllLeaders();
        void getAllSubscribers();
        void getAllSubscriptions();
        void getAllDailyThoughts();
        void getAllCategories();
        void getAllNewsArticle();
        void getAllWeeklyMessages();
        void getAllWeeklyMasterClasses();
        void getAllVideos();
        void getAllPodcasts();
        void getAllEBooks();
        void getAllPhotos();
        void getAllCalendarEvents();
        void getAllRatings();
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
        void onUserFound(UserDTO user);
        void onAllRatings(List<RatingDTO> list);
        void onDailyThoughtRatings(List<RatingDTO> list);
        void onWeeklyMessageRatings(List<RatingDTO> list);
        void onWeeklyMasterClassRatings(List<RatingDTO> list);
        void onDailythoughtsByUser(List<DailyThoughtDTO> list);
        void onCategories(List<CategoryDTO> list);
        void onCompanies(List<CompanyDTO> list);
        void onDailyThoughts(List<DailyThoughtDTO> list);
        void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list);
        void onAllDailyThoughts(List<DailyThoughtDTO> list);
        void onAllSubscriptions(List<SubscriptionDTO> list);
        void onAllNewsArticle(List<NewsDTO> list);
        void onAllCategories(List<CategoryDTO> list);
        void onAllVideos(List<VideoDTO> list);
        void onAllEBooks(List<EBookDTO> list);
        void onAllPhotos(List<PhotoDTO> list);
        void onAllWeeklyMessages(List<WeeklyMessageDTO> list);
        void onAllPodcasts(List<PodcastDTO> list);
        void onAllCalendarEvents(List<CalendarEventDTO> list);
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
    }
}
