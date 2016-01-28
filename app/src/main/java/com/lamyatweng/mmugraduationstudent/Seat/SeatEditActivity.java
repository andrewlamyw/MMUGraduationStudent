package com.lamyatweng.mmugraduationstudent.Seat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SeatEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_edit);

        // Receive seat key from the Intent
        Intent intent = getIntent();
        final String seatKey = intent.getStringExtra(Constants.EXTRA_SEAT_KEY);

        // Get references of views
        final TextInputLayout sessionIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_id);
        final TextInputLayout seatRowWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_row);
        final TextInputLayout seatColumnWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_column);
        final TextInputLayout seatIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_id);
        final TextInputLayout studentIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_student_id);

        // Populate seat statuses from array
        final Spinner seatStatusSpinner = (Spinner) findViewById(R.id.spinner_seat_status);
        final ArrayAdapter<CharSequence> seatStatusAdapter = ArrayAdapter.createFromResource(this,
                R.array.seat_status_array, android.R.layout.simple_spinner_item);
        seatStatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seatStatusSpinner.setAdapter(seatStatusAdapter);

        // Retrieve seat details from Firebase and display
        Constants.FIREBASE_REF_SEATS.child(seatKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Seat seat = dataSnapshot.getValue(Seat.class);
                // Null checking is required for handling removed item from Firebase
                if (seat != null) {
                    sessionIdWrapper.getEditText().setText(Integer.toString(seat.getSessionID()));
                    seatRowWrapper.getEditText().setText(seat.getRow());
                    seatColumnWrapper.getEditText().setText(seat.getColumn());
                    seatIdWrapper.getEditText().setText(Integer.toString(seat.getId()));
                    seatStatusSpinner.setSelection(seatStatusAdapter.getPosition(seat.getStatus()));
                    studentIdWrapper.getEditText().setText(seat.getStudentID());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set Toolbar close button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit " + Constants.TITLE_SEAT);
        // Close sMainActivity
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Commit: update programme information into Firebase
        toolbar.inflateMenu(R.menu.seat_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        final int id = Integer.parseInt(seatIdWrapper.getEditText().getText().toString());
                        final String row = seatRowWrapper.getEditText().getText().toString();
                        final String column = seatColumnWrapper.getEditText().getText().toString();
                        final String status = seatStatusSpinner.getSelectedItem().toString();
                        final int sessionID = Integer.parseInt(sessionIdWrapper.getEditText().getText().toString());
                        final String studentID = studentIdWrapper.getEditText().getText().toString();

                        // Replace old values with new values in Firebase
                        Seat updatedSeat = new Seat(id, row, column, status, sessionID, studentID);
                        Constants.FIREBASE_REF_SEATS.child(seatKey).setValue(updatedSeat);

                        // Display message and close dialog
                        Toast.makeText(getApplicationContext(), Constants.TITLE_SEAT + " updated.", Toast.LENGTH_LONG).show();
                        finish();

                    default:
                        return false;
                }
            }
        });
    }
}
