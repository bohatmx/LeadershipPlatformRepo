package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.CategoryDTO;

import java.util.List;

/**
 * Created by Kurisani on 2017/08/21.
 */

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CategoryDTO> mList;
    private Context ctx;
    private CategoryAdapterListener listener;


    public CategoryAdapter(List<CategoryDTO> mList, Context ctx, CategoryAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    public interface CategoryAdapterListener {
        void onCategorySelected(CategoryDTO category);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.landing_adapter, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final CategoryDTO cat = mList.get(position);
        final CategoryViewHolder cvh = (CategoryViewHolder) holder;

        cvh.categoryText.setText(cat.getCategoryName());
        cvh.homeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCategorySelected(cat);
            }
        });
        cvh.categoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCategorySelected(cat);
            }
        });
        if (cat.getCategoryName().equalsIgnoreCase("Leadership")) {
            cvh.homeImage.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.leadership001)/*ctx.getDrawable(R.drawable.leadership001)*/);
        }  else if (cat.getCategoryName().equalsIgnoreCase("Business")) {
            cvh.homeImage.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.business)/*ctx.getDrawable(R.drawable.business)*/);
        }  else if (cat.getCategoryName().equalsIgnoreCase("Family")) {
            cvh.homeImage.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.family)/*ctx.getDrawable(R.drawable.family)*/);
        } else if (cat.getCategoryName().equalsIgnoreCase("Social")) {
            cvh.homeImage.setImageDrawable(ContextCompat.getDrawable(ctx, R.drawable.social)/*ctx.getDrawable(R.drawable.social)*/);
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        protected TextView categoryText;
        protected ImageView homeImage;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            categoryText = (TextView) itemView.findViewById(R.id.categoryText);
            homeImage = (ImageView) itemView.findViewById(R.id.homeImage);
        }
    }
}
