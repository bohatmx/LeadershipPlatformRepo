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
        cvh.categoryText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCategorySelected(cat);
            }
        });
      /*  if (cat.getCategoryName().equalsIgnoreCase("Executive Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.woman_screens));
        } else if (cat.getCategoryName().equalsIgnoreCase("Managerial Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.tablet));
        } else if (cat.getCategoryName().equalsIgnoreCase("Supervisory Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.room_screen));
        } else if (cat.getCategoryName().equalsIgnoreCase("Education Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.light_bulb));
        } else if (cat.getCategoryName().equalsIgnoreCase("Global Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.laptop4));
        } else if (cat.getCategoryName().equalsIgnoreCase("Local Leadership")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.laptop3));
        } else if (cat.getCategoryName().equalsIgnoreCase("Business")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.planning));
        } else if (cat.getCategoryName().equalsIgnoreCase("Life")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.woman_trees));
        } else if (cat.getCategoryName().equalsIgnoreCase("Success")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.mercedes_interior));
        } else if (cat.getCategoryName().equalsIgnoreCase("Physical Health")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.surfer));
        } else if (cat.getCategoryName().equalsIgnoreCase("Love")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.cute_toddler));
        } else if (cat.getCategoryName().equalsIgnoreCase("Technology")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.tablet));
        } else if (cat.getCategoryName().equalsIgnoreCase("Fitness")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.veg_flowers));
        } else if (cat.getCategoryName().equalsIgnoreCase("Family")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.cute_toddler));
        } else if (cat.getCategoryName().equalsIgnoreCase("Social")) {
            cvh.homeImage.setImageDrawable(ctx.getDrawable(R.drawable.hand_signs));
        }*/
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
