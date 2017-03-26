package com.oneconnect.leadership.admin.crud;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.oneconnect.leadership.admin.R;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by aubreyM on 14/12/17.
 */
public class LocalImageAdapter extends RecyclerView.Adapter<LocalImageAdapter.ImageViewHolder> {

    public interface RouteListener {
        void onGalleryClicked(LocalImage route);
        void onCameraClicked(LocalImage route);
        void onLandmarksClicked(LocalImage route);
        void onRouteSelected(LocalImage route);

    }

    private ImageListener mListener;
    private List<LocalImage> localImages;
    private Context ctx;
    public static final Locale locale = Locale.getDefault();
    public static final DecimalFormat sdf = new DecimalFormat("###,###,###,###,##0.0");


    public LocalImageAdapter(List<LocalImage> localImages, Context ctx, ImageListener listener) {
        this.localImages = localImages;
        this.mListener = listener;
        this.ctx = ctx;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image, parent, false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final LocalImage p = localImages.get(position);
        holder.image.setImageDrawable(ContextCompat.getDrawable(ctx,p.getResourceId()));
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onImageClicked(p);
            }
        });

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
        return localImages == null ? 0 : localImages.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;


        public ImageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }

    }

    public interface ImageListener {
        void onImageClicked(LocalImage image);
    }
    static final String LOG = LocalImageAdapter.class.getSimpleName();
}
