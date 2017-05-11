package com.oneconnect.leadership.library.api;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ThumbnailDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by josephmokenela on 10/14/16.
 */

@SuppressWarnings("VisibleForTests")
public class FirebaseStorageAPI {

    private static final String TAG = FirebaseStorageAPI.class.getSimpleName();

    private StorageReference storageReference;
    private DataAPI dataAPI;
    public static final String STORAGE_URL = "gs://leadershipplatform-158316.appspot.com",
            PHOTOS = "photos/", VIDEOS = "videos/", THUMBNAILS = "thumbnails/", PODCASTS = "podcasts/",
            EBOOKS = "ebooks/";


    public FirebaseStorageAPI() {
        dataAPI = new DataAPI();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public interface StorageListener {
        void onResponse(String key);
        void onProgress(long transferred, long size);
        void onError(String message);
    }

    public interface DownloadListener {
        void onResponse(File file);

        void onError(String message);
    }

    public void getThumbnailFromVideo(String videoFilePath, File thumbnail) {
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(
                videoFilePath, MediaStore.Video.Thumbnails.MINI_KIND);

        Util.getFileFromBitmap(thumb, thumbnail);
        Log.d(TAG, "getThumbnailFromVideo: thumbnail: " + thumbnail.length());
    }

    public void downloadVideoFile(final String name, final File destinationFile, final DownloadListener listener) {
        StorageReference ref = storageReference.child(VIDEOS.concat(name));
        ref.getFile(destinationFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: File has been downloaded: " + destinationFile.length());
                        listener.onResponse(destinationFile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: destinationFile download failed ", e);
                        FirebaseCrash.report(new Exception("Video destinationFile download failed: ".concat(name)));
                        listener.onError("Video destinationFile download failed");
                    }
                });
    }





    public void uploadThumbnail(final ThumbnailDTO thumbnail,
                                final StorageListener listener) {

        Log.d(TAG, "uploadThumbnail: ".concat(thumbnail.getFilePath()));
        final File f = new File(thumbnail.getFilePath());
        if (f.exists()) {
            Log.d(TAG, "uploadThumbnail: prior to upload: "
                    + f.length() + " - " + f.getAbsolutePath());
        } else {
            listener.onError("Cannot find file for upload");
            return;
        }
        StorageReference reference = storageReference.child(THUMBNAILS
                + "/"
                + f.getName().concat(" ").concat(sdf.format(new Date())));
        Log.d(TAG, "uploadThumbnail: starting upload ...: " + f.getAbsolutePath());
        try {
            reference.putStream(new FileInputStream(f)).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    thumbnail.setUrl(taskSnapshot.getDownloadUrl().toString());
                    dataAPI.addThumbnail(thumbnail, new DataAPI.DataListener() {
                        @Override
                        public void onResponse(String key) {
                            Log.i(TAG, "onResponse: thumbnail added to firebase: ".concat(key));
                            listener.onResponse(key);
                        }

                        @Override
                        public void onError(String message) {
                            listener.onError(message);
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.w(TAG, "onProgress: bytes transferred: "
                            + taskSnapshot.getBytesTransferred()
                            + " from total " + taskSnapshot.getTotalByteCount()
                            + " done: ".concat(getPercentage(taskSnapshot.getBytesTransferred(),
                            taskSnapshot.getTotalByteCount())));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "uploadThumbnail: we fell down", e);
                    FirebaseCrash.report(new Exception("Thumbnail file upload failed "));
                    listener.onError("Unable to upload the bleeding file");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadThumbnail: we fell down", e);
            FirebaseCrash.report(new Exception("Photo file upload failed "));
            listener.onError("Unable to upload the bleeding file");
        }
    }

    public void uploadPhoto(final PhotoDTO photo,
                            final StorageListener listener) {

        Log.d(TAG, "###### uploadPhoto: ".concat(photo.getFilePath())
                .concat("\n").concat(GSON.toJson(photo)));
        final File f = new File(photo.getFilePath());
        if (!f.exists()) {
            listener.onError("Cannot find file for upload");
            return;
        }
        StorageReference photoReference = storageReference.child(PHOTOS
                + "/"
                + photo.getCaption().concat(" ").concat(sdf.format(new Date())));
        try {
            photoReference.putStream(new FileInputStream(f)).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess: photo uploaded to firebase storage");
                    addPhotoToFirebase(photo, taskSnapshot, listener);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "### onProgress: bytes transferred: "
                            + getSize(taskSnapshot.getBytesTransferred())
                            + " from total " + getSize(f.length()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                   listener.onError("Photo file upload failed");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadPhoto: we fell down", e);
            FirebaseCrash.report(new Exception("Photo file upload failed: ".concat(photo.getTitle())));
            listener.onError("Unable to upload the photo file");
        }
    }
    private String getSize(long num) {
        BigDecimal d = new BigDecimal(num).divide(new BigDecimal(1024*1024));
        return df.format(d.doubleValue()) + " MB";

    }
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public void uploadVideo(final VideoDTO video,
                            final StorageListener listener) {

        final File f = new File(video.getFilePath());
        if (!f.exists()) {
            listener.onError("Cannot find video file for upload");
            return;
        }
        final String storageName = video.getFilePath()/*getCaption()*/.concat(" - ").concat(sdf.format(new Date()));
        StorageReference videoReference = storageReference.child(VIDEOS
                + storageName);
        Log.w(TAG, "uploadVideo: ******** starting upload ...: "
                + f.getAbsolutePath().concat("\n").concat(GSON.toJson(video)));
        try {

            videoReference.putStream(new FileInputStream(f))
                    .addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    video.setStorageName(VIDEOS.concat(storageName));
                    video.setUrl(taskSnapshot.getDownloadUrl().toString());
                    Log.d(TAG, "uploadVideo onSuccess: ####### video in firebase storage.....starting addVideoToFirebase method ...");
                    addVideoToFirebase(video, taskSnapshot, listener);
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "### onProgress: bytes transferred: "
                            + getSize(taskSnapshot.getBytesTransferred())
                            + " from total " + getSize(f.length()));
                    listener.onProgress(taskSnapshot.getBytesTransferred(), f.length());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report(new Exception("Video file upload failed: "));
                    listener.onError("Video upload failed");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadVideo: we fell down", e);
            FirebaseCrash.report(new Exception("Video file upload failed: "
                    .concat(video.getTitle())));
            listener.onError("Unable to upload the video file");
        }
    }

    public void uploadEbook(final EBookDTO eBook,
                            final StorageListener listener) {

        final File f = new File(eBook.getFilePath());
        if (!f.exists()) {
            listener.onError("Cannot find eBook file for upload");
            return;
        }
        final String storageName = eBook.getFilePath()/*.concat(" - ").concat(sdf.format(new Date()))*/;
        StorageReference eBookReference = storageReference.child(EBOOKS
                + storageName);
        Log.w(TAG, "uploadEbook: ******** starting upload ...: "
                + f.getAbsolutePath().concat("\n").concat(GSON.toJson(eBook)));
        try {

            eBookReference.putStream(new FileInputStream(f))
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    eBook.setStorageName(EBOOKS.concat(storageName));
                                    eBook.setUrl(taskSnapshot.getDownloadUrl().toString());
                                    Log.d(TAG, "uploadEbook onSuccess: ####### eBook in firebase storage.....starting addEbookToFirebase method ...");
                                    addEBookToFirebase(eBook, taskSnapshot, listener);
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "### onProgress: bytes transferred: "
                            + getSize(taskSnapshot.getBytesTransferred())
                            + " from total " + getSize(f.length()));
                    listener.onProgress(taskSnapshot.getBytesTransferred(), f.length());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report(new Exception("Ebook file upload failed: "));
                    listener.onError("Ebook upload failed");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadEbook: we fell down", e);
            FirebaseCrash.report(new Exception("Ebook file upload failed: "
                    .concat(eBook.getTitle())));
            listener.onError("Unable to upload the eBook file");
        }
    }

    public void uploadPodcast(final PodcastDTO podcast,
                            final StorageListener listener) {

        final File f = new File(podcast.getFilePath());
        if (!f.exists()) {
            listener.onError("Cannot find podcast file for upload");
            return;
        }
        final String storageName = podcast.getFilePath().concat(" - ").concat(sdf.format(new Date()));
        StorageReference podcastReference = storageReference.child(PODCASTS
                + storageName);
        Log.w(TAG, "uploadPodcast: ******** starting upload ...: "
                + f.getAbsolutePath().concat("\n").concat(GSON.toJson(podcast)));
        try {

            podcastReference.putStream(new FileInputStream(f))
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    podcast.setStorageName(PODCASTS.concat(storageName));
                                    podcast.setUrl(taskSnapshot.getDownloadUrl().toString());
                                    Log.d(TAG, "uploadPodcast onSuccess: ####### podcast in firebase storage.....starting addPodcastToFirebase method ...");
                                    addPodcastToFirebase(podcast, taskSnapshot, listener);
                                }
                            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.e(TAG, "### onProgress: bytes transferred: "
                            + getSize(taskSnapshot.getBytesTransferred())
                            + " from total " + getSize(f.length()));
                    listener.onProgress(taskSnapshot.getBytesTransferred(), f.length());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseCrash.report(new Exception("Podcast file upload failed: "));
                    listener.onError("Podcast upload failed");
                }
            });

        } catch (Exception e) {
            Log.e(TAG, "uploadPodcast: we fell down", e);
            FirebaseCrash.report(new Exception("Podcast file upload failed: "
                    .concat(podcast.getTitle())));
            listener.onError("Unable to upload the podcast file");
        }
    }

    private static  final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private String getPercentage(long transferred, long total) {
        BigDecimal a = new BigDecimal(transferred)
                .divide(new BigDecimal(total))
                .multiply(new BigDecimal(100));
        return "" + df.format(a.doubleValue()) + " %";
    }
    private static final DecimalFormat df = new DecimalFormat("##0.00");
    @SuppressWarnings("VisibleForTests")
    private void addPhotoToFirebase(final PhotoDTO photo,
                                    UploadTask.TaskSnapshot taskSnapshot,
                                    final StorageListener listener) {
        Log.i(TAG, "...about to add photoDTO to firebase: "
                + taskSnapshot.getDownloadUrl().toString());
        photo.setUrl(taskSnapshot.getDownloadUrl().toString());
        photo.setDateUploaded(new Date().getTime());
        photo.setFilePath(null);
        photo.setPhotoID(null);
        dataAPI.addPhoto(photo, new DataAPI.DataListener() {
            @Override
            public void onResponse(final String key) {
                Log.w(TAG, "onResponse: photo added to firebase, OK: " + key);
                photo.setPhotoID(key);
                dataAPI.addPhotoToEntity(photo, new DataAPI.DataListener() {
                    @Override
                    public void onResponse(String k) {
                        listener.onResponse(key);
                    }

                    @Override
                    public void onError(String message) {
                        listener.onError(message);
                    }
                });

            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });

    }

    @SuppressWarnings("VisibleForTests")
    private void addVideoToFirebase(final VideoDTO video,
                                    UploadTask.TaskSnapshot taskSnapshot,
                                    final StorageListener listener) {
        Log.d(TAG, ".........adding videoDTO to firebase  ...: "
                + GSON.toJson(video));
        video.setUrl(taskSnapshot.getDownloadUrl().toString());
        video.setDateUploaded(new Date().getTime());
        video.setFilePath(null);
        video.setVideoID(null);
        dataAPI.addVideo(video, new DataAPI.DataListener() {
            @Override
            public void onResponse(final String key) {
                video.setVideoID(key);
                Log.w(TAG, "dataAPI addVideo onResponse: ".concat(key).concat(" - adding videoDTO to entity ...") );
                dataAPI.addVideoToEntity(video, new DataAPI.DataListener() {
                    @Override
                    public void onResponse(String k) {
                        listener.onResponse(key);
                    }

                    @Override
                    public void onError(String message) {
                       listener.onError(message);
                    }
                });

            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });

    }

    private void addPodcastToFirebase(final PodcastDTO podcast,
                                    UploadTask.TaskSnapshot taskSnapshot,
                                    final StorageListener listener) {
        Log.d(TAG, ".........adding PodcastDTO to firebase  ...: "
                + GSON.toJson(podcast));
        podcast.setUrl(taskSnapshot.getDownloadUrl().toString());
        podcast.setDateScheduled(new Date().getTime());
        podcast.setFilePath(null);
        podcast.setPodcastID(null);
        dataAPI.addPodcast(podcast, new DataAPI.DataListener() {
            @Override
            public void onResponse(final String key) {
                podcast.setPodcastID(key);
                Log.w(TAG, "dataAPI addPodcast onResponse: ".concat(key).concat(" - adding PodcastDTO to entity ...") );
                dataAPI.addPodcastToEntity(podcast, new DataAPI.DataListener() {
                    @Override
                    public void onResponse(String k) {
                        listener.onResponse(key);
                    }

                    @Override
                    public void onError(String message) {
                        listener.onError(message);
                    }
                });

            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });

    }

    private void addEBookToFirebase(final EBookDTO eBook,
                                      UploadTask.TaskSnapshot taskSnapshot,
                                      final StorageListener listener) {
        Log.d(TAG, ".........adding EBookDTO to firebase  ...: "
                + GSON.toJson(eBook));
        eBook.setUrl(taskSnapshot.getDownloadUrl().toString());
        eBook.setDate(new Date().getTime());
        eBook.setFilePath(null);
        eBook.seteBookID(null);
        dataAPI.addEBook(eBook, new DataAPI.DataListener() {
            @Override
            public void onResponse(final String key) {
                eBook.seteBookID(key);
                Log.w(TAG, "dataAPI addEBook onResponse: ".concat(key).concat(" - adding EBookDTO to entity ...") );
                dataAPI.addEBookToEntity(eBook, new DataAPI.DataListener() {
                    @Override
                    public void onResponse(String k) {
                        listener.onResponse(key);
                    }

                    @Override
                    public void onError(String message) {
                        listener.onError(message);
                    }
                });

            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });

    }

}
