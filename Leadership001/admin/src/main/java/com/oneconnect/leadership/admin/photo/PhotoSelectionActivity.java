package com.oneconnect.leadership.admin.photo;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.admin.ebook.EbookAdapter;
import com.oneconnect.leadership.admin.ebook.EbookUploadContract;
import com.oneconnect.leadership.admin.ebook.EbookUploadPresenter;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import es.dmoral.toasty.Toasty;

public class PhotoSelectionActivity extends AppCompatActivity implements PhotoUploadContract.View, EbookUploadContract.View{


    private RecyclerView recyclerView;
    private int type;
    private PhotoUploadPresenter presenter;
    private EbookUploadPresenter eBookpresenter;
    private Toolbar toolbar;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private EBookDTO eBook;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private Snackbar snackbar;
    public static final int PHOTO_REQUEST = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Photo Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        presenter = new PhotoUploadPresenter(this);
        //remove if it doesnt work
        eBookpresenter = new EbookUploadPresenter(this);

        type = getIntent().getIntExtra("type", 0/*type*/);

        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent().getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent().getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent().getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                break;
            //testing
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                break;
        }
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = 3;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(lm);


       // setUp();
        //walkdir(Environment.getExternalStorageDirectory());
        getFilePaths();
    }

    private void setUp() {


    }




    /*public void walkdir(File dir) {
        String pdfPattern = ".jpg";

        File listFile[] = dir.listFiles();
        filePathList = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++)  {
                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                    if (listFile[i].getName().contains(".jpg")|| listFile[i].getName().contains(".JPG")
                            || listFile[i].getName().contains(".jpeg")|| listFile[i].getName().contains(".JPEG")
                            || listFile[i].getName().contains(".png") || listFile[i].getName().contains(".PNG")
                            || listFile[i].getName().contains(".gif") || listFile[i].getName().contains(".GIF")
                            || listFile[i].getName().contains(".bmp") || listFile[i].getName().contains(".BMP")) {
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FilePathList: " + filePathList.toString());
                    }

                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FileName: " + listFile[i].getName());

                        //
                        PhotoAdapter adapter = new PhotoAdapter(filePathList, this, new PhotoAdapter.PhotoAdapterListener() {
                            @Override
                            public void onUploadPhoto(String path) {
                                confirmUpload(path);
                            }

                            @Override
                            public void onViewPhoto(String path) {
                                //viewPhoto(path);
                            }

                        });

                        recyclerView.setAdapter(adapter);


                    }
                }
            }
        }
    }*/

    public ArrayList<String> getFilePaths()
    {


        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        SortedSet<String> dirList = new TreeSet<String>();
        ArrayList<String> resultIAV = new ArrayList<String>();

        String[] directories = null;
        if (u != null)
        {
            c = managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst()))
        {
            do
            {
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));
                try{
                    dirList.add(tempDir);
                }
                catch(Exception e)
                {

                }
            }
            while (c.moveToNext());
            directories = new String[dirList.size()];
            dirList.toArray(directories);

        }

        for(int i=0;i<dirList.size();i++)
        {
            File imageDir = new File(directories[i]);
            File[] imageList = imageDir.listFiles();
            if(imageList == null)
                continue;
            for (File imagePath : imageList) {
                try {

                    if(imagePath.isDirectory())
                    {
                        imageList = imagePath.listFiles();

                    }
                    if ( imagePath.getName().contains(".jpg")|| imagePath.getName().contains(".JPG")
                            || imagePath.getName().contains(".jpeg")|| imagePath.getName().contains(".JPEG")
                            || imagePath.getName().contains(".png") || imagePath.getName().contains(".PNG")
                            || imagePath.getName().contains(".gif") || imagePath.getName().contains(".GIF")
                            || imagePath.getName().contains(".bmp") || imagePath.getName().contains(".BMP")
                            )
                    {
                        String path= imagePath.getAbsolutePath();
                        resultIAV.add(path);

                    }
                }
                //  }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //
            PhotoAdapter adapter = new PhotoAdapter(resultIAV, this, new PhotoAdapter.PhotoAdapterListener() {
                @Override
                public void onUploadPhoto(String path) {
                    confirmUpload(path);
                }

                @Override
                public void onViewPhoto(String path) {
                    //viewPhoto(path);
                }

            });

            recyclerView.setAdapter(adapter);
            //
        }

        return resultIAV;


    }
    List<String> filePathList;

    private void confirmUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this photo to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadPhoto(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }


    private void uploadPhoto(String path) {
        showSnackbar("Uploading photo ...", "OK", Constants.CYAN);
        PhotoDTO p = new PhotoDTO();

        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
        // v.setCompanyName(u.getCompanyName());
        // v.setCompanyID(u.getCompanyID());
        p.setFilePath(path);
        File file = new File(path);
        p.setImageSize(file.length());
        p.setBytes(file.length());
        if (file.getName() != null) {
            p.setCaption(file.getName());
        } else {
            p.setCaption("Leadership & Motivation");
        }
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                p.setDailyThoughtID(dailyThought.getDailyThoughtID());
                p.setTitle(dailyThought.getTitle());
               // p.setDescription(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                p.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                p.setTitle(weeklyMasterClass.getTitle());
                //p.setDescription(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                p.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                p.setTitle(weeklyMessage.getTitle());
                //p.setDescription(weeklyMessage.getTitle());
                break;
            case ResponseBag.EBOOKS:
                p.seteBookID(eBook.geteBookID());
                p.setTitle(eBook.getStorageName());
                break;
        }
        //openProgressSheet();
        presenter.uploadPhoto(p);
    }

    private File photoFile,  currentThumbFile;
    private List<PhotoDTO> photos = new ArrayList<>();


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
        Log.i(LOG, "onActivityResult: photoFile: ".concat(getSize(photoFile.length())));
        Toasty.success(this,"Photo taken OK", Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(String title, String action, String color) {
        snackbar = Snackbar.make(toolbar, title, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(Color.parseColor(color));
        snackbar.setAction(action, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private static final DecimalFormat df = new DecimalFormat("###,###,###,###,##0.00");

    private String getSize(long num) {
        BigDecimal d = new BigDecimal(num).divide(new BigDecimal(1024*1024));
        return df.format(d.doubleValue()) + " MB";
    }

    @Override
    public void onPhotoUploaded(String key) {

    }

    @Override
    public void onEbookUploaded(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {

    }

    @Override
    public void onError(String message) {

    }



    static final String LOG = PhotoSelectionActivity.class.getSimpleName();
}
