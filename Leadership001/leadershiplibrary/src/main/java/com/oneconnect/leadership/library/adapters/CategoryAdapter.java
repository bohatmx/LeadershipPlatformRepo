package com.oneconnect.leadership.library.adapters;

import android.content.Context;
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
