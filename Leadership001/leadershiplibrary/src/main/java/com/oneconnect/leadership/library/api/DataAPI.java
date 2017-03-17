package com.oneconnect.leadership.library.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.CountryDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.SubscriptionDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by aubreymalabie on 2/12/17.
 */

public class DataAPI {
    public static final String TAG = DataAPI.class.getSimpleName();
    private FirebaseDatabase db;


    public DataAPI() {
        db = FirebaseDatabase.getInstance();
    }

    public interface DataListener {
        void onResponse(String key);

        void onError(String message);
    }

    public static final String
            COUNTRIES = "countries",
            CATEGORIES = "categories",
            DAILY_THOUGHTS = "dailyThoughts",
            EBOOKS = "eBooks",
            PAYMENTS = "payments",
            PODCASTS = "podcasts",
            PHOTOS = "photos",
            PRICES = "prices",
            USERS = "users",
            NEWS = "news",
            SUBSCRIPTIONS = "subscriptions",
            VIDEOS = "videos",
            COMPANIES = "companies",
            WEEKLY_MASTER_CLASSES = "weeklyMasterClasses",
            WEEKLY_MESSAGES = "weeklyMessages";

    static Random random = new Random(System.currentTimeMillis());

    public String getRandomPassword() {
        StringBuilder sb = new StringBuilder();

        sb.append(getRandomLetter());
        sb.append(getRandomLetter());
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(9));
        }
        sb.append("$");
        sb.append(getRandomLetter());

        return sb.toString();
    }

    private String getRandomLetter() {
        return letters[random.nextInt(letters.length - 1)];
    }

    public interface CreateUserListener {
        void onUserCreated(UserDTO user);
        void onUserAlreadyExists(UserDTO user);
        void onError(String message);
    }

    public interface OnSignedIn {
        void onSuccess(FirebaseUser user);
        void onError();
    }

    public interface OnDataRead {
        void onResponse(ResponseBag responseBag);
        void onError();
    }
    public interface EmailQueryListener {
        void onUserFoundByEmail(UserDTO user);
        void onUserNotFoundByEmail();
        void onError(String message);
    }
    public interface AddUserListener {
        void onUserAdded(UserDTO user);
        void onUserAlreadyExists(UserDTO user);
        void onError(String message);
    }
    public void createUser(final UserDTO user,
                           final CreateUserListener listener) {
        Log.d(TAG, "createUser: starting to create user: " + user.getEmail());
        getUserByEmail(user.getEmail(), new EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                listener.onError("User already exists");
                return;
            }

            @Override
            public void onUserNotFoundByEmail() {
                performWrite(user, listener);
            }

            @Override
            public void onError(String message) {
               listener.onError(message);
            }
        });


    }

    private void performWrite(final UserDTO user, final CreateUserListener listener) {
        try {
            if (user.getPassword() == null) {
                user.setPassword(getRandomPassword());
            }
            if (mAuth == null)
                mAuth = FirebaseAuth.getInstance();

            Log.d(TAG, "createUser: email: " + user.getEmail() + " " + user.getPassword());

            Task<AuthResult> authResultTask = mAuth.createUserWithEmailAndPassword(
                    user.getEmail(), user.getPassword());
            authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser fbUser = authResult.getUser();
                    Log.i(TAG, ".............onSuccess: user added to Leadership Platform: "
                            + fbUser.getEmail() + " "
                            + fbUser.getUid());
                    user.setUid(fbUser.getUid());
                    Log.w(TAG, "onSuccess: add user to actual database ***************");

                    addUser(user, new AddUserListener() {
                        @Override
                        public void onUserAdded(UserDTO user) {
                              listener.onUserCreated(user);
                        }

                        @Override
                        public void onUserAlreadyExists(UserDTO user) {
                             listener.onUserAlreadyExists(user);
                        }

                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }
                    });


                }
            });
            authResultTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report(e);
                    Log.e(TAG, "================ onFailure: ", e);
                    listener.onError("Unable to register user: " + user.getEmail());
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "createUser failed!: ", e);
        }
    }

    static FirebaseAuth mAuth;


    public void signIn(final String email, final String password, final OnSignedIn onSignedIn) {
        Log.d(TAG, ".....................signIn: email: " + email);

        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser fbUser = task.getResult().getUser();
                            Log.i(TAG, "####### onComplete: we cool, displayName: "
                                    + fbUser.getDisplayName() + " email: " + fbUser.getEmail()
                                    + " uid: " + fbUser.getUid() + " \ntoken: " + fbUser.getToken(true));
                             onSignedIn.onSuccess(fbUser);
                        } else {
                            Log.e(TAG, "------------ sign in FAILED: task not successful");
                            onSignedIn.onError();
                        }
                    }
                });
    }


    public void getUserByEmail(final String email, final EmailQueryListener listener) {
        Log.d(TAG, "################## getUserByEmail: find user by mail: " + email);
        final long start = System.currentTimeMillis();
        DatabaseReference usersRef = db.getReference(USERS);
        Query q = usersRef.orderByChild("email").equalTo(email);
        log(usersRef);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: getUser: dataSnapshot:" + dataSnapshot);

                if (dataSnapshot.getChildren() == null || dataSnapshot.getChildrenCount() == 0) {
                    Log.e(TAG, "onDataChange: getUser: no users found for email: " + email);
                    listener.onUserNotFoundByEmail();
                    return;
                }
                Log.w(TAG, "onDataChange: getUser: users found by email: "
                        + dataSnapshot.getChildrenCount());

                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    UserDTO u = shot.getValue(UserDTO.class);
                    listener.onUserFoundByEmail(u);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError(databaseError.getMessage());
            }
        });

    }


    public void getUserByUid(final String uid, final OnDataRead listener) {
        Log.d(TAG, "########### getUser: get user by uid: " + uid);
        final long start = System.currentTimeMillis();
        DatabaseReference usersRef = db.getReference(USERS);

        Query q = usersRef.orderByChild("uid").equalTo(uid);
        log(usersRef);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i(TAG, "onDataChange: getUser: dataSnapshot:" + dataSnapshot);
                ResponseBag b = new ResponseBag();
                b.setUsers(new ArrayList<UserDTO>());
                if (dataSnapshot.getChildren() == null) {
                    Log.e(TAG, "onDataChange: getUser: no users found for uid: " + uid);
                    listener.onError();
                    return;
                }
                Log.w(TAG, "onDataChange: getUser: users found for uid: "
                        + dataSnapshot.getChildrenCount());
                for (DataSnapshot shot : dataSnapshot.getChildren()) {
                    UserDTO u = shot.getValue(UserDTO.class);
                    b.getUsers().add(u);
                    Log.d(TAG, "onDataChange: getUser: about to send user via listener");
                    long end = System.currentTimeMillis();
                    Log.e(TAG, "********* onDataChange: getUser, elapsed seconds: " + (end - start) / 1000);
                    listener.onResponse(b);
                    return;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onError();
            }
        });


    }

    public void addUser(final UserDTO c, final AddUserListener listener) {
        getUserByEmail(c.getEmail(), new EmailQueryListener() {
            @Override
            public void onUserFoundByEmail(UserDTO user) {
                  listener.onUserAlreadyExists(user);
            }

            @Override
            public void onUserNotFoundByEmail() {
                Log.w(TAG, "addUser: onUserNotFoundByEmail: now add the new user to database" );
                DatabaseReference userRef = db.getReference(USERS);
                log(userRef);
                userRef.push().setValue(c, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            databaseReference.child("userID").setValue(databaseReference.getKey());
                            Log.i(TAG, "***************** onComplete: user added to firebase: " + c.getEmail());
                            c.setUserID(databaseReference.getKey());
                            listener.onUserAdded(c);

                        } else {
                            listener.onError(databaseError.getMessage());
                        }

                    }
                });
            }

            @Override
            public void onError(String message) {
                 listener.onError(message);
            }
        });


    }

    public void addNews(final NewsDTO news, final DataListener listener) {
        final DatabaseReference ref = db.getReference(NEWS);
        log(ref);
        ref.push().setValue(news, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: news added: "
                            + news.getCompanyName());
                    news.setNewsID(responseRef.getKey());
                    responseRef.child("newsID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }
    public void addSubscription(final SubscriptionDTO subscription, final DataListener listener) {
        final DatabaseReference ref = db.getReference(SUBSCRIPTIONS);
        log(ref);
        ref.push().setValue(subscription, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: subscription added: "
                            + subscription.getCompanyName());
                    subscription.setSubscriptionID(responseRef.getKey());
                    responseRef.child("subscriptionID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addDailyThoughts(final DailyThoughtDTO dailyThought, final DataListener listener) {
        final DatabaseReference ref = db.getReference(DAILY_THOUGHTS);
        log(ref);
        ref.push().setValue(dailyThought, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: dailyThought added: "
                            + dailyThought.getTitle());
                    dailyThought.setDailyThoughtID(responseRef.getKey());
                    responseRef.child("countryID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addCompany(final CompanyDTO company, final DataListener listener) {
        final DatabaseReference ref = db.getReference(COMPANIES);
        log(ref);
        ref.push().setValue(company, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: company added: "
                            + company.getCompanyName());
                    company.setCompanyID(responseRef.getKey());
                    responseRef.child("companyID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addCountry(final CountryDTO country, final DataListener listener) {
        final DatabaseReference ref = db.getReference(COUNTRIES);
        log(ref);
        ref.push().setValue(country, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: country added: "
                            + country.getCountryName());
                    country.setCountryID(responseRef.getKey());
                    responseRef.child("countryID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addCategory(final CategoryDTO category, final DataListener listener) {
        final DatabaseReference ref = db.getReference(CATEGORIES);
        log(ref);
        ref.push().setValue(category, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: category added: "
                            + category.getCategoryName());
                    category.setCategoryID(responseRef.getKey());
                    responseRef.child("categoryID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addEBooks(final EBookDTO eBook, final DataListener listener) {
        final DatabaseReference ref = db.getReference(EBOOKS);
        log(ref);
        ref.push().setValue(eBook, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: eBook added: "
                            + eBook.getTitle());
                    eBook.seteBookID(responseRef.getKey());
                    responseRef.child("eBookID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addPhoto(final PhotoDTO photo, final DataListener listener) {
        final DatabaseReference ref = db.getReference(PHOTOS);
        log(ref);
        ref.push().setValue(photo, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: photo added: "
                            + photo.getCaption());
                    photo.setPhotoID(responseRef.getKey());
                    responseRef.child("photoID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addVideo(final VideoDTO video, final DataListener listener) {
        final DatabaseReference ref = db.getReference(VIDEOS);
        log(ref);
        ref.push().setValue(video, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: video added: "
                            + video.getCaption());
                    video.setVideoID(responseRef.getKey());
                    responseRef.child("videoID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addWeeklyMessage(final WeeklyMessageDTO weeklyMessage, final DataListener listener) {
        final DatabaseReference ref = db.getReference(WEEKLY_MESSAGES);
        log(ref);
        ref.push().setValue(weeklyMessage, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: weeklyMessage added: "
                            + weeklyMessage.getTitle());
                    weeklyMessage.setWeeklyMessageID(responseRef.getKey());
                    responseRef.child("weeklyMessageID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addWeeklyMasterClass(final WeeklyMasterClassDTO weeklyMasterClass, final DataListener listener) {
        final DatabaseReference ref = db.getReference(WEEKLY_MASTER_CLASSES);
        log(ref);
        ref.push().setValue(weeklyMasterClass, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError,
                                   final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: weeklyMasterClass added: "
                            + weeklyMasterClass.getTitle());
                    weeklyMasterClass.setWeeklyMasterClassID(responseRef.getKey());
                    responseRef.child("weeklyMasterClassID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public void addPodcast(final PodcastDTO podcast, final DataListener listener) {
        final DatabaseReference ref = db.getReference(PODCASTS);
        log(ref);
        ref.push().setValue(podcast, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, final DatabaseReference responseRef) {
                if (databaseError == null) {
                    Log.i(TAG, "------------- onComplete: podcast added: "
                            + podcast.getTitle());
                    podcast.setPodcastID(responseRef.getKey());
                    responseRef.child("podcastID").setValue(responseRef.getKey());
                    if (listener != null)
                        listener.onResponse(responseRef.getKey());

                } else {
                    if (listener != null)
                        listener.onError(databaseError.getMessage());
                }
            }
        });
    }

    public static void log(DatabaseReference ref) {
        Log.w("Firebase APIs", "Firebase Request Log: databaseReference: " + ref.getRef().toString());
    }

    private String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "m", "n", "p", "q", "w", "x", "y", "z"};
}
