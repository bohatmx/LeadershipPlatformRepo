package com.oneconnect.leadership.library.api;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
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

import static android.R.attr.mimeType;
import static android.content.Context.DOWNLOAD_SERVICE;


/**
 * Created by josephmokenela on 10/14/16.
 */

@SuppressWarnings("VisibleForTests")
public class FirebaseStorageAPI extends Activity {

    private static final String TAG = FirebaseStorageAPI.class.getSimpleName();

    private StorageReference storageReference;
    private DataAPI dataAPI;
    private  Context ctx;
    private String path;
    private  String bookUrl;
    private String fileName;
    ProgressDialog dialog;

    public static final String STORAGE_URL = "gs://leadershipplatform-158316.appspot.com",
            PHOTOS = "photos/", VIDEOS = "videos/", THUMBNAILS = "thumbnails/", PODCASTS = "podcasts/",
            EBOOKS = "ebooks/";
    public static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;

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

    public void downloadEbook(String bookUrl, final String fileName, String path, final Context ctx) {
        this.ctx = ctx;
        this.bookUrl = bookUrl;
        this.fileName = fileName;
        this.path = path.substring(path.indexOf("//")) + ".pdf";
        checkWriteExternalStoragePermission();
     }

    BroadcastReceiver onComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            if(dialog != null && dialog.isShowing()){
                dialog.dismiss();
            }
            readEbook();
        }
    };

    protected void readEbook() {
        Log.w("IR", "TRYING TO RENDER: " + path);
        File file = new File(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri apkURI = FileProvider.getUriForFile(
                ctx,
                ctx.getApplicationContext()
                        .getPackageName() + ".provider", file);
        intent.setDataAndType(apkURI, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        ctx.startActivity(intent);
    }

    public void checkWriteExternalStoragePermission() {
        ContextCompat.checkSelfPermission(ctx,      Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }else{
            startDownloadEbook();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownloadEbook();
                } else {
                    Toast.makeText(ctx, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;

            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void startDownloadEbook(){
        File bookName = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName + ".pdf");;

        if(!bookName.exists()){
            DownloadManager.Request r = new DownloadManager.Request(Uri.parse(bookUrl));

            // This put the download in the same Download dir the browser uses
            r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName + ".pdf");
            //r.setDestinationInExternalPublicDir(Environment.getExternalStorageDirectory().getAbsolutePath() + "eBooks", "Christian.pdf");

            // When downloading music and videos they will be listed in the player
            // (Seems to be available since Honeycomb only)
            r.allowScanningByMediaScanner();

            // Notify user when download is completed
            // (Seems to be available since Honeycomb only)
            r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            // Start download
            dialog = new ProgressDialog(ctx);
            //set message of the dialog
            dialog.setMessage("Loading...");
            //show dialog
            dialog.show();
            DownloadManager dm = (DownloadManager) ctx.getSystemService(DOWNLOAD_SERVICE);
            ctx.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            dm.enqueue(r);
        }else{
            readEbook();
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
