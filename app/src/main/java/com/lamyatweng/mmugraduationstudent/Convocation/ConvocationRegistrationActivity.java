package com.lamyatweng.mmugraduationstudent.Convocation;

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
import android.widget.Spinner;
import android.widget.Switch;

import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Seat.SeatSelectActivity;
import com.lamyatweng.mmugraduationstudent.SessionManager;

public class ConvocationRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocation_registration);

        final SessionManager sessionManager = new SessionManager(getApplicationContext());

        // Get reference of views
        final LinearLayout attendLayout = (LinearLayout) findViewById(R.id.linearLayout_convocation_attend);
        final TextInputLayout gratitudeMessage = (TextInputLayout) findViewById(R.id.wrapper_gratitude_message);

        final Switch convoAttendSwitch = (Switch) findViewById(R.id.switch_convocation_attendance);
        convoAttendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    attendLayout.setVisibility(View.VISIBLE);
                } else {
                    // The toggle is disabled
                    attendLayout.setVisibility(View.GONE);
                }
            }
        });

        final Spinner robeSizeSpinner = (Spinner) findViewById(R.id.spinner_robe_size);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this, R.array.robe_size, android.R.layout.simple_spinner_item);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        robeSizeSpinner.setAdapter(sizeAdapter);

        final Spinner numOfGuestSpinner = (Spinner) findViewById(R.id.spinner_numOfGuest);
        ArrayAdapter<CharSequence> guestNumAdapter = ArrayAdapter.createFromResource(this, R.array.number_of_guest, android.R.layout.simple_spinner_item);
        guestNumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numOfGuestSpinner.setAdapter(guestNumAdapter);

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
                            // save robe size, message and number of guest into shared preference (session)
                            // Redirects to LoginActivity if user is not logged in
                            String robeSize = robeSizeSpinner.getSelectedItem().toString();
                            String message = gratitudeMessage.getEditText().toString();
                            int numOfGuest = Integer.parseInt(numOfGuestSpinner.getSelectedItem().toString());

                            sessionManager.setConvocationAttendance(true);
                            sessionManager.setRobesize(robeSize);
                            sessionManager.setGratitudeMessage(message);
                            sessionManager.setNumberOfGuest(numOfGuest);

                            if (Integer.parseInt(numOfGuestSpinner.getSelectedItem().toString()) == 0) {
                                // this student doesn't has guest
                                // make payment
                                Intent intent = new Intent(getApplicationContext(), ConvocationPaymentActivity.class);
                                startActivity(intent);
                            } else {
                                // this student has guest
                                // select guest seat
                                Intent intent = new Intent(getApplicationContext(), SeatSelectActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            // this student is not going to the convocation
                            // make payment
                            sessionManager.setConvocationAttendance(false);

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
}
