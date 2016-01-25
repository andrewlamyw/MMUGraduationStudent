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

import com.firebase.client.Firebase;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class ProgrammeAddDialogFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_programme_add, container, false);

        // Get references of views
        final TextInputLayout programmeNameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_programme_name);
        final Spinner programmeLevelSpinner = (Spinner) view.findViewById(R.id.spinner_level);
        final Spinner facultySpinner = (Spinner) view.findViewById(R.id.spinner_faculty);

        // Populate levels from array
        ArrayAdapter<CharSequence> levelAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.programme_level_array, android.R.layout.simple_spinner_item);
        levelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programmeLevelSpinner.setAdapter(levelAdapter);

        // Populate faculties from array
        ArrayAdapter<CharSequence> facultyAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.faculty_array, android.R.layout.simple_spinner_item);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        facultySpinner.setAdapter(facultyAdapter);

        // Set Toolbar with close and save button
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("New " + Constants.TITLE_PROGRAMME);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.inflateMenu(R.menu.programme_add);
        // Close dialog
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgrammeAddDialogFragment.this.getDialog().cancel();
            }
        });

        // Commit: add new programme into Firebase
        Firebase.setAndroidContext(getActivity());
        final Firebase programmeRef = new Firebase(Constants.FIREBASE_STRING_PROGRAMMES_REF);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        String name = programmeNameWrapper.getEditText().getText().toString();
                        String level = programmeLevelSpinner.getSelectedItem().toString();
                        String faculty = facultySpinner.getSelectedItem().toString();

                        // Push into Firebase programme list
                        Programme newProgramme = new Programme(name, level, faculty);
                        programmeRef.push().setValue(newProgramme);

                        // Display message and close dialog
                        Toast.makeText(getActivity(), Constants.TITLE_PROGRAMME + " added.", Toast.LENGTH_LONG).show();
                        ProgrammeAddDialogFragment.this.getDialog().cancel();
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
