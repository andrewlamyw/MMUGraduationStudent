package com.lamyatweng.mmugraduationstudent.Seat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SeatDeleteDialogFragment extends DialogFragment {
    OnDeleteSeatDialogButtonClicked mListener;

    /**
     * To ensure that the host sMainActivity implements the interface
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnDeleteSeatDialogButtonClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnDeleteSeatDialogButtonClicked");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Retrieve session id from previous sMainActivity
        Bundle bundle = getArguments();
        final int sessionId = bundle.getInt(getString(R.string.key_session_id), -1);
        final String sessionKey = bundle.getString(getString(R.string.key_session_key));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Delete seating arrangement?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Query seatQuery = Constants.FIREBASE_REF_SEATS.orderByChild("sessionID").equalTo(sessionId);
                        seatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
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

                        // Update session row and column size to zero
                        Constants.FIREBASE_REF_SESSIONS.child(sessionKey).child(Constants.FIREBASE_ATTR_SESSIONS_ROWSIZE).setValue(0);
                        Constants.FIREBASE_REF_SESSIONS.child(sessionKey).child(Constants.FIREBASE_ATTR_SESSIONS_COLUMNSIZE).setValue(0);

                        // Send the event and zero number of column to the host sMainActivity
                        mListener.onDeleteSeatDialogButtonClicked(0);

//                        Toast.makeText(getActivity(), Constants.TITLE_SEAT + "s deleted", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        return builder.create();
    }

    /**
     * Communicating with the Activity
     */
    public interface OnDeleteSeatDialogButtonClicked {
        void onDeleteSeatDialogButtonClicked(int numberOfColumns);
    }
}