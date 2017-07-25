package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.RatingDTO;
import com.oneconnect.leadership.library.util.Util;

import java.util.List;

/**
 * Created by Kurisani on 2017/07/14.
 */

public class RatingReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<RatingDTO> mList;
    private Context ctx;
    private RatingAdapterlistener listener;

    public interface RatingAdapterlistener{
        void addListenerOnButton();
        void addListenerOnRatingBar();
    }
    public RatingReviewAdapter(Context ctx, List<RatingDTO> mList, RatingAdapterlistener listener) {
        this.ctx = ctx;
        this.mList = mList;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new RatingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final RatingDTO dt = mList.get(position);
        final RatingViewHolder dvh = (RatingViewHolder) holder;
        if(dt.getUserName() != null){
            dvh.username.setText(dt.getUserName());
        } else{
            dvh.username.setVisibility(View.GONE);
        }
        if (dt.getComment() != null) {
            dvh.txtComment.setText(dt.getComment());
        } else {
            dvh.txtComment.setVisibility(View.GONE);
        }

       /* StringBuilder sb = new StringBuilder(dt.getStringDate());
        sb.deleteCharAt(sb.indexOf(","));
        long miliSecs = Util.getMiliseconds(sb.toString());
        String formatedDate = Util.getFormattedDate(miliSecs); */
        //dvh.txtDate.setText(formatedDate);
        dvh.txtDate.setText(dt.getStringDate());
        dvh.ratingbar.setRating(dt.getRating());
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
    public class RatingViewHolder extends RecyclerView.ViewHolder {
        protected TextView username,txtComment,txtDate;
        protected RecyclerView imageRecyclerView;
        protected RatingBar ratingbar;

        public RatingViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.username);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            ratingbar = (RatingBar) itemView.findViewById(R.id.framework_normal_ratingbar);
        }
    }
    static final String LOG = RatingReviewAdapter.class.getSimpleName();
}
