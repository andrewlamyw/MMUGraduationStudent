package com.lamyatweng.mmugraduationstudent.Session;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.DatePickerFragment;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.TimePickerFragment;

import java.util.HashMap;
import java.util.Map;

public class SessionAddActivity extends AppCompatActivity {
    Bundle mBundle = new Bundle();
    int mConvocationYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_add);

        // Receive convocation year from previous sMainActivity
        Intent intent = getIntent();
        mConvocationYear = intent.getIntExtra(Constants.EXTRA_CONVOCATION_YEAR, -1);

        // Get references of views
        final TextInputLayout sessionNumberWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_number);
        final TextView dateTextView = (TextView) findViewById(R.id.session_date);
        final TextView startTimeTextView = (TextView) findViewById(R.id.session_start_time);
        final TextView endTimeTextView = (TextView) findViewById(R.id.session_end_time);

        // Open date picker and set text for session date
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                mBundle.putInt(getString(R.string.key_datePicker_textView_id), R.id.session_date);
                newFragment.setArguments(mBundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // Open time picker and set text for start time
        startTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                mBundle.putInt(getString(R.string.key_timePicker_textView_id), R.id.session_start_time);
                newFragment.setArguments(mBundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        // Open time picker and set text for end time
        endTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                mBundle.putInt(getString(R.string.key_timePicker_textView_id), R.id.session_end_time);
                newFragment.setArguments(mBundle);
                newFragment.show(getFragmentManager(), "timePicker");
            }
        });

        // Set Toolbar name and close button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New " + Constants.TITLE_SESSION);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set menu save button to add new session into Firebase
        toolbar.inflateMenu(R.menu.session_add);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        int convocationYear = mConvocationYear;
                        int sessionNumber = Integer.parseInt(sessionNumberWrapper.getEditText().getText().toString());
                        String date = dateTextView.getText().toString();
                        String startTime = startTimeTextView.getText().toString();
                        String endTime = endTimeTextView.getText().toString();
                        int rowSize = 0;
                        int columnSize = 0;
                        int id;
                        if (sessionNumber < 10) {
                            id = Integer.parseInt(Integer.toString(convocationYear) + "0" + Integer.toString(sessionNumber));
                        } else {
                            id = Integer.parseInt(Integer.toString(convocationYear) + Integer.toString(sessionNumber));
                        }
                        Map<String, Boolean> programmes = new HashMap<>();

                        // Push into Firebase programme list
                        ConvocationSession session = new ConvocationSession(columnSize, convocationYear, date, endTime, id, programmes, rowSize, sessionNumber, startTime);
                        Firebase sessionRef = new Firebase(Constants.FIREBASE_STRING_SESSIONS_REF);
                        sessionRef.push().setValue(session);

                        // Display message and close dialog
                        Toast.makeText(getApplicationContext(), Constants.TITLE_SESSION + " added.", Toast.LENGTH_LONG).show();
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
