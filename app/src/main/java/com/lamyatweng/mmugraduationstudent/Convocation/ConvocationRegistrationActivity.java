package com.lamyatweng.mmugraduationstudent.Convocation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Seat.SeatSelectActivity;
import com.lamyatweng.mmugraduationstudent.Session.ConvocationSession;
import com.lamyatweng.mmugraduationstudent.SessionManager;
import com.lamyatweng.mmugraduationstudent.SessionProgramme;

public class ConvocationRegistrationActivity extends AppCompatActivity {
    public static Activity sConvocationRegistrationActivity;
    int mSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocation_registration);
        sConvocationRegistrationActivity = this;

        // Get programme information of student from session manager
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        final String programme = sessionManager.getProgramme();
        final String faculty = sessionManager.getFaculty();

        // Temporary hard code, to be replaced in future
        final String convoYear = "2016";

        // Get reference of views
        final LinearLayout attendLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_attend);
        final TextInputLayout gratitudeMessageWrapper = (TextInputLayout) findViewById(R.id.wrapper_gratitude_message);
        final ProgressBar loadingSpinner = (ProgressBar) findViewById(R.id.progressBar_login);

        final Switch convoAttendSwitch = (Switch) findViewById(R.id.switch_convocation_attendance);
        convoAttendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled, student is attending
                    attendLayout.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled, student is not attending
                    attendLayout.setVisibility(View.GONE);
                }
            }
        });

        final Spinner robeSizeSpinner = (Spinner) findViewById(R.id.spinner_robe_size);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.robe_size, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robeSizeSpinner.setAdapter(sizeAdapter);

        final Spinner numberOfGuestSpinner = (Spinner) findViewById(R.id.spinner_numOfGuest);
        ArrayAdapter<CharSequence> guestNumAdapter = ArrayAdapter.createFromResource(this, R.array.number_of_guest, android.R.layout.simple_spinner_item);
        guestNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numberOfGuestSpinner.setAdapter(guestNumAdapter);

        // Get session id based on programme of student
        Query sessionProgrammeQuery = Constants.FIREBASE_REF_SESSIONPROGRAMMES.child(convoYear)
                .orderByChild(Constants.FIREBASE_ATTR_SESSIONPROGRAMMES_NAME).equalTo(programme);
        sessionProgrammeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionProgrammeSnapshot : dataSnapshot.getChildren()) {
                    SessionProgramme sessionProgramme = sessionProgrammeSnapshot.getValue(SessionProgramme.class);
                    if (sessionProgramme.getFaculty().equals(faculty)) {
                        mSessionId = sessionProgramme.getSessionID();
                        loadingSpinner.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Set up Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_CONVOCATION + " Registration");
        // Back button
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Next button
        toolbar.inflateMenu(R.menu.convocation_registration);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_NEXT:
                        if (convoAttendSwitch.isChecked()) {
                            // this student is going to the convocation
                            final String robeSize = robeSizeSpinner.getSelectedItem().toString();
                            final String gratitudeMessage = gratitudeMessageWrapper.getEditText().getText().toString();
                            int numberOfGuest = Integer.parseInt(numberOfGuestSpinner.getSelectedItem().toString());

                            if (numberOfGuest == 0) {
                                // this student doesn't has guest, make payment

                                Query sessionQuery = Constants.FIREBASE_REF_SESSIONS.orderByChild(Constants.FIREBASE_ATTR_SESSIONS_ID).equalTo(mSessionId);
                                sessionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        DataSnapshot sessionSnapshot = dataSnapshot.getChildren().iterator().next();
                                        ConvocationSession session = sessionSnapshot.getValue(ConvocationSession.class);

                                        Intent intent = new Intent(getApplicationContext(), ConvocationPaymentActivity.class);
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, mSessionId);
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ATTENDANCE, true);
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ROBE_SIZE, robeSize);
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_GRATITUDE_MESSAGE, gratitudeMessage);
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_DATE, session.getDate());
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_START_TIME, session.getStartTime());
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_END_TIME, session.getEndTime());
                                        intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_NUMBER_OF_GUEST, 0);
                                        startActivity(intent);
                                    }

                                    @Override
                                    public void onCancelled(FirebaseError firebaseError) {
                                    }
                                });
                            } else {
                                // this student has guest, select guest seat
                                Intent intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, mSessionId);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ATTENDANCE, true);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ROBE_SIZE, robeSize);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_GRATITUDE_MESSAGE, gratitudeMessage);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_NUMBER_OF_GUEST, numberOfGuest);
                                startActivity(intent);
                            }
                        } else {
                            // this student is not going to the convocation, make payment
                            Intent intent = new Intent(getApplicationContext(), ConvocationPaymentActivity.class);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, mSessionId);
                            intent.putExtra(Constants.EXTRA_CONVOCATION_ORDER_ATTENDANCE, false);
                            startActivity(intent);
                        }
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
