package com.oneconnect.leadership.library.util;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.oneconnect.leadership.library.R;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public TimePickerFragment() {
        // Required empty public constructor
    }
    Context ctx;

    View view;
   // TimePicker timePicker;

    int pickerint;
    TimePickerDialog timePickerDialog;

    /*public Date getTime(Date selectedDate){
       *//* timePickerDialog = new TimePickerDialog(ctx,  new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            }
        });*//*
    //    int hour = timePicker.getCurrentHour(); // before api level 23
       /// int hours =simpleTimePicker.getHour();
    //    int  minute = timePicker.getCurrentMinute();
       // int minute = timePicker.getMinute();
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(selectedDate);
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal.getTime();
    }*/

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        /*int hour*/this.selectedHour = calendar.get(Calendar.HOUR);
        /*int minute*/this.selectedMinute = calendar.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, selectedHour, selectedMinute,
                DateFormat.is24HourFormat(getActivity()));

  //      getSetTime(calendar.getTime());
    }

    int selectedHour;
    int selectedMinute;
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar cal = GregorianCalendar.getInstance();
       // cal.setTime();
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        this.selectedHour = hourOfDay;
        this.selectedMinute = minute;
      //  return cal.getTime();

    }

    public Date getSetTime(Date selectedDate){
        Calendar cal = GregorianCalendar.getInstance();
         cal.setTime(selectedDate);
        cal.set(Calendar.HOUR, selectedHour);
        cal.set(Calendar.MINUTE, selectedMinute);
          return cal.getTime();
    }

}
