package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.BaseDTO;
import com.oneconnect.leadership.library.data.CategoryDTO;
import com.oneconnect.leadership.library.data.CompanyDTO;
import com.oneconnect.leadership.library.data.PhotoDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by nkululekochrisskosana on 2017/10/09.
 */

public class CompanyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CompanyDTO> mList;
    private Context ctx;
    private CompanyAdapterListener listener;

    public CompanyAdapter(List<CompanyDTO> mList, Context ctx, CompanyAdapterListener listener) {
        this.mList = mList;
        this.ctx = ctx;
        this.listener = listener;
    }

    public interface CompanyAdapterListener {
        void onCompanySelected(CompanyDTO company);
        void onPhotoRequired(BaseDTO base);
        void onChangeColor(CompanyDTO company);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.company, parent, false);
        return new CompanyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final CompanyDTO com = mList.get(position);
        final CompanyViewHolder cvh = (CompanyViewHolder) holder;

        cvh.companyTxt.setText(com.getCompanyName());

        cvh.companyIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onPhotoRequired(com);
            }
        });
        cvh.companyTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCompanySelected(com);
            }
        });

        if (com.getPrimaryColor() != 0) {
            cvh.color1.setColorFilter(com.getPrimaryColor());
        }
        if (com.getSecondaryColor() != 0) {
            cvh.color2.setColorFilter(com.getSecondaryColor());
        }
        cvh.color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangeColor(com);
            }
        });
        cvh.color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChangeColor(com);
            }
        });
        cvh.companyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCompanySelected(com);
            }
        });


        if (com.getPhotos() != null) {
            CompanyDTO dtd = mList.get(position);
            List<PhotoDTO> urlList = new ArrayList<>();

            Map map = dtd.getPhotos();
            PhotoDTO vDTO;
            String photoUrl;
            for (Object value : map.values()) {
                vDTO = (PhotoDTO) value;
                photoUrl = vDTO.getUrl();
                urlList.add(vDTO);

                Glide.with(ctx)
                        .load(photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(cvh.companyIMG);
            }
        }
    //    if (com.get)
        /*Map map = com.getPhotos();
        PhotoDTO vDTO;
        String photoUrl;
        for (Object value : map.values()) {
            vDTO = (PhotoDTO) value;
            photoUrl = vDTO.getUrl();
            urlList.add(vDTO);

            Glide.with(ctx)
                    .load(photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(cvh.companyIMG);
            //   dvh.captiontxt.setText(vDTO.getCaption());


        }*/
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CompanyViewHolder extends RecyclerView.ViewHolder {
        protected TextView companyTxt;
        protected ImageView companyIMG, color1, color2;
        protected CardView companyCard;

        public CompanyViewHolder(View itemView) {
            super(itemView);
            companyTxt = (TextView) itemView.findViewById(R.id.companyTxt);
            companyIMG = (ImageView) itemView.findViewById(R.id.companyIMG);
            color1 = (ImageView) itemView.findViewById(R.id.color1);
            color2 = (ImageView) itemView.findViewById(R.id.color2);
            companyCard = (CardView) itemView.findViewById(R.id.companyCard);
        }
    }
}
