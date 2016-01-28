package com.lamyatweng.mmugraduationstudent.Student;

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
import com.lamyatweng.mmugraduationstudent.Programme.Programme;
import com.lamyatweng.mmugraduationstudent.Programme.ProgrammeAdapter;
import com.lamyatweng.mmugraduationstudent.R;

public class StudentEditDialogFragment extends DialogFragment {
    Bundle mBundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student_edit, container, false);

        // Retrieve studentKey from previous fragment
        mBundle = getArguments();
        final String studentKey = mBundle.getString(getString(R.string.key_student_key));

        // Get references of views
        final TextInputLayout nameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_name);
        final TextInputLayout idWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_id);
        final TextInputLayout balanceCreditHourWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_balanceCreditHour);
        final TextInputLayout cgpaWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_cgpa);
        final TextInputLayout financialDueWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_financialDue);

        // Populate programmes from Firebase
        final Spinner programmeSpinner = (Spinner) view.findViewById(R.id.spinner_student_programme);
        final ProgrammeAdapter programmeAdapter = new ProgrammeAdapter(getActivity());
        Constants.FIREBASE_REF_PROGRAMMES.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                programmeAdapter.clear();
                for (DataSnapshot programmeSnapshot : dataSnapshot.getChildren()) {
                    Programme programme = programmeSnapshot.getValue(Programme.class);
                    programmeAdapter.add(programme);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        programmeSpinner.setAdapter(programmeAdapter);

        // Populate student statuses from array
        final Spinner statusSpinner = (Spinner) view.findViewById(R.id.status_spinner);
        final ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.student_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        // Populate muet grades from array
        final Spinner muetSpinner = (Spinner) view.findViewById(R.id.muet_spinner);
        final ArrayAdapter<CharSequence> muetAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.muet_grading_array, android.R.layout.simple_spinner_item);
        muetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        muetSpinner.setAdapter(muetAdapter);

        // Retrieve student details from Firebase and display
        final Firebase studentRef = new Firebase(Constants.FIREBASE_STRING_STUDENTS_REF);
        studentRef.child(studentKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                // Required for handling item removed from Firebase
                if (student != null) {
                    String name = student.getProgramme();
                    String level = student.getLevel();
                    String faculty = student.getFaculty();
                    Programme programme = new Programme(name, level, faculty);

                    nameWrapper.getEditText().setText(student.getName());
                    idWrapper.getEditText().setText(student.getId());
                    programmeSpinner.setSelection(programmeAdapter.getPosition(programme));
                    statusSpinner.setSelection(statusAdapter.getPosition(student.getStatus()));
                    balanceCreditHourWrapper.getEditText().setText(String.valueOf(student.getBalanceCreditHour()));
                    cgpaWrapper.getEditText().setText(String.valueOf(student.getCgpa()));
                    muetSpinner.setSelection(muetAdapter.getPosition(String.valueOf(student.getMuet())));
                    financialDueWrapper.getEditText().setText(String.valueOf(student.getFinancialDue()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Set Toolbar with close and save button
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_close_white_24dp);
        toolbar.inflateMenu(R.menu.student_edit);
        // Close dialog
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentEditDialogFragment.this.getDialog().cancel();
            }
        });
        // Commit: update programme information into Firebase
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_SAVE:
                        // Retrieve user inputs
                        Programme spinnerProgramme = (Programme) programmeSpinner.getSelectedItem();

                        int balanceCreditHour = Integer.parseInt(balanceCreditHourWrapper.getEditText().getText().toString());
                        double cgpa = Double.parseDouble(cgpaWrapper.getEditText().getText().toString());
                        String email = idWrapper.getEditText().getText().toString() + "@student.mmu.edu.my";
                        String faculty = spinnerProgramme.getFaculty();
                        double financialDue = Double.parseDouble(financialDueWrapper.getEditText().getText().toString());
                        String id = idWrapper.getEditText().getText().toString();
                        String level = spinnerProgramme.getLevel();
                        int muet = Integer.parseInt(muetSpinner.getSelectedItem().toString());
                        String name = nameWrapper.getEditText().getText().toString();
                        String programme = spinnerProgramme.getName();
                        String status = statusSpinner.getSelectedItem().toString();

                        // Replace old values with new values in Firebase
                        Student updatedStudent = new Student(balanceCreditHour, cgpa, email, faculty,
                                financialDue, id, level, muet, name, programme, status);
                        studentRef.child(studentKey).setValue(updatedStudent);

                        // Display message and close dialog
                        Toast.makeText(getActivity(), Constants.TITLE_STUDENT + " updated.", Toast.LENGTH_LONG).show();
                        StudentEditDialogFragment.this.getDialog().cancel();
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
