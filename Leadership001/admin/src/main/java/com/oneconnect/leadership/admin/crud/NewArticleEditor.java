package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.SheetContract;
import com.oneconnect.leadership.library.activities.SheetPresenter;
import com.oneconnect.leadership.library.data.NewsDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Kurisani on 2017/06/26.
 */

public class NewArticleEditor extends BaseBottomSheet implements SheetContract.View {
    private NewsDTO article;
    private TextInputEditText editTitle, editSubtitle;
    private RecyclerView recyclerView;
    private ImageView iconCamera, iconVideo, iconDate, iconURLs;
    private View iconLayout;
    private Button btn, btnDate;
    private Date selectedDate;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: news article added to firebase "
                .concat(key));
        article.setNewsID(key);
        bottomSheetListener.onWorkDone(article);
        this.dismiss();
    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onEntityDeleted() {

    }

    @Override
    public void onError(String message) {
        bottomSheetListener.onError(message);
    }

    static NewArticleEditor newInstance(NewsDTO article, int type) {
        NewArticleEditor f = new NewArticleEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (article != null) {
            args.putSerializable("article", article);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        article = (NewsDTO) getArguments().getSerializable("dailyThought");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.new_article_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send();
            }
        });
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);
        editSubtitle.setHint("Enter News");
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetListener.onDateRequired();
            }
        });



        return view;
    }

    private boolean isReadyToSend;

    private void send() {
        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError(getString(R.string.enter_title));
            return;
        }
        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editTitle.setError(getString(R.string.enter_news));
            return;
        }
        Log.d(TAG, "send: @@@@@@@@@@@ starting to send daily thought to Firebase");
        if (article == null) {
            article = new NewsDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            article.setCompanyID(me.getCompanyID());
            article.setCompanyName(me.getCompanyName());
            //article.setActive(true);
            article.setJournalUserID(me.getUserID());
            article.setJournalUserName(me.getFullName());

        }
        if (selectedDate == null) {
            isReadyToSend = true;
            bottomSheetListener.onDateRequired();
            return;
        } else {
            article.setDateScheduled(selectedDate.getTime());
        }
        article.setTitle(editTitle.getText().toString());
        article.setSubtitle(editSubtitle.getText().toString());


        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(article)));
                presenter.addEntity(article);
                break;
            case Constants.UPDATE_ENTITY:

                break;
            case Constants.DELETE_ENTITY:

                break;
        }
    }

    public void setSelectedDate(Date selectedDate) {

        this.selectedDate  = Util.getDateAtMidnite(selectedDate);
        btnDate.setText(sdf.format(this.selectedDate));
        if (article != null) {
            article.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }


    }
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
    public static final String TAG = NewArticleEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
}