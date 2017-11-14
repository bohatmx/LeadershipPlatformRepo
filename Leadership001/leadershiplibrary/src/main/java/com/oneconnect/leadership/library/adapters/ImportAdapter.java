package com.oneconnect.leadership.library.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;

import java.util.List;
import java.util.Locale;

/**
 * Created by nkululekochrisskosana on 2017/11/13.
 */

public class ImportAdapter extends RecyclerView.Adapter<ImportAdapter.ImportViewHolder> {


    private List<String> list;
    public static final Locale locale = Locale.getDefault();
    static final String TAG = ImportAdapter.class.getSimpleName();
    boolean openFirstItem;

    public interface ImportListener {
        void onRemoveRequired(String name, int position);
    }

    ImportListener listener;

    public ImportAdapter(List<String> list, ImportListener listener) {
        this.listener = listener;
        this.list = list;
    }

    @Override
    public ImportViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_import_item, parent, false);
        return new ImportViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ImportViewHolder holder, final int position) {

        final String m = list.get(position);
        holder.name.setText(m);
        holder.number.setText("" + (position + 1));
        animateOut(holder.number);

        holder.iconDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRemoveRequired(m,position);
            }
        });

    }

    private void animateIn(View view) {
        view.setVisibility(View.VISIBLE);
        view.setPivotY(0);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 0, 1);
        an.setDuration(300);
        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.start();
    }

    private void animateOut(final View view) {
        view.setPivotY(0f);
        ObjectAnimator an = ObjectAnimator.ofFloat(view, "scaleY", 1, 0);
        an.setDuration(300);

        an.setInterpolator(new AccelerateDecelerateInterpolator());
        an.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                view.setVisibility(View.GONE);
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
        return list == null ? 0 : list.size();
    }


    public class ImportViewHolder extends RecyclerView.ViewHolder {
        protected TextView name, number;
        protected ImageView iconDelete;


        public ImportViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            iconDelete = itemView.findViewById(R.id.iconRemove);
        }
    }
}
