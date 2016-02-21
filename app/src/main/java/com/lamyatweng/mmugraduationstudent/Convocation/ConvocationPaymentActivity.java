package com.lamyatweng.mmugraduationstudent.Convocation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.Order;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Seat.SeatSelectActivity;
import com.lamyatweng.mmugraduationstudent.SessionManager;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ConvocationPaymentActivity extends AppCompatActivity {
    Double mPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocation_payment);

        // Receive convocation key from the Intent
        Intent intent = getIntent();
        final Boolean convocationAttendance = intent.getBooleanExtra(Constants.EXTRA_CONVOCATION_ORDER_ATTENDANCE, false);
        final String robeSize = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_ROBE_SIZE);
        final String gratitudeMessage = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_GRATITUDE_MESSAGE);
        final String date = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_DATE);
        final String startTime = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_START_TIME);
        final String endTime = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_END_TIME);
        final String seat1 = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_SEAT_1);
        final String seat2 = intent.getStringExtra(Constants.EXTRA_CONVOCATION_ORDER_SEAT_2);
        final int numberOfGuest = intent.getIntExtra(Constants.EXTRA_CONVOCATION_ORDER_NUMBER_OF_GUEST, -1);
        final int sessionId = intent.getIntExtra(Constants.EXTRA_CONVOCATION_ORDER_SESSION_ID, -1);

        // Get reference of views
        TextView priceTextView = (TextView) findViewById(R.id.textView_convocation_price);
        TextView attendanceTextView = (TextView) findViewById(R.id.textView_convocation_attendance);
        TextView dateTextView = (TextView) findViewById(R.id.textView_convocation_date);
        TextView startTimeTextView = (TextView) findViewById(R.id.textView_convocation_startTime);
        TextView endTimeTextView = (TextView) findViewById(R.id.textView_convocation_endTime);
        TextView numberOfGuestTextView = (TextView) findViewById(R.id.textView_convocation_numberOfGuest);
        TextView seat1TextView = (TextView) findViewById(R.id.textView_convocation_seat1);
        TextView seat2TextView = (TextView) findViewById(R.id.textView_convocation_seat2);
        TextView robeSizeTextView = (TextView) findViewById(R.id.textView_convocation_robeSize);
        TextView gratitudeMessageTextView = (TextView) findViewById(R.id.textView_convocation_gratitudeMessage);
        LinearLayout dateLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_date);
        LinearLayout timeLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_startTime);
        LinearLayout numberOfGuestLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_numberOfGuest);
        LinearLayout seat1LinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_seat1);
        LinearLayout seat2LinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_seat2);
        LinearLayout robeSizeLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_robeSize);
        LinearLayout gratitudeMessageLinearLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_gratitudeMessage);

        // Get programme information of student from session manager
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        String level = sessionManager.getLevel();
        final String studentId = sessionManager.getStudentId();

        if (convocationAttendance) {
            // student will attend the convocation

            // Price is based on level of education
            switch (level) {
                case Constants.PROGRAMME_LEVEL_DIPLOMA:
                    mPrice = 150.00;
                    break;
                case Constants.PROGRAMME_LEVEL_BACHELOR:
                    mPrice = 150.00;
                    break;
                case Constants.PROGRAMME_LEVEL_MASTER:
                    mPrice = 200.00;
                    break;
                case Constants.PROGRAMME_LEVEL_DOCTORATE:
                    mPrice = 300.00;
                    break;
                default:
                    mPrice = 0.0;
                    break;
            }

            // Set text view text
            priceTextView.setText("RM " + Double.toString(mPrice));
            attendanceTextView.setText(convocationAttendance.toString());
            dateTextView.setText(date);
            startTimeTextView.setText(startTime);
            endTimeTextView.setText(endTime);
            numberOfGuestTextView.setText(Integer.toString(numberOfGuest));
            robeSizeTextView.setText(robeSize);
            gratitudeMessageTextView.setText(gratitudeMessage);

            // Show text views
            dateLinearLayout.setVisibility(View.VISIBLE);
            timeLinearLayout.setVisibility(View.VISIBLE);
            numberOfGuestLinearLayout.setVisibility(View.VISIBLE);
            robeSizeLinearLayout.setVisibility(View.VISIBLE);
            gratitudeMessageLinearLayout.setVisibility(View.VISIBLE);

            if (numberOfGuest >= 1) {
                seat1TextView.setText(seat1);
                seat1LinearLayout.setVisibility(View.VISIBLE);
            }

            if (numberOfGuest == 2) {
                seat2TextView.setText(seat2);
                seat2LinearLayout.setVisibility(View.VISIBLE);
            }
        } else {
            // student will not attend the convocation
            mPrice = 100.00;
            priceTextView.setText("RM " + Double.toString(mPrice));
            attendanceTextView.setText(convocationAttendance.toString());
        }

        // card expiry month
        final Spinner monthSpinner = (Spinner) findViewById(R.id.spinner_month);
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthAdapter);

        // card expiry year
        final Spinner yearSpinner = (Spinner) findViewById(R.id.spinner_year);
        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Set toolbar title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_PAYMENT);
        // Set back button
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set menu
        toolbar.inflateMenu(R.menu.convocation_payment);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SUBMIT:
                        // Change seat 1 status to occupied
                        if (numberOfGuest >= 1) {
                            Query seat1Query = Constants.FIREBASE_REF_SEATS
                                    .orderByChild(Constants.FIREBASE_ATTR_SEATS_ID).equalTo(Integer.parseInt(seat1));
                            seat1Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DataSnapshot seatSnapshot = dataSnapshot.getChildren().iterator().next();
                                    Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey())
                                            .child(Constants.FIREBASE_ATTR_SEATS_STATUS)
                                            .setValue(getString(R.string.seat_status_occupied));
                                    // Save student name into the seat
                                    Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey())
                                            .child(Constants.FIREBASE_ATTR_SEATS_STUDENTID)
                                            .setValue(studentId);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        // Change seat 2 status to occupied
                        if (numberOfGuest == 2) {
                            Query seat2Query = Constants.FIREBASE_REF_SEATS
                                    .orderByChild(Constants.FIREBASE_ATTR_SEATS_ID).equalTo(Integer.parseInt(seat2));
                            seat2Query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    DataSnapshot seatSnapshot = dataSnapshot.getChildren().iterator().next();
                                    Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey())
                                            .child(Constants.FIREBASE_ATTR_SEATS_STATUS)
                                            .setValue(getString(R.string.seat_status_occupied));
                                    // Save student name into the seat
                                    Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey())
                                            .child(Constants.FIREBASE_ATTR_SEATS_STUDENTID)
                                            .setValue(studentId);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });
                        }

                        // Save order details to Firebase
                        Calendar calendar = Calendar.getInstance();
                        Date paymentDate = calendar.getTime();
                        String paymentDateString = DateFormat.getDateInstance().format(paymentDate);
                        Order order = null;
                        if (convocationAttendance) {
                            // Student will attend
                            switch (numberOfGuest) {
                                case 0:
                                    order = new Order(convocationAttendance, mPrice, gratitudeMessage, numberOfGuest, paymentDateString, robeSize, sessionId, studentId);
                                    break;
                                case 1:
                                    order = new Order(convocationAttendance, mPrice, gratitudeMessage, numberOfGuest, paymentDateString, robeSize, Integer.parseInt(seat1), sessionId, studentId);
                                    break;
                                case 2:
                                    order = new Order(convocationAttendance, mPrice, gratitudeMessage, numberOfGuest, paymentDateString, robeSize, Integer.parseInt(seat1), Integer.parseInt(seat2), sessionId, studentId);
                                    break;
                            }
                        } else {
                            // Student will not attend
                            order = new Order(convocationAttendance, mPrice, paymentDateString, sessionId, studentId);
                        }
                        Constants.FIREBASE_REF_ORDERS.child(studentId).setValue(order);

                        // Close activity
                        ConvocationRegistrationActivity.sConvocationRegistrationActivity.finish();
                        SeatSelectActivity.sSeatSelectActivity.finish();
                        finish();

                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
