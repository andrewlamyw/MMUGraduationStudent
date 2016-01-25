package com.lamyatweng.mmugraduationstudent.Seat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SeatDisplayDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_display_detail);

        // Receive seat key from the Intent
        Intent intent = getIntent();
        final String seatKey = intent.getStringExtra(Constants.EXTRA_SEAT_KEY);

        // Get references of views
        final TextInputLayout sessionIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_id);
        final TextInputLayout seatRowWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_row);
        final TextInputLayout seatColumnWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_column);
        final TextInputLayout seatIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_id);
        final TextInputLayout seatStatusWrapper = (TextInputLayout) findViewById(R.id.wrapper_seat_status);
        final TextInputLayout studentIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_student_id);

        // Retrieve seat details from Firebase and display
        Firebase seatRef = new Firebase(Constants.FIREBASE_STRING_SEATS_REF);
        seatRef.child(seatKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Seat seat = dataSnapshot.getValue(Seat.class);
                // Null checking is required for handling removed item from Firebase
                if (seat != null) {
                    sessionIdWrapper.getEditText().setText(Integer.toString(seat.getSessionID()));
                    seatRowWrapper.getEditText().setText(seat.getRow());
                    seatColumnWrapper.getEditText().setText(seat.getColumn());
                    seatIdWrapper.getEditText().setText(Integer.toString(seat.getId()));
                    seatStatusWrapper.getEditText().setText(seat.getStatus());
                    studentIdWrapper.getEditText().setText(seat.getStudentID());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set Toolbar back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_SEAT + " Detail");
        // Close sMainActivity
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set menu edit button
        toolbar.inflateMenu(R.menu.seat_arrangement);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_EDIT:
                        Intent intent = new Intent(getApplicationContext(), SeatEditActivity.class);
                        intent.putExtra(Constants.EXTRA_SEAT_KEY, seatKey);
                        startActivity(intent);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
