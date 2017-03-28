package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.data.WeeklyMessageDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;
import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class WeeklyMessageEditor extends BaseBottomSheet
        implements SheetContract.View {
    private WeeklyMessageDTO weeklyMessage;
    private TextInputEditText editTitle, editSubtitle;
    private Button btnSend, btnDate;
    private Date selectedDate;

    @Override
    public void onEntityAdded(String key) {
        Log.i(TAG, "******* onEntityAdded: daily thought added to firebase "
                .concat(key));
        weeklyMessage.setWeeklyMessageID(key);
        bottomSheetListener.onWorkDone(weeklyMessage);
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


    static WeeklyMessageEditor newInstance(WeeklyMessageDTO weeklyMessage, int type) {
        WeeklyMessageEditor f = new WeeklyMessageEditor();
        Bundle args = new Bundle();
        args.putInt("type", type);
        if (weeklyMessage != null) {
            args.putSerializable("weeklyMessage", weeklyMessage);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weeklyMessage = (WeeklyMessageDTO) getArguments().getSerializable("weeklyMessage");
        type = getArguments().getInt("type", 0);
        presenter = new SheetPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.daily_thought_editor, container, false);
        btnSend = (Button) view.findViewById(R.id.btn);
        btnDate = (Button) view.findViewById(R.id.btnDate);
        editTitle = (TextInputEditText) view.findViewById(R.id.editTitle);
        editSubtitle = (TextInputEditText) view.findViewById(R.id.editSubtitle);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });
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
            editTitle.setError(getString(R.string.enter_subtitle));
            return;
        }
        Log.d(TAG, "send: @@@@@@@@@@@ starting to send weeky message to Firebase");
        if (weeklyMessage == null) {
            weeklyMessage = new WeeklyMessageDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            weeklyMessage.setCompanyID(me.getCompanyID());
            weeklyMessage.setCompanyName(me.getCompanyName());
            weeklyMessage.setActive(true);
            weeklyMessage.setJournalUserID(me.getUserID());
            weeklyMessage.setJournalUserName(me.getFullName());

        }
        if (selectedDate == null) {
            isReadyToSend = true;
            bottomSheetListener.onDateRequired();
            return;
        } else {
            weeklyMessage.setDateScheduled(selectedDate.getTime());
        }
        weeklyMessage.setTitle(editTitle.getText().toString());
        weeklyMessage.setSubtitle(editSubtitle.getText().toString());


        switch (type) {
            case Constants.NEW_ENTITY:
                Log.w(TAG, "...sending to Firebase: ".concat(GSON.toJson(weeklyMessage)));
                presenter.addEntity(weeklyMessage);
                break;
            case Constants.UPDATE_ENTITY:

                break;
            case Constants.DELETE_ENTITY:

                break;
        }
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = Util.getDateAtMidnite(selectedDate);
        btnDate.setText(sdf.format(this.selectedDate));
        if (weeklyMessage != null) {
            weeklyMessage.setDateScheduled(this.selectedDate.getTime());
            if (isReadyToSend) {
                isReadyToSend = false;
                send();
            }

        }
    }

    public static final String TAG = WeeklyMessageEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");

}
