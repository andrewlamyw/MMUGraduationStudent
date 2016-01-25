package com.lamyatweng.mmugraduationstudent;

import android.app.Fragment;
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
import com.lamyatweng.mmugraduationstudent.Student.Student;

public class GraduationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graduation, container, false);

        // Get email of currently logged in user
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        final String userKey = session.getKeyUserFirebaseKey();

        // scenario 1: student which status has already completed. that means they already approved by staff
        // scenario 2: student has already successfully apply for graduation, waiting for staff approval. status is pending approval
        // scenario 3: student has not successful register
        // scenario 3.1: because student hasn't click register
        // scenario 3.2: because student doesn't fullfill one of the requirement

        // Get student academic status from Firebase

        // Get reference of views
        final TextView textView = (TextView) view.findViewById(R.id.text_view_graduation);
        final Button button = (Button) view.findViewById(R.id.button_graduation);

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
                        textView.setText("Congratulation, your graduation application has been approved. You can now proceed with convocation registration.");
                        button.setVisibility(View.VISIBLE);
                        button.setText("CONVOCATION");
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // button will open convocation fragment and checked convocation in navigation drawer
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
