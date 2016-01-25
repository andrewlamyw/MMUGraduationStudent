package com.lamyatweng.mmugraduationstudent.Student;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class StudentDeleteDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Retrieve studentID passed from previous fragment
        Bundle bundle = getArguments();
        final String studentKey = bundle.getString(getString(R.string.key_student_key));

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete student?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Firebase.setAndroidContext(getActivity());
                        Firebase studentRef = new Firebase(Constants.FIREBASE_STRING_STUDENTS_REF);
                        studentRef.child(studentKey).setValue(null);
                        Toast.makeText(getActivity(), "Student deleted", Toast.LENGTH_SHORT).show();
                        // Close StudentDisplayDetailDialogFragment because item is removed
                        getFragmentManager().popBackStackImmediate(StudentDisplayDetailDialogFragment.class.getName(),
                                FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
