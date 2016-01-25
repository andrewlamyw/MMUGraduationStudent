package com.lamyatweng.mmugraduationstudent;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener{
    Bundle mBundle = new Bundle();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Retrieve studentKey from previous fragment
        mBundle = getArguments();
        int textViewId = mBundle.getInt(getString(R.string.key_timePicker_textView_id));

        // Create Date object based on date selection by user
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        Date date = calendar.getTime();

        // Update TextView with the date chosen by the user
        TextView textView = (TextView) getActivity().findViewById(textViewId);
        textView.setText(java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(date));
    }
}
