package com.oneconnect.leadership.library.editors;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.SheetContract;

import com.oneconnect.leadership.library.util.Util;

import java.text.SimpleDateFormat;


//import com.oneconnect.leadership.admin.R;

/**
 * Created by kurisani chauke on 12/09/17.
 */

public class MenuBottomEditor extends BaseBottomSheet implements SheetContract.View {

    private TextView editTitle, editSubtitle;


    @Override
    public void onEntityAdded(String key) {

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

    public static MenuBottomEditor newInstance() {
        MenuBottomEditor f = new MenuBottomEditor();
        Bundle args = new Bundle();
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.menu_editor, container, false);

        editTitle = (TextView) view.findViewById(R.id.editTitle);

        editTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(editTitle, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });
            }
        });

        editSubtitle = (TextView) view.findViewById(R.id.editSubtitle);

        editSubtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.flashOnce(editTitle, 300, new Util.UtilAnimationListener() {
                    @Override
                    public void onAnimationEnded() {

                    }
                });
            }
        });

        return view;
    }


    public static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE dd MMMM yyyy");
    public static final String TAG = MenuBottomEditor.class.getSimpleName();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

}
