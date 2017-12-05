package com.oneconnect.leadership.library.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by nkululekochrisskosana on 2017/11/23.
 */

public class PldpDTO extends BaseDTO implements Serializable, Comparable<PldpDTO>{

    private String pldpID, actionName, sessionName, note;
    private String weeklyMasterClassID, weeklyMessageID, dailyThoughtID,
            eBookID, podcastID, videoID, newsID, podcastUrl, videoUrl;
    private long dateUpdated, reminderDate;
    private EBookDTO eBook;
    private VideoDTO video;
    private UrlDTO urlDTO;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private WeeklyMessageDTO weeklyMessage;
    private NewsDTO news;
    private int actionType;


    public static final String
            DESC_LISTEN = "Listen More",
            DESC_REMEMBER_STAFF_BIRTHDAYS = "Remember staff birthdays",
            DESC_ASSERTIVE = "Be Assertive",
            DESC_COMPASSION = "Show Compassion",
            DESC_START_SLOW = "Start slow",
            DESC_DONT_COMPARE_YOURSELF = "Compare yourself with yourself",
            DESC_REMEMBER_YOUR_SUCCESS = "Remember your successes",
            DESC_DONT_FEAR_FAILURE = "Don’t fear failure",
            DESC_CUT_DOWN_ON_TV = "Cut down on TV";

    public static final int
            LISTEN = 1,
            REMEMBER_STAFF_BIRTHDAYS = 2,
            ASSERTIVE = 3,
            COMPASSION = 4,
            START_SLOW = 5,
            DONT_COMPARE_YOURSELF = 6,
            REMEMBER_YOUR_SUCCESS = 7,
            DONT_FEAR_FAILURE = 8,
            CUT_DOWN_ON_TV = 9;

    public String getPldpID() {
        return pldpID;
    }

    public void setPldpID(String pldpID) {
        this.pldpID = pldpID;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getWeeklyMasterClassID() {
        return weeklyMasterClassID;
    }

    public void setWeeklyMasterClassID(String weeklyMasterClassID) {
        this.weeklyMasterClassID = weeklyMasterClassID;
    }

    public String getWeeklyMessageID() {
        return weeklyMessageID;
    }

    public void setWeeklyMessageID(String weeklyMessageID) {
        this.weeklyMessageID = weeklyMessageID;
    }

    public String getDailyThoughtID() {
        return dailyThoughtID;
    }

    public void setDailyThoughtID(String dailyThoughtID) {
        this.dailyThoughtID = dailyThoughtID;
    }

    public String geteBookID() {
        return eBookID;
    }

    public void seteBookID(String eBookID) {
        this.eBookID = eBookID;
    }

    public String getPodcastID() {
        return podcastID;
    }

    public void setPodcastID(String podcastID) {
        this.podcastID = podcastID;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public long getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(long dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public EBookDTO geteBook() {
        return eBook;
    }

    public void seteBook(EBookDTO eBook) {
        this.eBook = eBook;
    }

    public VideoDTO getVideo() {
        return video;
    }

    public void setVideo(VideoDTO video) {
        this.video = video;
    }

    public UrlDTO getUrlDTO() {
        return urlDTO;
    }

    public void setUrlDTO(UrlDTO urlDTO) {
        this.urlDTO = urlDTO;
    }

    public DailyThoughtDTO getDailyThought() {
        return dailyThought;
    }

    public void setDailyThought(DailyThoughtDTO dailyThought) {
        this.dailyThought = dailyThought;
    }

    public PodcastDTO getPodcast() {
        return podcast;
    }

    public void setPodcast(PodcastDTO podcast) {
        this.podcast = podcast;
    }

    public WeeklyMasterClassDTO getWeeklyMasterClass() {
        return weeklyMasterClass;
    }

    public void setWeeklyMasterClass(WeeklyMasterClassDTO weeklyMasterClass) {
        this.weeklyMasterClass = weeklyMasterClass;
    }

    public WeeklyMessageDTO getWeeklyMessage() {
        return weeklyMessage;
    }

    public void setWeeklyMessage(WeeklyMessageDTO weeklyMessage) {
        this.weeklyMessage = weeklyMessage;
    }

    public int getActionType() {
        return actionType;
    }
    public void setActionType(int actionType) {
        this.actionType = actionType;
        switch (actionType) {
            case LISTEN:
                actionName = DESC_LISTEN;
                break;
            case REMEMBER_STAFF_BIRTHDAYS:
                actionName = DESC_REMEMBER_STAFF_BIRTHDAYS;
                break;
            case ASSERTIVE:
                actionName = DESC_ASSERTIVE;
                break;
            case COMPASSION:
                actionName = DESC_COMPASSION;
                break;
            case START_SLOW:
                actionName = DESC_START_SLOW;
                break;
            case DONT_COMPARE_YOURSELF:
                actionName = DESC_DONT_COMPARE_YOURSELF;
                break;
            case REMEMBER_YOUR_SUCCESS:
                actionName = DESC_REMEMBER_YOUR_SUCCESS;
                break;
            case DONT_FEAR_FAILURE:
                actionName = DESC_DONT_FEAR_FAILURE;
                break;
            case CUT_DOWN_ON_TV:
                actionName = DESC_CUT_DOWN_ON_TV;
                break;
        }
    }

    @Override
    public void setJournalUserID(String userID) {
        this.journalUserID = userID;
    }

    @Override
    public void setJournalUserName(String userName) {
        this.journalUserName = userName;
    }

    public String getJournalUserID() {
        return journalUserID;
    }

    public String getJournalUserName() {
        return journalUserName;
    }

    @Override
    public int compareTo(@NonNull PldpDTO o) {
        return 0;
    }

    public String getPodcastUrl() {
        return podcastUrl;
    }

    public void setPodcastUrl(String podcastUrl) {
        this.podcastUrl = podcastUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getNewsID() {
        return newsID;
    }

    public void setNewsID(String newsID) {
        this.newsID = newsID;
    }

    public NewsDTO getNews() {
        return news;
    }

    public void setNews(NewsDTO news) {
        this.news = news;
    }

    public long getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(long reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
