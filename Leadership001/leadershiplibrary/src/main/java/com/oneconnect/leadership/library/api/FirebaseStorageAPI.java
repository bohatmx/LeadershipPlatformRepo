package com.oneconnect.leadership.library.api;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
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
import com.oneconnect.leadership.library.data.PhotoDTO;
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
            PHOTOS = "photos/", VIDEOS = "videos/", THUMBNAILS = "thumbnails/";


    public FirebaseStorageAPI() {
        dataAPI = new DataAPI();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public interface StorageListener {
        void onResponse(String key);

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

        Log.d(TAG, "uploadPhoto: ".concat(photo.getFilePath()));
        final File f = new File(photo.getFilePath());
        if (f.exists()) {
            Log.d(TAG, "uploadPhoto: prior to upload: "
                    + f.length() + " - " + f.getAbsolutePath());
        } else {
            listener.onError("Cannot find file for upload");
            return;
        }
        StorageReference photoReference = storageReference.child(PHOTOS
                + "/"
                + photo.getCaption().concat(" ").concat(sdf.format(new Date())));
        Log.d(TAG, "uploadPhoto: starting upload ...: " + f.getAbsolutePath());
        try {
            photoReference.putStream(new FileInputStream(f)).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
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
        if (f.exists()) {
            Log.d(TAG, "uploadVideo: prior to upload: "
                    + getSize(f.length()) + " - " + f.getAbsolutePath());
        } else {
            listener.onError("Cannot find file for upload");
            return;
        }
        final String storageName = video.getCaption().concat(" - ").concat(sdf.format(new Date()));
        StorageReference videoReference = storageReference.child(VIDEOS
                + "/"
                + storageName);
        Log.d(TAG, "uploadVideo: starting upload ...: " + f.getAbsolutePath());
        try {

            videoReference.putStream(new FileInputStream(f))
                    .addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i(TAG, "onSuccess: ".concat(taskSnapshot.getDownloadUrl().toString()));
                    video.setStorageName(VIDEOS.concat("/").concat(storageName));
                    video.setUrl(taskSnapshot.getDownloadUrl().toString());
                    addVideoToFirebase(video, taskSnapshot, listener);
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
        Log.i(TAG, "The photo has been succesfully uploaded: "
                + taskSnapshot.getDownloadUrl().toString());
        photo.setUrl(taskSnapshot.getDownloadUrl().toString());
        photo.setBytes(taskSnapshot.getTotalByteCount());
        photo.setDateUploaded(new Date().getTime());
        photo.setFilePath(null);
        photo.setPhotoID(null);
        dataAPI.addPhoto(photo, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                Log.w(TAG, "onResponse: photo added to firebase, OK: " + key);
                photo.setPhotoID(key);
                listener.onResponse(key);
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
        Log.i(TAG, "The video has been succesfully uploaded: "
                + taskSnapshot.getDownloadUrl().toString());
        video.setUrl(taskSnapshot.getDownloadUrl().toString());
        video.setVideoSize(taskSnapshot.getTotalByteCount());
        video.setDateUploaded(new Date().getTime());
        video.setFilePath(null);
        video.setVideoID(null);
        dataAPI.addVideo(video, new DataAPI.DataListener() {
            @Override
            public void onResponse(String key) {
                Log.w(TAG, "onResponse: video added to firebase, OK: " + key);
                video.setVideoID(key);
                listener.onResponse(key);
            }

            @Override
            public void onError(String message) {
                listener.onError(message);
            }
        });

    }
}
