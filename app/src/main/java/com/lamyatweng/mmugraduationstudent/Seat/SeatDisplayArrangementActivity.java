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
import com.lamyatweng.mmugraduationstudent.R;

public class SeatDisplayArrangementActivity extends AppCompatActivity
        implements SeatAddDialogFragment.OnCreateSeatDialogButtonClicked, SeatDeleteDialogFragment.OnDeleteSeatDialogButtonClicked {

    final int GRID_COLUMN_WIDTH_IN_DP = 39; // default 24

    Query mSeatQuery;
    ValueEventListener mSeatListener;
    SeatAdapter mSeatAdapter;
    GridView mGridView;
    String mSessionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_display_arrangement);

        // Get mSessionId & numOfCol from previous sMainActivity
        Intent intent = getIntent();
        final int sessionId = intent.getIntExtra(Constants.EXTRA_SESSION_ID, -1);
        mSessionKey = intent.getStringExtra(Constants.EXTRA_SESSION_KEY);


        // Initialise variables for displaying seats in GridView
        mSeatQuery = Constants.FIREBASE_REF_SEATS.orderByChild("sessionID").equalTo(sessionId);
        mSeatAdapter = new SeatAdapter(this);
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mSeatAdapter);


        // Display seat detail when user click on a seat
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Seat selectedSeat = (Seat) parent.getItemAtPosition(position);
                Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_ID)
                        .equalTo(selectedSeat.getId());
                seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                            Seat firebaseSeat = seatSnapshot.getValue(Seat.class);
                            if (firebaseSeat.getSessionID() == selectedSeat.getSessionID()) {
                                Intent intent = new Intent(getApplicationContext(), SeatEditActivity.class);
                                intent.putExtra(Constants.EXTRA_SEAT_KEY, seatSnapshot.getKey());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
            }
        });

        // Set toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_SEAT + " Arrangement");
        // Set back button
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set menu
        toolbar.inflateMenu(R.menu.seat_arrangement);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_SESSIONID).equalTo(sessionId);

                switch (item.getTitle().toString()) {
                    case Constants.MENU_DELETE:
                        seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    SeatDeleteDialogFragment seatDeleteDialogFragment = new SeatDeleteDialogFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt(getString(R.string.key_session_id), sessionId);
                                    bundle.putString(getString(R.string.key_session_key), mSessionKey);
                                    seatDeleteDialogFragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().add(seatDeleteDialogFragment, null).commit();
                                } else {
                                    Toast.makeText(getApplicationContext(), "There are no seats to delete", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                        return true;

                    case Constants.MENU_ADD:
                        seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    SeatExistDialogFragment dialogFragment = new SeatExistDialogFragment();
                                    getFragmentManager().beginTransaction().add(dialogFragment, null).commit();
                                } else {
                                    SeatAddDialogFragment dialogFragment = new SeatAddDialogFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString(getString(R.string.key_session_key), mSessionKey);
                                    bundle.putInt(getString(R.string.key_session_id), sessionId);
                                    dialogFragment.setArguments(bundle);
                                    getFragmentManager().beginTransaction().add(dialogFragment, null).commit();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });
                        return true;

                    default:
                        return false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Get seats and add into adapter
        mSeatListener = mSeatQuery.addValueEventListener(new ValueEventListener() {
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
        Constants.FIREBASE_REF_SESSIONS.child(mSessionKey)
                .child(Constants.FIREBASE_ATTR_SESSIONS_COLUMNSIZE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numberOfColumns = Integer.parseInt(dataSnapshot.getValue().toString());
                updateGridViewWidth(mGridView, numberOfColumns);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constants.FIREBASE_REF_SEATS.removeEventListener(mSeatListener);
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

    @Override
    public void onCreateSeatDialogButtonClicked(int numberOfColumns) {
        updateGridViewWidth(mGridView, numberOfColumns);
        Toast.makeText(this, "Seat arrangement is ready", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteSeatDialogButtonClicked(int numberOfColumns) {
        updateGridViewWidth(mGridView, numberOfColumns);
        Toast.makeText(this, Constants.TITLE_SEAT + "s deleted", Toast.LENGTH_LONG).show();
    }
}
