package com.lamyatweng.mmugraduationstudent.Programme;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class ProgrammeEditDialogFragment extends DialogFragment {
    Bundle mBundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_programme_edit, container, false);

        // Retrieve programmeKey from previous fragment
        mBundle = getArguments();
        final String programmeKey = mBundle.getString(getString(R.string.key_programme_key));

        // Get references of views
        final TextInputLayout nameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_programme_name);
        final Spinner levelSpinner = (Spinner) view.findViewById(R.id.spinner_level);
        final Spinner facultySpinner = (Spinner) view.findViewById(R.id.spinner_faculty);

        // Populate levels from array
        final ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.programme_level_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelSpinner.setAdapter(levelAdapter);

        // Populate faculties from array
        final ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.faculty_array, android.R.layout.simple_spinner_item);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(facultyAdapter);

        // Retrieve programme details from Firebase and display
        Firebase.setAndroidContext(getActivity());
        final Firebase programmeRef = new Firebase(Constants.FIREBASE_STRING_PROGRAMMES_REF);
        programmeRef.child(programmeKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Programme programme = dataSnapshot.getValue(Programme.class);
                // Null checking is required for handling removed item from Firebase
                if (programme != null) {
                    nameWrapper.getEditText().setText(programme.getName());
                    levelSpinner.setSelection(levelAdapter.getPosition(programme.getLevel()));
                    facultySpinner.setSelection(facultyAdapter.getPosition(programme.getFaculty()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        // Set up Toolbar with close and save button
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.inflateMenu(R.menu.programme_edit);
        // Close dialog
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgrammeEditDialogFragment.this.getDialog().cancel();
            }
        });
        // Commit: update programme information into Firebase
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        final String name = nameWrapper.getEditText().getText().toString();
                        final String level = levelSpinner.getSelectedItem().toString();
                        final String faculty = facultySpinner.getSelectedItem().toString();

                        // Replace old values with new values in Firebase
                        Programme updatedProgramme = new Programme(name, level, faculty);
                        programmeRef.child(programmeKey).setValue(updatedProgramme);

                        // Display message and close dialog
                        Toast.makeText(getActivity(), Constants.TITLE_PROGRAMME + " updated.", Toast.LENGTH_LONG).show();
                        ProgrammeEditDialogFragment.this.getDialog().cancel();
                        return true;

                    default:
                        return false;
                }
            }
        });

        return view;
    }

    /**
     * Set full screen dialog theme
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
    }
}
