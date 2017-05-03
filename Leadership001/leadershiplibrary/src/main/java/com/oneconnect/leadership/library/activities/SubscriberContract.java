package com.oneconnect.leadership.library.activities;

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
 * Created by Nkululeko on 2017/04/18.
 */

public class SubscriberContract {

    public interface Presenter {

        void getCategories(String companyID);
        void getCompanies(String companyID);
        void getDailyThoughts(String companyID);
        void getAllCompanyDailyThoughts(String companyID);
        void getAllDailyThoughts();

        void getAllWeeklyMasterClasses();

        void getAllVideos();
        void getAllPodcasts();
        void getAllEBooks();
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

        void onCategories(List<CategoryDTO> list);
        void onCompanies(List<CompanyDTO> list);
        void onDailyThoughts(List<DailyThoughtDTO> list);
        void onAllCompanyDailyThoughts(List<DailyThoughtDTO> list);
        void onAllDailyThoughts(List<DailyThoughtDTO> list);
        void onAllVideos(List<VideoDTO> list);
        void onAllEBooks(List<EBookDTO> list);
        void onAllPodcasts(List<PodcastDTO> list);
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
