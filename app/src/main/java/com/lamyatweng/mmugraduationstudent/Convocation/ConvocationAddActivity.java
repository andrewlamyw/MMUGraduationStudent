package com.lamyatweng.mmugraduationstudent.Convocation;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.DatePickerFragment;
import com.lamyatweng.mmugraduationstudent.R;

public class ConvocationAddActivity extends AppCompatActivity {
    Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convocation_add);

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

        // Set Toolbar with close and save button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New " + Constants.TITLE_CONVOCATION);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.inflateMenu(R.menu.convocation_add);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Commit: add new convocation into Firebase
        final Firebase convocationRef = new Firebase(Constants.FIREBASE_STRING_CONVOCATIONS_REF);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        int year = Integer.parseInt(yearWrapper.getEditText().getText().toString());
                        String openDate = openDateTextView.getText().toString();
                        String closeDate = closeDateTextView.getText().toString();

                        // Push into Firebase programme list
                        Convocation convocation = new Convocation(year, openDate, closeDate);
                        convocationRef.push().setValue(convocation);

                        // Display message and close dialog
                        Toast.makeText(getApplicationContext(), Constants.TITLE_CONVOCATION + " added.", Toast.LENGTH_LONG).show();
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });
    }
}
