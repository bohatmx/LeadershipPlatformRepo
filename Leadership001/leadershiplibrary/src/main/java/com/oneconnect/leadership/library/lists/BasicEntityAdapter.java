package com.oneconnect.leadership.library.lists;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DTOEntity;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class BasicEntityAdapter extends RecyclerView.Adapter<BasicEntityAdapter.EntityViewHolder> {

    public interface EntityListener {
        void onDeleteClicked(DTOEntity entity);
        void onUpdateClicked(DTOEntity entity);
        void onPhotoCaptureRequested(DTOEntity entity);
        void onVideoCaptureRequested(DTOEntity entity);
        void onLocationRequested(DTOEntity entity);
        void onEntityClicked(DTOEntity entity);

    }

    private EntityListener mListener;
    private List<DTOEntity> entities;
    private Context ctx;
    boolean noIcons;
    public static final Locale locale = Locale.getDefault();
    public static final DecimalFormat sdf = new DecimalFormat("###,###,###,###,##0.0");


    public BasicEntityAdapter(List<DTOEntity> entities,
                              Context ctx, EntityListener listener) {
        this.entities = entities;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_item, parent, false);
        return new EntityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EntityViewHolder holder, final int position) {

        final DTOEntity p = entities.get(position);

    }

    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, 0.5f);
        an.setDuration(200);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                animateIn(view);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        an.start();

    }

    @Override
    public int getItemCount() {
        return entities == null ? 0 : entities.size();
    }

    public class EntityViewHolder extends RecyclerView.ViewHolder {
        protected TextView txtTitle, txtSubTitle, txtLine1, txtLine2;
        protected ImageView iconTopRight, icon1, icon2,icon3,icon4,icon5;
        protected View bottomLayout;


        public EntityViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtLine1 = (TextView) itemView.findViewById(R.id.txtLine1);
            txtLine2 = (TextView) itemView.findViewById(R.id.txtLine2);
            iconTopRight = (ImageView) itemView.findViewById(R.id.iconTopRight);

            icon1 = (ImageView) itemView.findViewById(R.id.icon1);
            icon2 = (ImageView) itemView.findViewById(R.id.icon2);
            icon3 = (ImageView) itemView.findViewById(R.id.icon3);
            icon4 = (ImageView) itemView.findViewById(R.id.icon4);
            icon5 = (ImageView) itemView.findViewById(R.id.icon5);

            bottomLayout = itemView.findViewById(R.id.bottomLayout);
        }

    }


    static final String LOG = BasicEntityAdapter.class.getSimpleName();
}
