package com.lamyatweng.mmugraduationstudent;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Convocation.ConvocationRegistrationActivity;
import com.lamyatweng.mmugraduationstudent.Convocation.ConvocationSummaryFragment;
import com.lamyatweng.mmugraduationstudent.Student.Student;

public class GraduationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graduation, container, false);

        // Get user firebase child key from shared preference
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        final String userKey = session.getKeyUserFirebaseKey();

        // Get reference of views
        final TextView textView = (TextView) view.findViewById(R.id.text_view_graduation);
        final Button button = (Button) view.findViewById(R.id.button_graduation);

        // Get student academic status from firebase
        Constants.FIREBASE_REF_STUDENTS.child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student = dataSnapshot.getValue(Student.class);
                String studentStatus = student.getStatus();
                int balanceCreditHour = student.getBalanceCreditHour();
                double cgpa = student.getCgpa();
                double financialDue = student.getFinancialDue();
                int muet = student.getMuet();

                switch (studentStatus) {
                    case Constants.STUDENT_STATUS_COMPLETED:
                        // The student graduation application has already been approved and is allowed to register convocation

                        Constants.FIREBASE_REF_ORDERS.child(student.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot == null) {
                                    // The student has not register convocation, ask user to register convocation
                                    textView.setText("CONGRATULATION!\n\nYour application has been approved, please proceed with convocation registration.");
                                    button.setVisibility(View.VISIBLE);
                                    button.setText("CONVOCATION");
                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getActivity(), ConvocationRegistrationActivity.class);
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    // The student has already register convocation, display convocation summary page
                                    ConvocationSummaryFragment fragment = new ConvocationSummaryFragment();
                                    getActivity().getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });
                        break;

                    case Constants.STUDENT_STATUS_PENDING_APPROVAL:
                        textView.setText("Your graduation application has been submitted. We will review your application shortly.");
                        button.setVisibility(View.GONE);
                        break;

                    default:
                        // check student whether fulfill all the requirements
                        // scenario 1: fullfills, then show register button
                        // scenario 2: unfullfills, then show which requirements doesn't fullfill

                        if (studentStatus.equals(Constants.STUDENT_STATUS_ACTIVE) &&
                                balanceCreditHour <= 0 &&
                                cgpa >= 2 &&
                                financialDue <= 0 &&
                                muet >= 3) {

                            textView.setText("You have fulfilled all the requirements to graduate. Click the button to apply for graduation.");
                            button.setVisibility(View.VISIBLE);
                            button.setText("APPLY GRADUATION");
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    // Change student status to pending approval
                                    Constants.FIREBASE_REF_STUDENTS.child(userKey).child(Constants.FIREBASE_ATTR_STUDENTS_STATUS).setValue(Constants.STUDENT_STATUS_PENDING_APPROVAL);
                                }
                            });
                        } else {
                            String message = "You can't apply for graduation yet, because you did not fulfilled the following requirement(s)\n\n";
                            if (!studentStatus.equals(Constants.STUDENT_STATUS_ACTIVE))
                                message += "- Academic status must be \"Active\"\n";
                            if (balanceCreditHour > 0)
                                message += "- No credit hour remaining\n";
                            if (cgpa < 2)
                                message += "- CGPA must be at least 2.0\n";
                            if (financialDue > 0)
                                message += "- No financial due remaining\n";
                            if (muet < 3)
                                message += "- Muet must be at least band 3\n";
                            textView.setText(message);
                            button.setVisibility(View.GONE);
                        }
                        break;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        return view;
    }

    /**
     * Set ActionBar title
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setTitle(Constants.TITLE_GRADUATION);
    }
}
