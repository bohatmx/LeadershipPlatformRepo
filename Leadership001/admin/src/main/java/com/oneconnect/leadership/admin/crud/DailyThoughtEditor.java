package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.LinearLayoutManager;
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
import com.oneconnect.leadership.library.data.DailyThoughtDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class DailyThoughtEditor extends BaseBottomSheet implements SheetContract.View {
    private DailyThoughtDTO dailyThought;
    ;

    private TextInputEditText editTitle, editSubtitle;
    private RecyclerView recyclerView;
    private ImageView iconCamera, iconVideo, iconDate, iconURLs;
    private View iconLayout;
    private Button btn;
    private Date selectedDate;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "onEntityAdded: ".concat(key));
        dailyThought.setDailyThoughtID(key);
        bottomSheetListener.onWorkDone(dailyThought);
    }

    @Override
    public void onEntityUpdated() {

    }

    @Override
    public void onEntityDeleted() {

    }

    @Override
    public void onError(String message) {

    }

    public interface IconListener {
        void onCameraIconClicked();

        void onVideoIconClicked();

        void onDateIconClicked();

        void onURLIconClicked();

        void onLocalImageClicked(LocalImage image);
    }

    private IconListener iconListener;

    public void setIconListener(IconListener iconListener) {
        this.iconListener = iconListener;
    }

    static DailyThoughtEditor newInstance(DailyThoughtDTO dailyThought, int type) {
        DailyThoughtEditor f = new DailyThoughtEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (dailyThought != null) {
            args.putSerializable("dailyThought", dailyThought);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dailyThought = (DailyThoughtDTO) getArguments().getSerializable("dailyThought");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.daily_thought_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);
        iconCamera = (ImageView) view.findViewById(R.id.iconCamera);
        iconVideo = (ImageView) view.findViewById(R.id.iconVideo);
        iconDate = (ImageView) view.findViewById(R.id.iconDate);
        iconURLs = (ImageView) view.findViewById(R.id.iconURL);
        iconLayout = view.findViewById(R.id.iconLayout);
        iconLayout.setVisibility(View.GONE);
        iconCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: iconCamera clicked");
                iconListener.onCameraIconClicked();
            }
        });
        iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: iconVideo clicked");
                iconListener.onVideoIconClicked();
            }
        });
        iconDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: iconDate clicked");
                iconListener.onDateIconClicked();
            }
        });
        iconURLs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: iconUrl clicked");
                iconListener.onURLIconClicked();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(lm);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVisibility(View.GONE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        buildImages();
        return view;
    }

    private List<LocalImage> images = new ArrayList<>();
    private LocalImage localImage;
    LocalImageAdapter localImageAdapter;

    private void buildImages() {
        images.add(new LocalImage(R.drawable.image1));
        images.add(new LocalImage(R.drawable.image2));
        images.add(new LocalImage(R.drawable.image3));
        images.add(new LocalImage(R.drawable.image4));
        images.add(new LocalImage(R.drawable.image5));
        images.add(new LocalImage(R.drawable.image6));
        images.add(new LocalImage(R.drawable.image7));
        images.add(new LocalImage(R.drawable.image8));
        images.add(new LocalImage(R.drawable.image9));
        images.add(new LocalImage(R.drawable.image10));
        images.add(new LocalImage(R.drawable.image11));
        images.add(new LocalImage(R.drawable.image12));
        images.add(new LocalImage(R.drawable.image13));
        images.add(new LocalImage(R.drawable.image14));
        images.add(new LocalImage(R.drawable.image15));
        images.add(new LocalImage(R.drawable.image16));
        images.add(new LocalImage(R.drawable.image17));
        images.add(new LocalImage(R.drawable.image18));
        images.add(new LocalImage(R.drawable.image19));
        images.add(new LocalImage(R.drawable.image20));
        localImageAdapter = new LocalImageAdapter(images, getActivity(), new LocalImageAdapter.ImageListener() {
            @Override
            public void onImageClicked(LocalImage image) {
                localImage = image;
                Log.e(TAG, "################### onImageClicked: " + image.getResourceId());
                iconListener.onLocalImageClicked(localImage);
            }
        });
        recyclerView.setAdapter(localImageAdapter);

    }

    private boolean isReadyToSend;

    private void send() {
        if (TextUtils.isEmpty(editTitle.getText())) {
            editTitle.setError(getString(R.string.enter_title));
            return;
        }
        if (TextUtils.isEmpty(editSubtitle.getText())) {
            editTitle.setError(getString(R.string.enter_subtitle));
            return;
        }
        Log.d(TAG, "send: starting to send thought to Firebase");
        if (dailyThought == null) {
            dailyThought = new DailyThoughtDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            dailyThought.setCompanyID(me.getCompanyID());
            dailyThought.setCompanyName(me.getCompanyName());
            dailyThought.setActive(true);
            dailyThought.setJournalUserID(me.getUserID());
            dailyThought.setJournalUserName(me.getFullName());

        }
        if (selectedDate == null) {
            isReadyToSend = true;
            iconListener.onDateIconClicked();
            return;
        } else {
            dailyThought.setDateScheduled(selectedDate.getTime());
        }
        dailyThought.setTitle(editTitle.getText().toString());
        dailyThought.setSubtitle(editSubtitle.getText().toString());


        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "send to Firebase: ".concat(GSON.toJson(dailyThought)));
                presenter.addEntity(dailyThought);
                break;
            case Constants.UPDATE_ENTITY:

                break;
            case Constants.DELETE_ENTITY:

                break;
        }
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
        Log.w(TAG, "***** setSelectedDate: ".concat(selectedDate.toString()));
        if (dailyThought != null) {
            dailyThought.setDateScheduled(selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }
    }

    public static final String TAG = DailyThoughtEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
