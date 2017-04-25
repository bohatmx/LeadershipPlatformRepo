package com.oneconnect.leadership.library.activities;

import android.animation.ObjectAnimator;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.oneconnect.leadership.library.data.BaseDTO;


/**
 * Created by aubreymalabie on 3/18/17.
 */

public abstract class BaseBottomSheet extends BottomSheetDialogFragment {
    public interface BottomSheetListener {
        void onWorkDone(BaseDTO entity);
        void onDateRequired();
        void onError(String message);
    }

    public SheetPresenter presenter;
    public BottomSheetListener bottomSheetListener;
    public View view;
    public int type;


    public void setBottomSheetListener(BottomSheetListener bottomSheetListener) {
        this.bottomSheetListener = bottomSheetListener;
    }


    public void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

}
