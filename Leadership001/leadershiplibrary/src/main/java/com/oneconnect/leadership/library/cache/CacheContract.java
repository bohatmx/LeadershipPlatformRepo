package com.oneconnect.leadership.library.cache;

import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.PriceDTO;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.List;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class CacheContract {
    public interface Presenter {
        void cacheCategories(List<CategoryDTO> list);
        void cacheDailyThoughts(List<DailyThoughtDTO> list);
        void cacheEbooks(List<EBookDTO> list);
        void cacheNews(List<NewsDTO> list);
        void cachePodcasts(List<PodcastDTO> list);
        void cachePrices(List<PriceDTO> list);
        void cacheSubscriptions(List<SubscriptionDTO> list);
        void cacheUsers(List<UserDTO> list);
        void cacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list);
        void cacheWeeklyMessages(List<WeeklyMessageDTO> list);

        void getCacheCategories();
        void getCacheDailyThoughts();
        void getCacheEbooks();
        void getCacheNews();
        void getCachePodcasts();
        void getCachePrices();
        void getCacheSubscriptions();
        void getCacheUsers();
        void getCacheWeeklyMasterclasses();
        void getCacheWeeklyMessages();
    }
    public interface View {
        void onDataCached();
        void onCacheCategories(List<CategoryDTO> list);
        void onCacheDailyThoughts(List<DailyThoughtDTO> list);
        void onCacheEbooks(List<EBookDTO> list);
        void onCacheNews(List<NewsDTO> list);
        void onCachePodcasts(List<PodcastDTO> list);
        void onCachePrices(List<PriceDTO> list);
        void onCacheSubscriptions(List<SubscriptionDTO> list);
        void onCacheUsers(List<UserDTO> list);
        void onCacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list);
        void onCacheWeeklyMessages(List<WeeklyMessageDTO> list);

        void onError(String message);
    }
}
