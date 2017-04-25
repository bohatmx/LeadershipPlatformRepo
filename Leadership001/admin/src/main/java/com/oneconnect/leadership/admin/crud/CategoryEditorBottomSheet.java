package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.activities.SheetContract;
import com.oneconnect.leadership.library.activities.SheetPresenter;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class CategoryEditorBottomSheet extends BaseBottomSheet implements SheetContract.View {
    private CategoryDTO category;
    private TextInputEditText editName;
    private Button btn;


    static CategoryEditorBottomSheet newInstance(CategoryDTO category, int type) {
        CategoryEditorBottomSheet f = new CategoryEditorBottomSheet();
        Bundle args = new Bundle();
        args.putInt("type",type);
        if (category != null) {
            args.putSerializable("category",category);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = (CategoryDTO) getArguments().getSerializable("category");
        type = getArguments().getInt("type",0);
        presenter = new SheetPresenter(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.category_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        editName = (TextInputEditText) view.findViewById(R.id.editName);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        return view;
    }
    private void send() {
        if (TextUtils.isEmpty(editName.getText())) {
            editName.setError(getString(R.string.enter_category_name));
            return;
        }

        if (type == Constants.NEW_ENTITY) {
            category = new CategoryDTO();
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            category.setCompanyID(me.getCompanyID());
            category.setCompanyName(me.getCompanyName());
            category.setActive(true);
        }
        category.setCategoryName(editName.getText().toString());

         switch (type) {
             case Constants.NEW_ENTITY:
                 presenter.addEntity(category);
                 break;
             case Constants.UPDATE_ENTITY:

                 break;
             case Constants.DELETE_ENTITY:

                 break;
         }
    }


    @Override
    public void onEntityAdded(String key) {
        bottomSheetListener.onWorkDone(category);

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
}
