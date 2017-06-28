package com.oneconnect.leadership.library.links;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.EBookDTO;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.PodcastDTO;
import com.oneconnect.leadership.library.data.ResponseBag;
import com.oneconnect.leadership.library.data.UrlDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.VideoDTO;
import com.oneconnect.leadership.library.data.WeeklyMasterClassDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

public class LinksActivity extends AppCompatActivity implements LinksContract.View {
    public static final String TAG = LinksActivity.class.getSimpleName();
    private TextView txtTitle;
    private ImageView iconAdd;
    private WebView webView;
    private DailyThoughtDTO dailyThought;
    private PodcastDTO podcast;
    private VideoDTO video;
    private EBookDTO eBook;
    private NewsDTO news;
    private WeeklyMasterClassDTO weeklyMasterClass;
    private WeeklyMessageDTO weeklyMessage;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private FloatingActionButton fab;
    private int type;
    private LinksPresenter presenter;
    private TextInputEditText editSearch;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Supporting Links");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        }

        presenter = new LinksPresenter(this);

        type = getIntent().getIntExtra("type", 0);
        switch (type) {
            case ResponseBag.NEWS:
                news = (NewsDTO) getIntent()
                        .getSerializableExtra("newsArticle");
                getSupportActionBar().setSubtitle(news.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(news)));
                break;
            case ResponseBag.DAILY_THOUGHTS:
                dailyThought = (DailyThoughtDTO) getIntent()
                        .getSerializableExtra("dailyThought");
                getSupportActionBar().setSubtitle(dailyThought.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(dailyThought)));
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                weeklyMasterClass = (WeeklyMasterClassDTO) getIntent()
                        .getSerializableExtra("weeklyMasterClass");
                getSupportActionBar().setSubtitle(weeklyMasterClass.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(weeklyMasterClass)));
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                weeklyMessage = (WeeklyMessageDTO) getIntent()
                        .getSerializableExtra("weeklyMessage");
                getSupportActionBar().setSubtitle(weeklyMessage.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(weeklyMessage)));
                break;
            case ResponseBag.PODCASTS:
                podcast = (PodcastDTO) getIntent()
                        .getSerializableExtra("podcast");
                getSupportActionBar().setSubtitle(podcast.getTitle());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(podcast)));
                break;
            case ResponseBag.VIDEOS:
                video = (VideoDTO) getIntent().getSerializableExtra("video");
                getSupportActionBar().setSubtitle(video.getStorageName());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(video)));
                break;
            case ResponseBag.EBOOKS:
                eBook = (EBookDTO) getIntent()
                        .getSerializableExtra("eBook");
                getSupportActionBar().setSubtitle(eBook.getStorageName());
                Log.d(TAG, "onCreate: ".concat(GSON.toJson(eBook)));
                break;
        }
        if (getIntent().getSerializableExtra("eBook") != null) {
            type = ResponseBag.EBOOKS;
            eBook = (EBookDTO) getIntent().getSerializableExtra("eBook");
            getSupportActionBar().setSubtitle(eBook.getStorageName());
        }
        if (getIntent().getSerializableExtra("video") != null) {
            type = ResponseBag.VIDEOS;
            video = (VideoDTO) getIntent().getSerializableExtra("video");
            getSupportActionBar().setSubtitle(video.getStorageName());
        }
        if (getIntent().getSerializableExtra("podcast") != null) {
            type = ResponseBag.PODCASTS;
            podcast = (PodcastDTO) getIntent().getSerializableExtra("podcast");
            getSupportActionBar().setSubtitle(podcast.getStorageName());
        }
        if (getIntent().getSerializableExtra("newsArticle") != null) {
            type = ResponseBag.NEWS;
            news = (NewsDTO) getIntent().getSerializableExtra("newsArticle");
            getSupportActionBar().setSubtitle(news.getTitle());
        }



        setup();
    }

    private void setup() {
        webView = (WebView) findViewById(R.id.webView);
        editSearch = (TextInputEditText) findViewById(R.id.editSearch);
        iconAdd = (ImageView) findViewById(R.id.iconAdd);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                startSearch();
            }
        });
        iconAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                confirm();
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                Log.i(TAG, "Finished loading URL: " + url);
                hideKeyboard();

            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.e(TAG, "Error: " + description);
                hideKeyboard();

            }
        });

        webView.loadUrl("https://www.google.com");
    }

    private void startSearch() {
        if (TextUtils.isEmpty(editSearch.getText())) {
            editSearch.setError("Enter search text");
            return;
        }
        hideKeyboard();
        String[] parts = editSearch.getText().toString().split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i]).append("+");
        }
        webView.loadUrl("https://www.google.com/search?q=".concat(sb.toString()));
    }

    private void confirm() {
        StringBuilder sb = new StringBuilder();
        sb.append(webView.getTitle()).append("\n");
        sb.append(webView.getUrl());
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Confirmation")
                .setMessage("Do you want to add this link?\n\n".concat(sb.toString()))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addLink();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void addLink() {
        Log.d(TAG, "addLink: ............".concat(webView.getTitle()));

        UrlDTO u = new UrlDTO();
        UserDTO user = SharedPrefUtil.getUser(this);
        u.setCompanyID(user.getCompanyID());
        u.setCompanyName(user.getCompanyName());
        u.setTitle(webView.getTitle());
        u.setUrl(webView.getUrl());

        showSnackbar("Adding link ....", "OK", "yellow");
        switch (type) {
            case ResponseBag.NEWS:
                presenter.addLink(u, news.getNewsID(), type);
                break;
            case ResponseBag.DAILY_THOUGHTS:
                presenter.addLink(u, dailyThought.getDailyThoughtID(), type);
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                presenter.addLink(u, weeklyMasterClass.getWeeklyMasterClassID(), type);
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                presenter.addLink(u, weeklyMessage.getWeeklyMessageID(), type);
                break;
            case ResponseBag.PODCASTS:
                presenter.addLink(u, podcast.getPodcastID(), type);
                break;
            case ResponseBag.VIDEOS:
                presenter.addLink(u, video.getVideoID(), type);
                break;
            case ResponseBag.EBOOKS:
                presenter.addLink(u, eBook.geteBookID(), type);
                break;
        }

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
    public void onLinkAdded(String key) {
        Log.i(TAG, "onLinkAdded: .................. ".concat(key));
        showSnackbar(webView.getTitle().concat(" ADDED."), "OK", "green");
    }

    @Override
    public void onError(String message) {
        showSnackbar(message, "bad", "red");
    }
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editSearch.getWindowToken(), 0);
    }
}
