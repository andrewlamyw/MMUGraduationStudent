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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.DatePickerFragment;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.TimePickerFragment;

import java.util.HashMap;
import java.util.Map;

public class SessionEditActivity extends AppCompatActivity {
    static ValueEventListener mSessionListener;
    // Pass text view id to date/time picker
    Bundle mBundle = new Bundle();
    Map<String, Boolean> mProgrammes = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_edit);

        // Receive convocation key from the Intent
        Intent intent = getIntent();
        final String sessionKey = intent.getStringExtra(Constants.EXTRA_SESSION_KEY);

        // Get references of views
        final TextInputLayout convocationYearWrapper = (TextInputLayout) findViewById(R.id.wrapper_convocation_year);
        final TextInputLayout sessionNumberWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_number);
        final TextInputLayout sessionIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_id);
        final TextView dateTextView = (TextView) findViewById(R.id.session_date);
        final TextView startTimeTextView = (TextView) findViewById(R.id.session_start_time);
        final TextView endTimeTextView = (TextView) findViewById(R.id.session_end_time);
        final TextInputLayout rowSizeWrapper = (TextInputLayout) findViewById(R.id.wrapper_row_size);
        final TextInputLayout columnSizeWrapper = (TextInputLayout) findViewById(R.id.wrapper_column_size);

        // Retrieve session details from Firebase and display
        final Firebase sessionRef = new Firebase(Constants.FIREBASE_STRING_SESSIONS_REF);
        mSessionListener = sessionRef.child(sessionKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConvocationSession session = dataSnapshot.getValue(ConvocationSession.class);
                // Null checking is required for handling removed item from Firebase
                if (session != null) {
                    convocationYearWrapper.getEditText().setText(Integer.toString(session.getConvocationYear()));
                    sessionNumberWrapper.getEditText().setText(Integer.toString(session.getSessionNumber()));
                    sessionIdWrapper.getEditText().setText(Integer.toString(session.getId()));
                    dateTextView.setText(session.getDate());
                    startTimeTextView.setText(session.getStartTime());
                    endTimeTextView.setText(session.getEndTime());
                    rowSizeWrapper.getEditText().setText(Integer.toString(session.getRowSize()));
                    columnSizeWrapper.getEditText().setText(Integer.toString(session.getColumnSize()));
                    mProgrammes = session.getProgrammes();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

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
        toolbar.setTitle("Edit " + Constants.TITLE_SESSION);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set menu save button to add new session into Firebase
        toolbar.inflateMenu(R.menu.session_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        int convocationYear = Integer.parseInt(convocationYearWrapper.getEditText().getText().toString());
                        int sessionNumber = Integer.parseInt(sessionNumberWrapper.getEditText().getText().toString());
                        int id = Integer.parseInt(sessionIdWrapper.getEditText().getText().toString());
                        String date = dateTextView.getText().toString();
                        String startTime = startTimeTextView.getText().toString();
                        String endTime = endTimeTextView.getText().toString();
                        int rowSize = Integer.parseInt(rowSizeWrapper.getEditText().getText().toString());
                        int columnSize = Integer.parseInt(columnSizeWrapper.getEditText().getText().toString());

                        // Push into Firebase programme list
                        ConvocationSession session = new ConvocationSession(columnSize, convocationYear, date, endTime, id, mProgrammes, rowSize, sessionNumber, startTime);
                        sessionRef.child(sessionKey).setValue(session);

                        // Display message and close dialog
                        Toast.makeText(getApplicationContext(), Constants.TITLE_SESSION + " updated.", Toast.LENGTH_LONG).show();
                        sessionRef.removeEventListener(mSessionListener);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
