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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.Convocation.ConvocationPaymentActivity;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Session.ConvocationSession;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectActivity extends AppCompatActivity {

    final int GRID_COLUMN_WIDTH_IN_DP = 39; // default 24
    GridView mGridView;
    int mNumOfSelected = 0;
    int mNumOfGuest;
    SeatSelectAdapter mSeatAdapter;
    List<String> mSelectedSeatIdList = new ArrayList<>();
    TextView mNumOfSeatsSelectedTextView;
    String mDate, mStartTime, mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_select);

        // Receive convocation registration information from Intent
        Intent intent = getIntent();
        final String robeSize = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_ROBE_SIZE);
        final String gratitudeMessage = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_GRATITUDE_MESSAGE);
        final int sessionId = intent.getIntExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, -1);
        mNumOfGuest = intent.getIntExtra(Constants.EXTRA_CONVOCATION_ORDER_NUMBER_OF_GUEST, -1);

        // Get reference of views
        mNumOfSeatsSelectedTextView = (TextView) findViewById(R.id.textView_convocation_numOfSeatsSelected);
        TextView maxNumOfSeatsTextView = (TextView) findViewById(R.id.textView_convocation_maxNumOfSeats);
        final TextView sessionTextView = (TextView) findViewById(R.id.textView_convocation_session);
        final TextView dateTextView = (TextView) findViewById(R.id.textView_convocation_date);
        final TextView startTimeTextView = (TextView) findViewById(R.id.textView_convocation_startTime);
        final TextView endTimeTextView = (TextView) findViewById(R.id.textView_convocation_endTime);

        maxNumOfSeatsTextView.setText(Integer.toString(mNumOfGuest));
        displaySeatArrangement(sessionId);

        Query sessionQuery = Constants.FIREBASE_REF_SESSIONS.orderByChild(Constants.FIREBASE_ATTR_SESSIONS_ID).equalTo(sessionId);
        sessionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    ConvocationSession session = sessionSnapshot.getValue(ConvocationSession.class);
                    sessionTextView.setText(Integer.toString(session.getSessionNumber()));
                    dateTextView.setText(session.getDate());
                    startTimeTextView.setText(session.getStartTime());
                    endTimeTextView.setText(session.getEndTime());

                    mDate = session.getDate();
                    mStartTime = session.getStartTime();
                    mEndTime = session.getEndTime();
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
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ATTENDANCE, true);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ROBE_SIZE, robeSize);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_GRATITUDE_MESSAGE, gratitudeMessage);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_NUMBER_OF_GUEST, mNumOfGuest);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_DATE, mDate);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_START_TIME, mStartTime);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_END_TIME, mEndTime);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, sessionId);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SEAT_1, mSelectedSeatIdList.get(0));
                            if (mNumOfGuest == 2)
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SEAT_2, mSelectedSeatIdList.get(1));
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
        mSeatAdapter = new SeatSelectAdapter(this);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mSeatAdapter);

        // Get seats and add into adapter
        Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild("sessionID").equalTo(sessionId);
        seatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot seatsSnapshot) {
                mSeatAdapter.clear();
                for (DataSnapshot seatSnapshot : seatsSnapshot.getChildren()) {
                    mSeatAdapter.add(seatSnapshot.getValue(Seat.class));
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

        // Temporary change color of seat to selected to show selected seat
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Seat selectedSeat = (Seat) parent.getItemAtPosition(position);
                if (selectedSeat.getStatus().equals(getString(R.string.seat_status_available)) && mNumOfSelected < mNumOfGuest) {
                    // Set the selectedSeat as selected, if the seat is available & mNumOfSelected is not more than mNumOfGuest
                    mSelectedSeatIdList.add(Integer.toString(selectedSeat.getId())); // Convert seat id to String because remove(int) is meant for object position
                    mSeatAdapter.remove(selectedSeat);
                    selectedSeat.setStatus(getString(R.string.seat_status_selected));
                    mSeatAdapter.insert(selectedSeat, position);
                    mNumOfSelected++;
                } else if (selectedSeat.getStatus().equals(getString(R.string.seat_status_available)) && mNumOfSelected >= mNumOfGuest) {
                    // Show error message, if the seat is available & mNumOfSelected is more or equals to mNumOfGuest
                    Toast.makeText(getApplicationContext(), "Unchecked previous seat to select other", Toast.LENGTH_LONG).show();
                } else if (selectedSeat.getStatus().equals(getString(R.string.seat_status_selected))) {
                    // Set the selectedSeat as available, if the seat is selected
                    mSelectedSeatIdList.remove(Integer.toString(selectedSeat.getId())); // Convert seat id to String because remove(int) is meant for object position
                    mSeatAdapter.remove(selectedSeat);
                    selectedSeat.setStatus(getString(R.string.seat_status_available));
                    mSeatAdapter.insert(selectedSeat, position);
                    mNumOfSelected--;
                } else {
                    // Either the seat is occupied or disabled
                }
                mNumOfSeatsSelectedTextView.setText(Integer.toString(mNumOfSelected));
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
