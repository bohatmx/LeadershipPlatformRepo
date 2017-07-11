package com.oneconnect.leadership.library.util;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Nkululeko on 2017/07/11.
 */

public class MySpannable extends ClickableSpan{

    private boolean isUnderline = true;

    public MySpannable(boolean isUnderline) {
        this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isUnderline);
    }

    @Override
    public void onClick(View widget) {

    }
}
