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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class StudentDisplayDetailDialogFragment extends DialogFragment {
    Bundle mBundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student_display, container, false);

        // Retrieve studentKey from previous fragment
        mBundle = getArguments();
        final String studentKey = mBundle.getString(getString(R.string.key_student_key));

        // Get references of views
        final TextInputLayout nameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_name);
        final TextInputLayout idWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_id);
        final TextInputLayout emailWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_email);
        final TextInputLayout programmeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_programme);
        final TextInputLayout statusWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_status);
        final TextInputLayout creditHourWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_balanceCreditHour);
        final TextInputLayout cgpaWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_cgpa);
        final TextInputLayout muetWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_muet);
        final TextInputLayout financialWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_financialDue);

        // Retrieve programme details from Firebase and display
        Firebase studentRef = new Firebase(Constants.FIREBASE_STRING_STUDENTS_REF);
        studentRef.child(studentKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                // Null checking is required for handling removed item from Firebase
                if (student != null) {
                    nameWrapper.getEditText().setText(student.getName());
                    idWrapper.getEditText().setText(student.getId());
                    programmeWrapper.getEditText().setText(student.getProgramme());
                    statusWrapper.getEditText().setText(student.getStatus());
                    emailWrapper.getEditText().setText(student.getEmail());
                    creditHourWrapper.getEditText().setText(String.valueOf(student.getBalanceCreditHour()));
                    cgpaWrapper.getEditText().setText(String.valueOf(student.getCgpa()));
                    muetWrapper.getEditText().setText(String.valueOf(student.getMuet()));
                    financialWrapper.getEditText().setText(String.valueOf(student.getFinancialDue()));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        /*Query queryRef = studentRef.orderByChild("id").equalTo(studentID);
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);

                nameWrapper.getEditText().setText(student.getName());
                idWrapper.getEditText().setText(student.getId());
                programmeWrapper.getEditText().setText(student.getProgramme());
                statusWrapper.getEditText().setText(student.getStatus());
                emailWrapper.getEditText().setText(student.getEmail());
                creditHourWrapper.getEditText().setText(String.valueOf(student.getBalanceCreditHour()));
                cgpaWrapper.getEditText().setText(String.valueOf(student.getCgpa()));
                muetWrapper.getEditText().setText(String.valueOf(student.getMuet()));
                financialWrapper.getEditText().setText(String.valueOf(student.getFinancialDue()));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Student student = dataSnapshot.getValue(Student.class);

                nameWrapper.getEditText().setText(student.getName());
                idWrapper.getEditText().setText(student.getId());
                programmeWrapper.getEditText().setText(student.getProgramme());
                statusWrapper.getEditText().setText(student.getStatus());
                emailWrapper.getEditText().setText(student.getEmail());
                creditHourWrapper.getEditText().setText(String.valueOf(student.getBalanceCreditHour()));
                cgpaWrapper.getEditText().setText(String.valueOf(student.getCgpa()));
                muetWrapper.getEditText().setText(String.valueOf(student.getMuet()));
                financialWrapper.getEditText().setText(String.valueOf(student.getFinancialDue()));
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });*/

        // Set up Toolbar with back, edit and delete button
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.inflateMenu(R.menu.student_details);
        // Close dialog
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentDisplayDetailDialogFragment.this.getDialog().cancel();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case Constants.MENU_DELETE:
                        StudentDeleteDialogFragment studentDeleteDialogFragment = new StudentDeleteDialogFragment();
                        studentDeleteDialogFragment.setArguments(mBundle);
                        getFragmentManager().beginTransaction().add(studentDeleteDialogFragment, null).addToBackStack(null).commit();
                        return true;

                    case Constants.MENU_EDIT:
                        StudentEditDialogFragment studentEditDialogFragment = new StudentEditDialogFragment();
                        studentEditDialogFragment.setArguments(mBundle);
                        getFragmentManager().beginTransaction().add(studentEditDialogFragment, null).addToBackStack(null).commit();
                        return true;
                }
                return false;
            }
        });

        return view;
    }


    /**
     * Set dialog theme as full screen
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DialogWhenLarge_NoActionBar);
    }
}
