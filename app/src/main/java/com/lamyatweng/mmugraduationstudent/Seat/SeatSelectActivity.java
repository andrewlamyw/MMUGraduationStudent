package com.lamyatweng.mmugraduationstudent.Seat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.Convocation.ConvocationPaymentActivity;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Session.ConvocationSession;
import com.lamyatweng.mmugraduationstudent.SessionProgramme;

public class SeatSelectActivity extends AppCompatActivity {

    final int GRID_COLUMN_WIDTH_IN_DP = 24;
    GridView mGridView;
    int mNumOfSelected = 0;
    int mNumOfGuest = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_select);

        String convoYear = "2017";
        String programmeName = "Bachelor of Computer Science (Honours)";
        final String faculty = "Faculty of Computing and Informatics";

        // Get session id based on programme of student
        Query sessionProgrammeQuery = Constants.FIREBASE_REF_SESSIONPROGRAMMES.child(convoYear).orderByChild(Constants.FIREBASE_ATTR_SESSIONPROGRAMMES_NAME).equalTo(programmeName);
        sessionProgrammeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionProgrammeSnapshot : dataSnapshot.getChildren()) {
                    SessionProgramme sessionProgramme = sessionProgrammeSnapshot.getValue(SessionProgramme.class);
                    if (sessionProgramme.getFaculty().equals(faculty)) {
                        int sessionId = sessionProgramme.getSessionID();
                        displaySeatArrangement(sessionId);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });


        // Set toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Select " + Constants.TITLE_SEAT);
        // Set back button
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set menu
        toolbar.inflateMenu(R.menu.seat_select);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_NEXT:
                        // If user hasn't finish selecting
                        if (mNumOfSelected != mNumOfGuest) {
                            Toast.makeText(getApplicationContext(), "You can't proceed before finish seat selection", Toast.LENGTH_LONG).show();
                        } else {
                            Intent intent = new Intent(getApplicationContext(), ConvocationPaymentActivity.class);
                            startActivity(intent);
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    private void displaySeatArrangement(int sessionId) {
        // Initialise variables for displaying seats in GridView

        final SeatAdapter seatAdapter = new SeatAdapter(this);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(seatAdapter);

        // Get seats and add into adapter
        Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild("sessionID").equalTo(sessionId);
        seatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot seatsSnapshot) {
                seatAdapter.clear();
                for (DataSnapshot seatSnapshot : seatsSnapshot.getChildren()) {
                    seatAdapter.add(seatSnapshot.getValue(Seat.class));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Get number of columns from FIREBASE
        Query sessionQuery = Constants.FIREBASE_REF_SESSIONS.orderByChild(Constants.FIREBASE_ATTR_SESSIONS_ID).equalTo(sessionId);
        sessionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    ConvocationSession session = sessionSnapshot.getValue(ConvocationSession.class);
                    int numberOfColumns = session.getColumnSize();
                    updateGridViewWidth(mGridView, numberOfColumns);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Mark occupied when user click on a seat
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Seat selectedSeat = (Seat) parent.getItemAtPosition(position);

                // Set the selectedSeat as selected, if the seat is available & mNumOfSelected is not more than mNumOfGuest
                if (selectedSeat.getStatus().equals(getString(R.string.seat_status_available)) && mNumOfSelected < mNumOfGuest) {
                    Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_ID)
                            .equalTo(selectedSeat.getId());
                    seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                Seat firebaseSeat = seatSnapshot.getValue(Seat.class);
                                if (firebaseSeat.getSessionID() == selectedSeat.getSessionID()) {
                                    String seatKey = seatSnapshot.getKey();
                                    Constants.FIREBASE_REF_SEATS.child(seatKey).child(Constants.FIREBASE_ATTR_SEATS_STATUS).setValue(getString(R.string.seat_status_selected));
                                    mNumOfSelected++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                } else if (selectedSeat.getStatus().equals(getString(R.string.seat_status_selected))) {
                    // Set the selectedSeat as available, if the seat is selected
                    Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_ID)
                            .equalTo(selectedSeat.getId());
                    seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                Seat firebaseSeat = seatSnapshot.getValue(Seat.class);
                                if (firebaseSeat.getSessionID() == selectedSeat.getSessionID()) {
                                    String seatKey = seatSnapshot.getKey();
                                    Constants.FIREBASE_REF_SEATS.child(seatKey).child(Constants.FIREBASE_ATTR_SEATS_STATUS).setValue(getString(R.string.seat_status_available));
                                    mNumOfSelected--;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "else", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    /**
     * Set GridView number of columns and grid width
     */
    private void updateGridViewWidth(GridView gridView, int numberOfColumns) {
        gridView.setNumColumns(numberOfColumns);
        ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();

        // Set GridView minimum width of 2 columns to show the word Stage
        if (numberOfColumns < 2)
            layoutParams.width = convertDpToPixels(2 * GRID_COLUMN_WIDTH_IN_DP, getApplicationContext());
        else
            layoutParams.width = convertDpToPixels(numberOfColumns * GRID_COLUMN_WIDTH_IN_DP, getApplicationContext());
        gridView.setLayoutParams(layoutParams);

        // Show seat is empty text if number of column is zero
        if (numberOfColumns == 0)
            findViewById(R.id.text_view_seat_empty).setVisibility(View.VISIBLE);
        else
            findViewById(R.id.text_view_seat_empty).setVisibility(View.GONE);
    }

    /**
     * Used for calculating grid total width
     */
    int convertDpToPixels(float dp, Context context) {
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }
}
