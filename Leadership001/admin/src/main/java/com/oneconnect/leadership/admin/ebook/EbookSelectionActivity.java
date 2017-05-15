package com.oneconnect.leadership.admin.ebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.ProgressBottomSheet;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;



public class EbookSelectionActivity extends AppCompatActivity implements EbookUploadContract.View{

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private DailyThoughtDTO dailyThought;
    private WeeklyMessageDTO weeklyMessage;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private int type;
    private Snackbar snackbar;
    private EbookUploadPresenter presenter;
    public static final String TAG = EbookSelectionActivity.class.getSimpleName();


    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_selection);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("EBook Selection & Upload");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        presenter = new EbookUploadPresenter(this);

        type = getIntent().getIntExtra("type", 0);
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
        }

        setup();
       // getEbooksOnDevice();
        walkdir(Environment.getExternalStorageDirectory());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void walkdir(File dir) {
        String pdfPattern = ".pdf";

        File listFile[] = dir.listFiles();
        filePathList = new ArrayList<>();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++)  {
                if (listFile[i].isDirectory()) {
                    walkdir(listFile[i]);
                    if (listFile[i].getName().contains(".pdf")) {
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FilePathList: " + filePathList.toString());
                    }

                } else {
                    if (listFile[i].getName().endsWith(pdfPattern)){
                        filePathList.add(listFile[i].getAbsolutePath());
                        Log.d(LOG, "FileName: " + listFile[i].getName());

                        //
                        EbookAdapter adapter = new EbookAdapter(filePathList, this, new EbookAdapter.EbookAdapterListener() {
                            @Override
                            public void onUploadEbook(String path) {
                                confirmUpload(path);
                            }

                            @Override
                            public void onReadEbook(String path) {
                                readEbook(path);
                            }
                        });

                        recyclerView.setAdapter(adapter);


                    }
                }
            }
        }
    }
    List<String> filePathList;
    public void getEbooksOnDevice() {

        File dir = Environment.getExternalStorageDirectory();

        File[] files = dir.listFiles();
        Log.d(LOG, "getEBooksOnDevice " + dir.getName());
        filePathList = new ArrayList<>();
        if (files != null) {
            for (File file: files) {
                if (file.getName().contains("dp.pdf")) {
                    filePathList.add(file.getAbsolutePath());
                }
            }
        }
        if (filePathList.isEmpty()) {
            Log.d(LOG, "file pathPathList is empty");
        } else {
            fileContainerList.clear();
            for (String d : filePathList) {
                fileContainerList.add(new FileContainer(d));
            }
            Collections.sort(fileContainerList);
            filePathList.clear();
            for (FileContainer f : fileContainerList) {
                filePathList.add(f.fileName);
            }
            EbookAdapter adapter = new EbookAdapter(filePathList, this, new EbookAdapter.EbookAdapterListener() {
                @Override
                public void onUploadEbook(String path) {
                    confirmUpload(path);
                }

                @Override
                public void onReadEbook(String path) {
                    readEbook(path);
                }
            });

            recyclerView.setAdapter(adapter);
        }





       /* HashSet<String> eBooksItemHashSet = new HashSet<>();
        String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME
        };
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        try {
            cursor.moveToFirst();
            do {

                Log.d(TAG, "getEbooksOnDevice: ".concat(cursor.getColumnNames().toString()));
                String path =  cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE));
                Log.e(TAG, "getEbooksOnDevice: duration: " + duration + " path: ".concat(path));
                localEbooks.add(new LocalEbook(duration,path));
                eBooksItemHashSet.add(path);
            } while (cursor.moveToNext());

            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "getEbooksOnDevice: ", e);
        }
        ArrayList<String> downloadedList = new ArrayList<>(eBooksItemHashSet);
        for (String id : downloadedList) {
            Log.e(TAG, "getEbooksOnDevice: ".concat(id));
        }
*/

    }





    List<FileContainer> fileContainerList = new ArrayList<>();

    class FileContainer implements Comparable<FileContainer> {
        String fileName;
        Date date;

        public FileContainer(String filename) {
            this.fileName = filename;
            try {
                Pattern patt = Pattern.compile("-");
                String[] parts = patt.split(filename);

                DateTime dateTime = new DateTime(Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]), 1, 0, 0);
                date = dateTime.toDate();
            } catch (Exception e) {
                Log.e("FileContainer", "problem", e);
                date = new Date();
            }
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public int compareTo(FileContainer another) {
            if (this.date.before(another.date)) {
                return 1;
            }
            if (this.date.after(another.date)) {
                return -1;
            }
            return 0;
        }
    }

    List<LocalEbook> localEbooks = new ArrayList<>();
    class LocalEbook {
        long duration;
        String path;

        public LocalEbook(long duration, String path) {
            this.duration = duration;
            this.path = path;
        }
    }

    static final String LOG = EbookSelectionActivity.class.getSimpleName();

    private void setup() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void confirmUpload(final String path) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to upload this ebook to the database?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        uploadEbook(path);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void readEbook(String path) {

        File f = new File(path);
        if (f.exists()) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(f), "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    private void uploadEbook(String path) {
        showSnackbar("Uploading ebook ...", "OK", Constants.CYAN);
        EBookDTO v = new EBookDTO();
        UserDTO u = SharedPrefUtil.getUser(getApplicationContext());
       // v.setCompanyName(u.getCompanyName());
       // v.setCompanyID(u.getCompanyID());
        v.setFilePath(path);
        File file = new File(path);
        v.setEbookSize(file.length());
        v.setActive(true);
        switch (type) {
            case ResponseBag.DAILY_THOUGHTS:
                v.setDailyThoughtID(dailyThought.getDailyThoughtID());
                v.setTitle(dailyThought.getTitle());
                v.setDescription(dailyThought.getTitle());
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                v.setWeeklyMasterClassID(weeklyMasterClass.getWeeklyMasterClassID());
                v.setTitle(weeklyMasterClass.getTitle());
                v.setDescription(weeklyMasterClass.getTitle());
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                v.setWeeklyMessageID(weeklyMessage.getWeeklyMessageID());
                v.setTitle(weeklyMessage.getTitle());
                v.setDescription(weeklyMessage.getTitle());
                break;
        }
        //openProgressSheet();
        presenter.uploadEbook(v);
    }

    private ProgressBottomSheet progressBottomSheet;
    private void openProgressSheet() {
        progressBottomSheet = ProgressBottomSheet.newInstance();
        progressBottomSheet.show(getSupportFragmentManager(),"PROGRESS_SHEET");
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

    @Override
    public void onEbookUploaded(String key) {

    }

    @Override
    public void onProgress(long transferred, long size) {

    }

    @Override
    public void onError(String message) {

    }
}
