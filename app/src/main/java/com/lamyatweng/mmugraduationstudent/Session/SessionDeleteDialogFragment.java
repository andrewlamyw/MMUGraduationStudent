package com.lamyatweng.mmugraduationstudent.Session;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SessionDeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve session key from previous sMainActivity
        Bundle bundle = getArguments();
        final String sessionKey = bundle.getString(getString(R.string.key_session_key));
        final int sessionId = bundle.getInt(getString(R.string.key_session_id));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete session?")
                .setMessage("Seat arrangement will also be deleted")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete all seats in this session
                        Query seatsQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_SESSIONID).equalTo(sessionId);
                        seatsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                    Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey()).setValue(null);
                                }
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                            }
                        });

                        // Delete session
                        Constants.FIREBASE_REF_SESSIONS.child(sessionKey).setValue(null);

                        Toast.makeText(getActivity(), Constants.TITLE_SESSION + " deleted", Toast.LENGTH_LONG).show();
                        getActivity().finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }
}