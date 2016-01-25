package com.lamyatweng.mmugraduationstudent.Programme;

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

public class ProgrammeDeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve ProgrammeKey from previous fragment
        Bundle bundle = getArguments();
        final String programmeKey = bundle.getString(getString(R.string.key_programme_key));

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete programme?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Firebase.setAndroidContext(getActivity());
                        Firebase programmeRef = new Firebase(Constants.FIREBASE_STRING_PROGRAMMES_REF);
                        programmeRef.child(programmeKey).setValue(null);
                        Toast.makeText(getActivity(), "Programme deleted", Toast.LENGTH_LONG).show();
                        // Close ProgrammeDisplayDetailDialogFragment because item is removed
                        getFragmentManager().popBackStackImmediate(ProgrammeDisplayDetailDialogFragment.class.getName(),
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