package com.lamyatweng.mmugraduationstudent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Student.Student;

public class ProfileFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Get email of currently logged in user
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        String userEmail = session.getUserEmail();

        // Get reference of views
        final TextInputLayout studentNameWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_name);
        final TextInputLayout studentIdWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_id);
        final TextInputLayout programmeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_programme);
        final TextInputLayout studentStatusWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_status);
        final TextInputLayout emailWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_email);
        final TextInputLayout creditHourWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_balanceCreditHour);
        final TextInputLayout cgpaWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_cgpa);
        final TextInputLayout muetWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_muet);
        final TextInputLayout financialWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_student_financialDue);

        // Retrieve student information from Firebase and set to display
        Query studentQuery = Constants.FIREBASE_REF_STUDENTS.orderByChild(Constants.FIREBASE_ATTR_STUDENTS_EMAIL).equalTo(userEmail);
        studentQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getChildren().iterator().next().getValue(Student.class);

                studentNameWrapper.getEditText().setText(student.getName());
                studentIdWrapper.getEditText().setText(student.getId());
                programmeWrapper.getEditText().setText(student.getProgramme());
                studentStatusWrapper.getEditText().setText(student.getStatus());
                emailWrapper.getEditText().setText(student.getEmail());
                creditHourWrapper.getEditText().setText(Integer.toString(student.getBalanceCreditHour()));
                cgpaWrapper.getEditText().setText(Double.toString(student.getCgpa()));
                muetWrapper.getEditText().setText(Integer.toString(student.getMuet()));
                financialWrapper.getEditText().setText(Double.toString(student.getFinancialDue()));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        return view;
    }

    /**
     * Set toolbar title text
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(Constants.TITLE_PROFILE);
    }
}