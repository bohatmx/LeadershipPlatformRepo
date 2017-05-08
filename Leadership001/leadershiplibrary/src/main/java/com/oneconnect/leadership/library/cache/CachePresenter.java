package com.oneconnect.leadership.library.cache;

import android.content.Context;

import com.oneconnect.leadership.library.data.CalendarEventDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
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
 * Created by aubreymalabie on 3/18/17.
 */

public class CachePresenter implements CacheContract.Presenter {
    private CacheContract.View view;
    private Context context;

    public CachePresenter(CacheContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void cacheCategories(List<CategoryDTO> list) {
        CategoryCache.addCategories(list, context, new CategoryCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheDailyThoughts(List<DailyThoughtDTO> list) {
        DailyThoughtCache.addDailyThoughts(list, context, new DailyThoughtCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheEbooks(List<EBookDTO> list) {
        EbookCache.addEbooks(list, context, new EbookCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheNews(List<NewsDTO> list) {
        NewsCache.addNews(list, context, new NewsCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cachePodcasts(List<PodcastDTO> list) {
        PodcastCache.addPodcasts(list, context, new PodcastCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cachePrices(List<PriceDTO> list) {
        PriceCache.addPrices(list, context, new PriceCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheSubscriptions(List<SubscriptionDTO> list) {
        SubscriptionCache.addSubscriptions(list, context, new SubscriptionCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheUsers(List<UserDTO> list) {
        UserCache.addUsers(list, context, new UserCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheWeeklyMasterclasses(List<WeeklyMasterClassDTO> list) {
        WeeklyMasterclassCache.addWeeklyMasterclasses(list, context, new WeeklyMasterclassCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheWeeklyMessages(List<WeeklyMessageDTO> list) {
        WeeklyMessageCache.addWeeklyMessages(list, context, new WeeklyMessageCache.WriteListener() {
            @Override
            public void onDataWritten() {
                view.onDataCached();
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheVideos(List<VideoDTO> list) {
        VideoCache.getVideos(context, new VideoCache.ReadListener() {
            @Override
            public void onDataRead(List<VideoDTO> videos) {
                view.onCacheVideos(videos);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cachePhotos(List<PhotoDTO> list) {
        PhotoCache.getPhotos(context, new PhotoCache.ReadListener() {
            @Override
            public void onDataRead(List<PhotoDTO> photos) {
                view.onCachePhotos(photos);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void cacheCalendarEvents(List<CalendarEventDTO> list) {
        CalendarEventCache.getCalendarEvents(context, new CalendarEventCache.ReadListener() {
            @Override
            public void onDataRead(List<CalendarEventDTO> calendarEvents) {
                view.onCacheCalendarEvents(calendarEvents);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheCategories() {
        CategoryCache.getCategories(context, new CategoryCache.ReadListener() {
            @Override
            public void onDataRead(List<CategoryDTO> categories) {
                view.onCacheCategories(categories);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheDailyThoughts() {
        DailyThoughtCache.getDailyThoughts(context, new DailyThoughtCache.ReadListener() {
            @Override
            public void onDataRead(List<DailyThoughtDTO> dailyThoughts) {
                view.onCacheDailyThoughts(dailyThoughts);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheEbooks() {
        EbookCache.getEbooks(context, new EbookCache.ReadListener() {
            @Override
            public void onDataRead(List<EBookDTO> ebooks) {
                view.onCacheEbooks(ebooks);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheNews() {
        NewsCache.getNews(context, new NewsCache.ReadListener() {
            @Override
            public void onDataRead(List<NewsDTO> news) {
                view.onCacheNews(news);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCachePodcasts() {
        PodcastCache.getPodcasts(context, new PodcastCache.ReadListener() {
            @Override
            public void onDataRead(List<PodcastDTO> podcasts) {
                view.onCachePodcasts(podcasts);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCachePrices() {
        PriceCache.getPrices(context, new PriceCache.ReadListener() {
            @Override
            public void onDataRead(List<PriceDTO> prices) {
                view.onCachePrices(prices);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheSubscriptions() {
        SubscriptionCache.getSubscriptions(context, new SubscriptionCache.ReadListener() {
            @Override
            public void onDataRead(List<SubscriptionDTO> subscriptions) {
                view.onCacheSubscriptions(subscriptions);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheUsers() {
        UserCache.getUsers(context, new UserCache.ReadListener() {
            @Override
            public void onDataRead(List<UserDTO> users) {
                view.onCacheUsers(users);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheWeeklyMasterclasses() {
        WeeklyMasterclassCache.getWeeklyMasterclasses(context, new WeeklyMasterclassCache.ReadListener() {
            @Override
            public void onDataRead(List<WeeklyMasterClassDTO> weeklyMasterclasses) {
                view.onCacheWeeklyMasterclasses(weeklyMasterclasses);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheWeeklyMessages() {
        WeeklyMessageCache.getWeeklyMessages(context, new WeeklyMessageCache.ReadListener() {
            @Override
            public void onDataRead(List<WeeklyMessageDTO> weeklyMessages) {
                view.onCacheWeeklyMessages(weeklyMessages);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCacheVideos() {
        VideoCache.getVideos(context, new VideoCache.ReadListener() {
            @Override
            public void onDataRead(List<VideoDTO> videos) {
                view.onCacheVideos(videos);
            }

            @Override
            public void onError(String message) {
                view.onError(message);
            }
        });
    }

    @Override
    public void getCalendarEvents() {
            CalendarEventCache.getCalendarEvents(context, new CalendarEventCache.ReadListener() {
                @Override
                public void onDataRead(List<CalendarEventDTO> calendarEvents) {
                    view.onCacheCalendarEvents(calendarEvents);
                }

                @Override
                public void onError(String message) {
                    view.onError(message);
                }
            });
    }
}
