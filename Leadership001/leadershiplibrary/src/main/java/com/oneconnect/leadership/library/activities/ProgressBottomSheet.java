package com.oneconnect.leadership.library.activities;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.oneconnect.leadership.library.R;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by aubreymalabie on 3/30/17.
 */

public class ProgressBottomSheet extends BottomSheetDialogFragment {

    private ProgressBar progressBar;
    private TextView txtPerc;
    private View view;
    private static final DecimalFormat df = new DecimalFormat("##0.00");
    public static final String TAG = ProgressBottomSheet.class.getSimpleName();


    public static ProgressBottomSheet newInstance() {
        ProgressBottomSheet f = new ProgressBottomSheet();
        return f;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ###############");
        view = inflater.inflate(R.layout.progress_bar, container, false);
        txtPerc = (TextView) view.findViewById(R.id.txtPercentage);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        return view;
    }

    public void onProgress(long transferred, long size) {
        Log.w(TAG, "onProgress: @@@@@@@@@@@@@@ transferred: " + transferred + " size: "+size );
        float percent = (float) transferred * 100 / size;
        if (txtPerc == null) {
            Log.e(TAG, "onProgress: txtPerc is NULL" );
            return;
        }
        txtPerc.setText(df.format(percent).concat(" %")
                .concat(" - ").concat(getMB(transferred)));
        progressBar.setProgress((int)percent);
    }

    private String getMB(long transferred) {
        BigDecimal bd = new BigDecimal(transferred);
        BigDecimal x = bd.divide(new BigDecimal(1024*1024));
        return df.format(x.doubleValue()).concat(" MB");
    }
}
