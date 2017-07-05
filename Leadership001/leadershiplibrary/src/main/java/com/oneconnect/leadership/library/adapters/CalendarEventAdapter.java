package com.oneconnect.leadership.library.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oneconnect.leadership.library.R;
import com.oneconnect.leadership.library.data.CalendarEventDTO;

import java.util.List;

/**
 * Created by Nkululeko on 2017/05/07.
 */

public class CalendarEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CalendarEventDTO> mList;
    private Context ctx;
    private CalendarEventAdapterlistener listener;

    public CalendarEventAdapter(List<CalendarEventDTO> mList, Context ctx) {
        this.mList = mList;
        this.ctx = ctx;
    }

    public interface CalendarEventAdapterlistener{
        void onCalendarEventClicked(CalendarEventDTO calanderEvent);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.calender_item, parent, false);
        return new CalendarEventViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CalendarEventDTO ce = mList.get(position);
        final CalendarEventViewHolder cevh = (CalendarEventViewHolder) holder;
        cevh.titletxt.setText(ce.getTitle());
        cevh.descriptiontxt.setText(ce.getDescription());
        cevh.startLabel.setText("Start Date: ");
        cevh.startDate.setText(ce.getStringStartDate());
        cevh.endLabel.setText("End Date: ");
        cevh.endDate.setText(ce.getStringEndDate());

        cevh.shareevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(ctx, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_calender, popup.getMenu());
        popup.setOnMenuItemClickListener(new CalendarEventAdapter.MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            int i = menuItem.getItemId();
            if (i == R.id.share) {
                shareIt();
                return true;
            }
            return false;
        }
    }
    private void shareIt() {
        //sharing implementation here
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Leadership Platform");
        String sAux = "\nLet me recommend you this application\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.minisass&hl=en \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        ctx.startActivity(Intent.createChooser(i, "choose one"));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public class CalendarEventViewHolder extends RecyclerView.ViewHolder {
        protected TextView titletxt, descriptiontxt, startLabel, startDate, endLabel, endDate;
        protected ImageView shareevent;

        public CalendarEventViewHolder(View itemView) {
            super(itemView);
            titletxt = (TextView) itemView.findViewById(R.id.titletxt);
            descriptiontxt = (TextView) itemView.findViewById(R.id.descriptiontxt);
            startLabel = (TextView) itemView.findViewById(R.id.startLabel);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            endLabel = (TextView) itemView.findViewById(R.id.endLabel);
            endDate = (TextView) itemView.findViewById(R.id.endDate);
            shareevent = (ImageView) itemView.findViewById(R.id.shareevent);

        }
    }
}
