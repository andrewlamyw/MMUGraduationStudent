package com.lamyatweng.mmugraduationstudent.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Seat.SeatDisplayArrangementActivity;

public class SessionDisplayDetailActivity extends AppCompatActivity {
    int mSessionId;
    int mColumnSize;
    Boolean mDoneLoadSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_display);

        // Initialize local variables
        mDoneLoadSession = false;

        // Receive convocation key from the Intent
        Intent intent = getIntent();
        final String sessionKey = intent.getStringExtra(Constants.EXTRA_SESSION_KEY);

        // Get references of views
        final TextInputLayout convocationYearWrapper = (TextInputLayout) findViewById(R.id.wrapper_convocation_year);
        final TextInputLayout sessionNumberWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_number);
        final TextInputLayout sessionIdWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_id);
        final TextInputLayout dateWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_date);
        final TextInputLayout startTimeWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_start_time);
        final TextInputLayout endTimeWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_end_time);
        final TextInputLayout rowSizeWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_row_size);
        final TextInputLayout columnSizeWrapper = (TextInputLayout) findViewById(R.id.wrapper_session_column_size);

        // Retrieve session details from Firebase and display
        Firebase sessionRef = new Firebase(Constants.FIREBASE_STRING_SESSIONS_REF);
        sessionRef.child(sessionKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConvocationSession session = dataSnapshot.getValue(ConvocationSession.class);
                // Null checking is required for handling removed item from Firebase
                if (session != null) {
                    convocationYearWrapper.getEditText().setText(Integer.toString(session.getConvocationYear()));
                    sessionNumberWrapper.getEditText().setText(Integer.toString(session.getSessionNumber()));
                    sessionIdWrapper.getEditText().setText(Integer.toString(session.getId()));
                    dateWrapper.getEditText().setText(session.getDate());
                    startTimeWrapper.getEditText().setText(session.getStartTime());
                    endTimeWrapper.getEditText().setText(session.getEndTime());
                    rowSizeWrapper.getEditText().setText(Integer.toString(session.getRowSize()));
                    columnSizeWrapper.getEditText().setText(Integer.toString(session.getColumnSize()));

                    mSessionId = session.getId();
                    mColumnSize = session.getColumnSize();
                    mDoneLoadSession = true;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Open a new sMainActivity to display list of programmes in the session
        Button viewProgrammes = (Button) findViewById(R.id.button_view_programmes);
        viewProgrammes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SessionProgrammeListActivity.class);
                intent.putExtra(Constants.EXTRA_SESSION_KEY, sessionKey);
                startActivity(intent);
            }
        });

        // Open seating arrangement button
        Button viewSeating = (Button) findViewById(R.id.button_view_seating);
        viewSeating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDoneLoadSession) {
                    Intent intent = new Intent(getApplicationContext(), SeatDisplayArrangementActivity.class);
                    intent.putExtra(Constants.EXTRA_SESSION_ID, mSessionId);
                    intent.putExtra(Constants.EXTRA_SESSION_COLUMN_SIZE, mColumnSize);
                    intent.putExtra(Constants.EXTRA_SESSION_KEY, sessionKey);
                    startActivity(intent);
                } else
                    Toast.makeText(getApplicationContext(), "Connection failed, try again later.", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up Toolbar with back, edit and delete button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_SESSION + " Detail");
        // Close sMainActivity
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set up menu
        toolbar.inflateMenu(R.menu.session_details);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_DELETE:
                        SessionDeleteDialogFragment sessionDeleteDialogFragment = new SessionDeleteDialogFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString(getString(R.string.key_session_key), sessionKey);
                        bundle.putInt(getString(R.string.key_session_id), mSessionId);
                        sessionDeleteDialogFragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().add(sessionDeleteDialogFragment, null).addToBackStack(null).commit();
                        return true;

                    case Constants.MENU_EDIT:
                        Intent intent = new Intent(getApplicationContext(), SessionEditActivity.class);
                        intent.putExtra(Constants.EXTRA_SESSION_KEY, sessionKey);
                        startActivity(intent);
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
