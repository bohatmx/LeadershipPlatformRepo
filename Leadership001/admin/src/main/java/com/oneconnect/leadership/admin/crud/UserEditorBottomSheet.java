package com.oneconnect.leadership.admin.crud;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.oneconnect.leadership.admin.R;
import com.oneconnect.leadership.library.activities.BaseBottomSheet;
import com.oneconnect.leadership.library.crud.CrudPresenter;
import com.oneconnect.leadership.library.data.UserDTO;
import com.oneconnect.leadership.library.util.Constants;
import com.oneconnect.leadership.library.util.SharedPrefUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreymalabie on 3/18/17.
 */

public class UserEditorBottomSheet extends BaseBottomSheet {
    private UserDTO user;
    private TextInputEditText eFirst,eLast,eMail;
    private Spinner spinner;
    private Button btn;
    private CrudPresenter crudPresenter;


    static UserEditorBottomSheet newInstance(UserDTO user, int type) {
        UserEditorBottomSheet f = new UserEditorBottomSheet();
        Bundle args = new Bundle();
        args.putInt("type",type);
        if (user != null) {
            args.putSerializable("user",user);
        }
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = (UserDTO) getArguments().getSerializable("user");
        type = getArguments().getInt("type",0);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.user_editor, container, false);
        btn = (Button) view.findViewById(R.id.btn);
        eFirst = (TextInputEditText) view.findViewById(R.id.editFirstName);
        eLast = (TextInputEditText) view.findViewById(R.id.editLastName);
        eMail = (TextInputEditText) view.findViewById(R.id.editEmail);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send();
            }
        });

        setSpinner();
        return view;
    }
    private void send() {
        if (TextUtils.isEmpty(eFirst.getText())) {
            eFirst.setError("Enter first name");
            return;
        }
        if (TextUtils.isEmpty(eLast.getText())) {
            eLast.setError("Enter last name");
            return;
        }
        if (TextUtils.isEmpty(eMail.getText())) {
            eMail.setError("Enter email address");
            return;
        }
        if (type == Constants.NEW_ENTITY) {
            user = new UserDTO();
            user.setEmail(eMail.getText().toString());
            UserDTO me = SharedPrefUtil.getUser(getActivity());
            user.setCompanyID(me.getCompanyID());
            user.setCompanyName(me.getCompanyName());
            user.setUserType(userType);
        }

        if(type == Constants.UPDATE_ENTITY){



        }

        if(type == Constants.DELETE_ENTITY){



        }

        user.setFirstName(eFirst.getText().toString());
        user.setLastName(eLast.getText().toString());

         switch (type) {
             case Constants.NEW_ENTITY:
                 crudPresenter.createUser(user);
                 bottomSheetListener.onWorkDone(user);
                 break;
             case Constants.UPDATE_ENTITY:
             //       crudPresenter.updateUser(user);
             //    bottomSheetListener.onWorkDone(user);
                 break;
             case Constants.DELETE_ENTITY:

                 break;
         }
    }
    int userType;

    public void setCrudPresenter(CrudPresenter crudPresenter) {
        this.crudPresenter = crudPresenter;
    }
    private void setSpinner() {
        List<String> list = new ArrayList<>();
        list.add("Select User Type");
        list.add(UserDTO.DESC_STAFF);
        list.add(UserDTO.DESC_SUBSCRIBER);
        list.add(UserDTO.DESC_LEADER);
        list.add(UserDTO.DESC_GOLD);
        list.add(UserDTO.DESC_PLATINUM);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_list_item_1,list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        userType = 0;
                        break;
                    case 1:
                        userType = UserDTO.COMPANY_STAFF;
                        break;
                    case 2:
                        userType = UserDTO.SUBSCRIBER;
                        break;
                    case 3:
                        userType = UserDTO.LEADER;
                        break;
                    case 4:
                        userType = UserDTO.GOLD;
                        break;
                    case 5:
                        userType = UserDTO.PLATINUM;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


}
