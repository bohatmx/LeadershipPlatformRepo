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
import com.oneconnect.leadership.library.data.ResponseBag;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class BasicEntityAdapter extends RecyclerView.Adapter<BasicEntityAdapter.EntityViewHolder> {

    public interface EntityListener {
        void onAddEntity();

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
    int type;
    public static final Locale locale = Locale.getDefault();
    public static final DecimalFormat sdf = new DecimalFormat("###,###,###,###,##0.0");


    public BasicEntityAdapter(List<DTOEntity> entities, int type,
                              Context ctx, EntityListener listener) {
        this.entities = entities;
        this.mListener = listener;
        this.ctx = ctx;
        this.type = type;
    }

    @Override
    public EntityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.entity_item, parent, false);
        return new EntityViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EntityViewHolder h, final int position) {

        final DTOEntity p = entities.get(position);
        h.txtTitle.setText(p.getTitleForAdapter());
        h.txtLine1.setText(p.getTopText());
        h.txtSubTitle.setText(p.getBottomTitle());
        h.txtLine2.setText(p.getBottomText());
        h.txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout, p);
                } else {
                    animateOut(h.bottomLayout,p);
                }
            }
        });
        h.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteClicked(p);
            }
        });
        h.iconUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onUpdateClicked(p);
            }
        });
        h.iconLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onLocationRequested(p);
            }
        });
        h.iconPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onPhotoCaptureRequested(p);
            }
        });
        h.iconVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onVideoCaptureRequested(p);
            }
        });
        h.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (h.bottomLayout.getVisibility() == View.GONE) {
                    animateIn(h.bottomLayout, p);
                } else {
                    animateOut(h.bottomLayout,p);
                }
            }
        });

        switch (type) {
            case ResponseBag.CATEGORIES:
                h.txtNumBlack.setVisibility(View.VISIBLE);
                h.txtNumBlack.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.COMPANY:
                h.txtNumBlue.setVisibility(View.VISIBLE);
                h.txtNumBlue.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.COUNTRY:
                h.txtNumGrey.setVisibility(View.VISIBLE);
                h.txtNumGrey.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.DAILY_THOUGHTS:
                h.txtNumRed.setVisibility(View.VISIBLE);
                h.txtNumRed.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.EBOOKS:
                h.txtNumGreen.setVisibility(View.VISIBLE);
                h.txtNumGreen.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.NEWS:
                h.txtNumRed.setVisibility(View.VISIBLE);
                h.txtNumRed.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.PAYMENTS:
                h.txtNumBlack.setVisibility(View.VISIBLE);
                h.txtNumBlack.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.PHOTOS:
                h.txtNumBlack.setVisibility(View.VISIBLE);
                h.txtNumBlack.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.PODCASTS:
                h.txtNumGreen.setVisibility(View.VISIBLE);
                h.txtNumGreen.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.PRICE:
                h.txtNumBlue.setVisibility(View.VISIBLE);
                h.txtNumBlue.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.SUBSCRIPTIONS:
                h.txtNumGreen.setVisibility(View.VISIBLE);
                h.txtNumGreen.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.USERS:
                h.txtNumBlack.setVisibility(View.VISIBLE);
                h.txtNumBlack.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.VIDEOS:
                h.txtNumBlue.setVisibility(View.VISIBLE);
                h.txtNumBlue.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.WEEKLY_MASTERCLASS:
                h.txtNumGreen.setVisibility(View.VISIBLE);
                h.txtNumGreen.setText(String.valueOf(position + 1));
                break;
            case ResponseBag.WEEKLY_MESSAGE:
                h.txtNumGrey.setVisibility(View.VISIBLE);
                h.txtNumGrey.setText(String.valueOf(position + 1));
                break;
        }

    }

    private void animateIn(View view, final DTOEntity entity) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                  mListener.onEntityClicked(entity);
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

    private void animateOut(final View view, final DTOEntity entity) {
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
                mListener.onEntityClicked(entity);
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
        protected TextView txtTitle, txtSubTitle, txtLine1, txtLine2,
        txtNumBlack,txtNumBlue,txtNumGrey,txtNumGreen,txtNumRed;
        protected ImageView iconTopRight, iconDelete, iconUpdate, iconPhoto, iconVideo, iconLocation;
        protected View bottomLayout, frameLayout;


        public EntityViewHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtSubTitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            txtLine1 = (TextView) itemView.findViewById(R.id.txtLine1);
            txtLine2 = (TextView) itemView.findViewById(R.id.txtLine2);
            txtNumBlack = (TextView) itemView.findViewById(R.id.txtNumberBlack);
            txtNumBlue = (TextView) itemView.findViewById(R.id.txtNumberBlue);
            txtNumGreen = (TextView) itemView.findViewById(R.id.txtNumberGreen);
            txtNumGrey = (TextView) itemView.findViewById(R.id.txtNumberGrey);
            txtNumRed = (TextView) itemView.findViewById(R.id.txtNumberRed);

            iconTopRight = (ImageView) itemView.findViewById(R.id.iconTopRight);

            iconDelete = (ImageView) itemView.findViewById(R.id.iconDelete);
            iconUpdate = (ImageView) itemView.findViewById(R.id.iconUpdate);
            iconPhoto = (ImageView) itemView.findViewById(R.id.iconCamera);
            iconVideo = (ImageView) itemView.findViewById(R.id.iconVideo);
            iconLocation = (ImageView) itemView.findViewById(R.id.iconLocation);

            frameLayout = itemView.findViewById(R.id.frame);
            bottomLayout = itemView.findViewById(R.id.bottomLayout);
            txtNumRed.setVisibility(View.GONE);
            txtNumGrey.setVisibility(View.GONE);
            txtNumGreen.setVisibility(View.GONE);
            txtNumBlue.setVisibility(View.GONE);
            txtNumBlack.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }

    }


    private void hide(View itemView) {

    }
    static final String LOG = BasicEntityAdapter.class.getSimpleName();
}
