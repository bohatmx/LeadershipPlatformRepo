package com.oneconnect.leadership.library.lists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by aubreymalabie on 3/17/17.
 */

public abstract class BaseListingFragment extends Fragment {

    public BasicEntityAdapter.EntityListener mListener;
    public View view;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BasicEntityAdapter.EntityListener) {
            mListener = (BasicEntityAdapter.EntityListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
