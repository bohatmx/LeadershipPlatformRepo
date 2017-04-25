package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.DailyThoughtDTO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Nkululeko on 2017/04/07.
 */

public class DailyThoughtAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<DailyThoughtDTO> mList;
    private Context ctx;
    private DailyThoughtAdapterlistener listener;

    public interface DailyThoughtAdapterlistener{
        void onThoughtClicked(int position);

    }

    public DailyThoughtAdapter(Context ctx, List<DailyThoughtDTO> mList) {
        this.ctx = ctx;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_item, parent, false);
        return new DailyViewHolder(v);
    }

    static final SimpleDateFormat sd1 = new SimpleDateFormat(" dd-MM-yyyy HH:mm");

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final DailyThoughtDTO dt = mList.get(position);
        final DailyViewHolder dvh = (DailyViewHolder) holder;
        dvh.txtTitle.setText(dt.getTitle());
        dvh.txtSubtitle.setText(dt.getSubtitle());
        Date d = new Date(dt.getDateScheduled());
        dvh.txtDate.setText("" + sd1.format(d));


    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class DailyViewHolder extends RecyclerView.ViewHolder {
        protected TextView /*txtNumberRed,*/ txtEvents, txtTitle, txtDate, txtSubtitle;
        protected ImageView iconCalendar;
        protected RelativeLayout bottomLayout;
        protected LinearLayout iconLayout;

        public DailyViewHolder(View itemView) {
            super(itemView);
            /*txtNumberRed = (TextView) itemView.findViewById(R.id.txtNumberRed);*/
            txtEvents = (TextView) itemView.findViewById(R.id.txtEvents);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            txtSubtitle = (TextView) itemView.findViewById(R.id.txtSubtitle);
            iconCalendar = (ImageView) itemView.findViewById(R.id.iconCalendar);
            bottomLayout = (RelativeLayout) itemView.findViewById(R.id.bottomLayout);
            iconLayout = (LinearLayout) itemView.findViewById(R.id.iconLayout);




        }
    }
}