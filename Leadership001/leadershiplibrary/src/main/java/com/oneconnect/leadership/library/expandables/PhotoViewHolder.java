package com.oneconnect.leadership.library.expandables;

import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.PhotoDTO;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

/**
 * Created by Nkululeko on 2017/05/09.
 */

public class PhotoViewHolder /*extends GroupViewHolder*/{

    private TextView captiontxt;
    private ImageView photoView;



    public PhotoViewHolder(View itemView) {
       // super(itemView);
        captiontxt = (TextView) itemView.findViewById(R.id.captiontxt);
        photoView = (ImageView) itemView.findViewById(R.id.photoView);
    }
    public void setPhotocaption(/*ExpandableGroup genre*/) {
    }

    /*@Override
    public void expand() {
        animateExpand();
    }

    @Override
    public void collapse() {
        animateCollapse();
    }

    private void animateExpand() {
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        photoView.setAnimation(rotate);
    }*/

    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        photoView.setAnimation(rotate);
    }
}
