package com.oneconnect.leadership.library.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.adapters.ImportAdapter;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.dataImport.ImportContract;
import com.oneconnect.leadership.library.dataImport.ImportPresenter;
import com.oneconnect.leadership.library.util.Constants;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DataImportActivity extends AppCompatActivity implements ImportContract.View {

    ImportPresenter importPresenter;
    Context ctx;
    CompanyDTO company;
    List<DailyThoughtDTO> dailyThoughts = new ArrayList<>();
    List<UserDTO> users = new ArrayList<>();
    List<File> files = new ArrayList<>();
    int typeIndex = 0;
    RecyclerView recyclerView;
    private File file;
    private static final String USERS = "user", DAILYTHOUGHTS = "dailyThought";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_import);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ctx = getApplicationContext();
        importPresenter = new ImportPresenter(this, ctx);

        if (getIntent().getSerializableExtra("company") != null) {
            company = (CompanyDTO) getIntent().getSerializableExtra("company");
        }

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onImportFiles(List<File> files) {

    }

    @Override
    public void onParseDailyThoughtsFile(List<DailyThoughtDTO> dailyThoughtsList) {
        Log.w(TAG, "############# onParseDailyThoughtsFile: " + dailyThoughts.size());
        this.dailyThoughts = dailyThoughtsList;
        if (dailyThoughts.isEmpty()) {
            setEmptyList();
            return;
        }
        List<String> list = new ArrayList<>();
        for (DailyThoughtDTO u : this.dailyThoughts) {
            u.setCompanyID(company.getCompanyID());
            u.setCompanyName(company.getCompanyName());
            u.setCompanyID_status(company.getCompanyID().concat("_").concat(Constants.APPROVED));
            list.add(u.getSubtitle().concat(" ").concat(u.getTitle()));
        }
        setDailyThoughtList(list);

    }

    ImportAdapter importAdapter;

    private void setDailyThoughtList(List<String> list) {
        importAdapter = new ImportAdapter(list,
                new ImportAdapter.ImportListener() {

                    @Override
                    public void onRemoveRequired(String name, int position) {
                        try {
                            dailyThoughts.remove(position);
                            onParseDailyThoughtsFile(dailyThoughts);
                        } catch (Exception e) {
                            Log.e(TAG, "onRemoveRequired: ", e);
                        }
                    }
                });
        recyclerView.setAdapter(importAdapter);
    }

    private List<File> getImportFiles() {
        System.out.println(".............getImportFiles: .............");
        File extDir = Environment.getRootDirectory();
        String state = Environment.getExternalStorageState();

        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        List<File> fileList = getImportFilesOnSD();
        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(file);
            Log.d(TAG, "getImportFiles: ### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### Import Files in list : " + fileList.size());
        return fileList;
    }

    private List<File> getImportFilesOnSD() {
        System.out.println(".....................getImportFiles On SD: .........");
        File extDir = Environment.getExternalStorageDirectory();
        String state = Environment.getExternalStorageState();
        // TODO check thru all state possibilities
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return new ArrayList<File>();
        }

        List<File> fileList = new ArrayList<File>();

        @SuppressWarnings("unchecked")
        Iterator<File> iter = FileUtils.iterateFiles(extDir, new String[]{
                "csv"}, true);

        while (iter.hasNext()) {
            File file = iter.next();
            if (file.getName().startsWith("._")) {
                continue;
            }
            fileList.add(0, file);
            Log.d(TAG, "getImportFilesOnSD: ### disk Import File: " + file.getAbsolutePath());
        }

        Log.i(TAG, "### SD Import Files in list : " + fileList.size());
        return fileList;
    }

    private void setEmptyList() {
        if (file != null) {
            Log.d(TAG, "setEmptyList: ################## delete file: "
                    + file.getAbsolutePath() + " -  " + file.delete());
            getFiles(null);
        }
        List<String> list = new ArrayList<>();
        importAdapter = new ImportAdapter(list,
                new ImportAdapter.ImportListener() {

                    @Override
                    public void onRemoveRequired(String name, int position) {

                    }
                });
        recyclerView.setAdapter(importAdapter);
    }

    private interface FileListener {
        void onFilesFound(int count);
    }

    private void getFiles(final FileListener listener) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<File> importFiles = getImportFiles();
                files = new ArrayList<>();
                switch (typeIndex) {
                    case 1:
                        for (File f : importFiles) {
                            if (f.getName().contains(USERS)) {
                                files.add(f);
                            }
                        }
                        break;
                    case 2:
                        for (File f : importFiles) {
                            if (f.getName().contains(DAILYTHOUGHTS)) {
                                files.add(f);
                            }
                        }
                        break;
                }

                if (listener != null)
                    listener.onFilesFound(files.size());
            }
        });
        thread.start();
    }

    @Override
    public void onParseUsersFile(List<UserDTO> users) {
        Log.i(TAG, "users found: " + users.size());

    }

    @Override
    public void onUsersAdded(List<UserDTO> users) {
        Log.i(TAG, "onUsersAdded added: " + users.size());

    }

    @Override
    public void onDailyThoughtsAdded(List<DailyThoughtDTO> dailyThoughts) {
        Log.i(TAG, "onDailyThoughtsAdded: " + dailyThoughts.size());

    }

    @Override
    public void onError(String message) {
        Log.e(TAG, message);
    }

    static final String TAG = DataImportActivity.class.getSimpleName();
}
