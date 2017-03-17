package com.oneconnect.leadership.library.api;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
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
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.ArrayList;

/**
 * Created by aubreymalabie on 2/12/17.
 */

public class ListAPI {

    public static final String TAG = ListAPI.class.getSimpleName();
    FirebaseDatabase db;

    public ListAPI() {
        db = FirebaseDatabase.getInstance();
    }
    public interface DataListener {
        void onResponse(ResponseBag bag);
        void onError(String messsage);
    }
    public void getDailyThoughts(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.DAILY_THOUGHTS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setDailyThoughts(new ArrayList<DailyThoughtDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        DailyThoughtDTO u = shot.getValue(DailyThoughtDTO.class);
                        bag.getDailyThoughts().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getNews(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.NEWS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setNews(new ArrayList<NewsDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        NewsDTO u = shot.getValue(NewsDTO.class);
                        bag.getNews().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getCompanies( final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.COMPANIES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setCompanies(new ArrayList<CompanyDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        CompanyDTO u = shot.getValue(CompanyDTO.class);
                        bag.getCompanies().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getDevices(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.DEVICES);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setDevices(new ArrayList<DeviceDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        DeviceDTO u = shot.getValue(DeviceDTO.class);
                        bag.getDevices().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getPhotos(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.PHOTOS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setPhotos(new ArrayList<PhotoDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        PhotoDTO u = shot.getValue(PhotoDTO.class);
                        bag.getPhotos().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getEBooks(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.EBOOKS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.seteBooks(new ArrayList<EBookDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        EBookDTO u = shot.getValue(EBookDTO.class);
                        bag.geteBooks().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getPayments(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.PAYMENTS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setPayments(new ArrayList<PaymentDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        PaymentDTO u = shot.getValue(PaymentDTO.class);
                        bag.getPayments().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getPodcasts(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.PODCASTS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setPodcasts(new ArrayList<PodcastDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        PodcastDTO u = shot.getValue(PodcastDTO.class);
                        bag.getPodcasts().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getPrices(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setPrices(new ArrayList<PriceDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        PriceDTO u = shot.getValue(PriceDTO.class);
                        bag.getPrices().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getSubscriptions(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setSubscriptions(new ArrayList<SubscriptionDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        SubscriptionDTO u = shot.getValue(SubscriptionDTO.class);
                        bag.getSubscriptions().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getVideo(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.VIDEOS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setVideos(new ArrayList<VideoDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        VideoDTO u = shot.getValue(VideoDTO.class);
                        bag.getVideos().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getWeeklyMasterclass(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.WEEKLY_MASTER_CLASSES);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setWeeklyMasterClasses(new ArrayList<WeeklyMasterClassDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        WeeklyMasterClassDTO u = shot.getValue(WeeklyMasterClassDTO.class);
                        bag.getWeeklyMasterClasses().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getWeeklyMessages(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.WEEKLY_MESSAGES);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setWeeklyMessages(new ArrayList<WeeklyMessageDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        WeeklyMessageDTO u = shot.getValue(WeeklyMessageDTO.class);
                        bag.getWeeklyMessages().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getCategories(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.CATEGORIES);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setCategories(new ArrayList<CategoryDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        CategoryDTO u = shot.getValue(CategoryDTO.class);
                        bag.getCategories().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getAllSubscribers(final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("userType").equalTo(UserDTO.SUBSCRIBER);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setUsers(new ArrayList<UserDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        UserDTO u = shot.getValue(UserDTO.class);
                        bag.getUsers().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getAllLeaders(final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("userType").equalTo(UserDTO.LEADER);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setUsers(new ArrayList<UserDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        UserDTO u = shot.getValue(UserDTO.class);
                        bag.getUsers().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getAllStaff(final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("userType").equalTo(UserDTO.COMPANY_STAFF);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setUsers(new ArrayList<UserDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        UserDTO u = shot.getValue(UserDTO.class);
                        bag.getUsers().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });
    }
    public void getCompanyStaff(String companyID, final DataListener listener) {
        DatabaseReference ref = db.getReference(DataAPI.USERS);
        Query q = ref.orderByChild("companyID").equalTo(companyID);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ResponseBag bag = new ResponseBag();
                bag.setUsers(new ArrayList<UserDTO>());
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot shot: dataSnapshot.getChildren()) {
                        UserDTO u = shot.getValue(UserDTO.class);
                        bag.getUsers().add(u);
                    }
                    listener.onResponse(bag);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                  listener.onError(databaseError.getMessage());
            }
        });
    }
}
