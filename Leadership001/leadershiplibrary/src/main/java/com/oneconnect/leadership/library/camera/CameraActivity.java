package com.oneconnect.leadership.library.camera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.bumptech.glide.Glide;
import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CameraActivity extends AppCompatActivity {

    public static final int CAMERA_REQUEST = 9985, VIDEO_REQUEST = 7663;
    private ImageView iconCamera, iconDelete, iconInfo, iconVideo, image;
    private double latitude, longitude;
    private int type;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private MaterialCamera materialCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setup();

        type = getIntent().getIntExtra("type", 0);
        dailyThought = (DailyThoughtDTO)getIntent().getSerializableExtra("dailyThought");
        podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
        weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
        weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");

        if (dailyThought != null) {
            Log.d(TAG, "onCreate: ".concat(GSON.toJson(dailyThought)));
        }
        switch (type) {
            case CAMERA_REQUEST:
                startCamera();
                break;
            case VIDEO_REQUEST:
                startVideoCamera();
                break;
        }
    }

    private void setup() {
        iconCamera = (ImageView) findViewById(R.id.iconCamera);
        iconDelete = (ImageView) findViewById(R.id.iconCancel);
        iconVideo = (ImageView) findViewById(R.id.iconVideo);
        iconInfo = (ImageView) findViewById(R.id.iconInfo);
        image = (ImageView) findViewById(R.id.image);

        latitude = getIntent().getDoubleExtra("latitude", 0.0);
        longitude = getIntent().getDoubleExtra("longitude", 0.0);

        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });
        iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideoCamera();
            }
        });
        iconInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMediaInfo();
            }
        });
    }

    private void showMediaInfo() {

    }

    private void startCamera() {
        new MaterialCamera(this)
                .stillShot()
                .labelConfirm(R.string.confirm_photo)
                .start(CAMERA_REQUEST);
    }

    private void startVideoCamera() {
        new MaterialCamera(this)
                .labelConfirm(R.string.confirm_video)
                .videoFrameRate(24)
                .videoPreferredAspect(4f / 3f)
                .videoEncodingBitRate(1024000)
                .audioEncodingBitRate(50000)
                .maxAllowedFileSize(1024*1024*1024)
                .videoPreferredHeight(720)
                .showPortraitWarning(false)
                .start(VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "$$$$$$$$$$$$$$ onActivityResult: requestCode " + requestCode
                + " resultCode: " + resultCode);
        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = Uri.parse(data.getDataString());
                savePhotoFile(uri);
                display();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                Log.e(TAG, "onActivityResult: ", e);
                Toasty.error(this, e.getMessage(), Toast.LENGTH_LONG, true).show();
            }
        }
        if (requestCode == VIDEO_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri uri = Uri.parse(data.getDataString());
                saveVideoFile(uri);
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                Log.e(TAG, "onActivityResult: ", e);
                Toasty.error(this, e.getMessage(), Toast.LENGTH_LONG, true).show();

            }
        }
    }

    private void savePhotoFile(Uri uri) {
        photoFile = new File(uri.getPath());
        PhotoDTO p = new PhotoDTO();
        p.setFilePath(photoFile.getAbsolutePath());
        UserDTO u = SharedPrefUtil.getUser(this);
        p.setJournalUserName(u.getFullName());
        p.setJournalUserID(u.getUserID());
        p.setCompanyID(u.getCompanyID());
        p.setCompanyName(u.getCompanyName());
        p.setImageSize(photoFile.length());
        p.setBytes(photoFile.length());
        photos.add(p);
        Log.i(TAG, "onActivityResult: photoFile: ".concat(getSize(photoFile.length())));
        Toasty.success(this,"Photo taken OK", Toast.LENGTH_SHORT).show();
    }

    private void saveVideoFile(Uri uri) {
        videoFile = new File(uri.getPath());
        VideoDTO p = new VideoDTO();
        p.setFilePath(videoFile.getAbsolutePath());
        UserDTO u = SharedPrefUtil.getUser(this);
        p.setJournalUserName(u.getFullName());
        p.setJournalUserID(u.getUserID());
        p.setCompanyID(u.getCompanyID());
        p.setCompanyName(u.getCompanyName());
        p.setVideoSize(videoFile.length());
        videos.add(p);
        Log.i(TAG, "onActivityResult: videoFile: ".concat(getSize(videoFile.length())));
        Toasty.success(this,"Video taken OK", Toast.LENGTH_SHORT).show();
        showSnackBar("Video saved on disk","ok","green");
    }

    private void display() {
        Glide.with(this).load(photoFile).into(image);
    }

    private String getSize(long num) {
        BigDecimal d = new BigDecimal(num).divide(new BigDecimal(1024*1024));
        return df.format(d.doubleValue()) + " MB";

    }
    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00");
    @Override
    public void onBackPressed() {
        ResponseBag bag = new ResponseBag();
        Intent m = new Intent();
        bag.setPhotos(photos);
        bag.setVideos(videos);
        m.putExtra("bag",bag);

        if (!photos.isEmpty() || !videos.isEmpty()) {
            setResult(RESULT_OK, m);
        } else {
            setResult(RESULT_CANCELED);
        }
        Log.e(TAG, "onBackPressed: ************** photos: "
                + photos.size() + " videos: " + videos.size());

        finish();
    }

    private List<VideoDTO> videos = new ArrayList<>();
    private List<PhotoDTO> photos = new ArrayList<>();
    private File photoFile, videoFile, currentThumbFile;
    public static final String TAG = CameraActivity.class.getSimpleName();

    class PhotoTask extends AsyncTask<Void, Void, Integer> {

        double latitude, longitude;

        /**
         * Scale the image to required size and delete the larger one
         *
         * @param voids
         * @return
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            try {
                if (photoFile == null || photoFile.length() == 0) {
                    Log.e(TAG, "----->> photoFile is null or length 0, exiting");
                    return 99;
                } else {
                    Log.w(TAG, "## PhotoTask starting, photoFile length: "
                            + photoFile.length());
                }
                processFile();
            } catch (Exception e) {
                Log.e(TAG, "Camera file processing failed", e);
                return 9;
            }


            return 0;
        }


        protected void processFile() throws Exception {

            Log.d(TAG, "processFile: photoFile: " + photoFile.length());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
//            options.outHeight = 768;
//            options.outWidth = 1024;
            Bitmap bm = BitmapFactory.decodeFile(photoFile.getAbsolutePath(), options);
            getLog(bm, "main bitmap");

            if (bm.getWidth() > bm.getHeight()) {
                Log.d(TAG, "*** this image in landscape ...............................");
                bm = Util.rotateBitmap(bm);

            }
            getLog(bm, "scaled Bitmap");
            currentThumbFile = getFileFromBitmap(bm);

            //write exif data
            Util.writeLocationToExif(currentThumbFile.getAbsolutePath(), latitude, longitude);
            bm.recycle();
            Log.i(TAG, "## photo file length: " + getLength(currentThumbFile.length())
                    + ", original size: " + getLength(photoFile.length()));
            //write exif from original photo

        }

        protected File getFileFromBitmap(Bitmap bm)
                throws Exception {
            if (bm == null) throw new Exception();
            File file = null;
            try {
                File rootDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                if (rootDir == null) {
                    rootDir = Environment.getRootDirectory();
                }
                File imgDir = new File(rootDir, "traffic_app");
                if (!imgDir.exists()) {
                    imgDir.mkdir();
                }
                OutputStream outStream = null;
                file = new File(imgDir, "traffic" + System.currentTimeMillis() + ".jpg");

                outStream = new FileOutputStream(file);
                bm.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
                outStream.flush();
                outStream.close();

            } catch (Exception e) {
                Log.e(TAG, "Failed to get file from bitmap", e);
            }
            Log.i(TAG, "getFileFromBitmap: size: " + file.length());
            return file;

        }

        private void getLog(Bitmap bm, String which) {
            if (bm == null) return;
            Log.e(TAG, which + " - bitmap: width: "
                    + bm.getWidth() + " height: "
                    + bm.getHeight() + " rowBytes: "
                    + bm.getRowBytes());
        }

        private String getLength(long num) {
            BigDecimal decimal = new BigDecimal(num).divide(new BigDecimal(1024), 2, BigDecimal.ROUND_HALF_UP);

            return "" + decimal.doubleValue() + " KB";
        }

        @Override
        protected void onPostExecute(Integer result) {
            Log.e(TAG, "onPostExecute completed image resizing, result: " + result.intValue());
            if (result > 0) {
                FirebaseCrash.report(new Exception("Unable to process image from camera"));
                showSnackBar("Unable to process photo", "Bad Camera?", Constants.RED);
                return;
            }
            PhotoDTO p = new PhotoDTO();
            p.setFilePath(currentThumbFile.getAbsolutePath());
            photos.add(p);
            Glide.with(getApplicationContext()).load(currentThumbFile).into(image);
            showSnackBar("Phtoto taken, number of photos: " + photos.size(), "OK", Constants.GREEN);
        }
    }

    Snackbar snackbar;

    public void showSnackBar(String title, String action, String color) {
        snackbar = Snackbar.make(iconCamera, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public void showSnackBar(String title) {
        snackbar = Snackbar.make(iconCamera, title, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
