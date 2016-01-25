package com.lamyatweng.mmugraduationstudent.Convocation;

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

public class ConvocationEditActivity extends AppCompatActivity {
    Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocation_edit);

        // Receive convocation key from the Intent
        Intent intent = getIntent();
        final String convocationKey = intent.getStringExtra(Constants.EXTRA_CONVOCATION_KEY);

        // Get references of views
        final TextInputLayout yearWrapper = (TextInputLayout) findViewById(R.id.wrapper_convocation_year);
        final TextView openDateTextView = (TextView) findViewById(R.id.open_registration_date);
        final TextView closeDateTextView = (TextView) findViewById(R.id.close_registration_date);

        // Open date picker and set text for open registration date
        openDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                mBundle.putInt(getString(R.string.key_datePicker_textView_id), R.id.open_registration_date);
                newFragment.setArguments(mBundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // Open date picker and set text for close registration date
        closeDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                mBundle.putInt(getString(R.string.key_datePicker_textView_id), R.id.close_registration_date);
                newFragment.setArguments(mBundle);
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

        // Retrieve convocation details from Firebase and display
        final Firebase convocationRef = new Firebase(Constants.FIREBASE_STRING_CONVOCATIONS_REF);
        convocationRef.child(convocationKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Convocation convocation = dataSnapshot.getValue(Convocation.class);
                // Null checking is required for handling removed item from Firebase
                if (convocation != null) {
                    yearWrapper.getEditText().setText(Integer.toString(convocation.getYear()));
                    openDateTextView.setText(convocation.getOpenRegistrationDate());
                    closeDateTextView.setText(convocation.getCloseRegistrationDate());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set up Toolbar with close and save button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        // Close dialog
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Commit: update programme information into Firebase
        toolbar.inflateMenu(R.menu.convocation_edit);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        final int year = Integer.parseInt(yearWrapper.getEditText().getText().toString());
                        final String open = openDateTextView.getText().toString();
                        final String close = closeDateTextView.getText().toString();

                        // Replace old values with new values in Firebase
                        Convocation updatedConvocation = new Convocation(year, open, close);
                        convocationRef.child(convocationKey).setValue(updatedConvocation);

                        // Display message and close dialog
                        Toast.makeText(getApplicationContext(), Constants.TITLE_CONVOCATION + " updated.", Toast.LENGTH_LONG).show();
                        finish();

                    default:
                        return false;
                }
            }
        });
    }
}
