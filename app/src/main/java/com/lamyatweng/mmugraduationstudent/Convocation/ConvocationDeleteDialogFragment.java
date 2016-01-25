package com.lamyatweng.mmugraduationstudent.Convocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;

public class ConvocationDeleteDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        final String convocationKey = bundle.getString(Constants.EXTRA_CONVOCATION_KEY);
        final int convocationYear = bundle.getInt(Constants.EXTRA_CONVOCATION_YEAR);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete convocation?");
        builder.setMessage("Sessions and seat arrangements will also be deleted");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Delete all sessions and seats in this convocation
                Log.i(getClass().getName(), "convocationYear: " + Integer.toString(convocationYear));
                Query sessionsQuery = Constants.FIREBASE_REF_SESSIONS.orderByChild(Constants.FIREBASE_ATTR_SESSIONS_CONVOCATIONYEAR).equalTo(convocationYear);
                sessionsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (final DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                            // Delete all seats in this session
                            int sessionId = Integer.parseInt(sessionSnapshot.child(Constants.FIREBASE_ATTR_SESSIONS_ID).getValue().toString());
                            Query seatsQuery = Constants.FIREBASE_REF_SEATS.orderByChild(Constants.FIREBASE_ATTR_SEATS_SESSIONID).equalTo(sessionId);
                            seatsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot seatSnapshot : dataSnapshot.getChildren()) {
                                        Constants.FIREBASE_REF_SEATS.child(seatSnapshot.getKey()).setValue(null);
                                    }
                                    // Delete this session
                                    Constants.FIREBASE_REF_SESSIONS.child(sessionSnapshot.getKey()).setValue(null);
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });

                // Delete this convocation
                Constants.FIREBASE_REF_CONVOCATIONS.child(convocationKey).setValue(null);

                Toast.makeText(getActivity(), "Convocation deleted", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder.create();
    }
}