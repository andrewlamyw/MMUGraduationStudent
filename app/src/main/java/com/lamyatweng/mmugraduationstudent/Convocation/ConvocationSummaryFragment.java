package com.lamyatweng.mmugraduationstudent.Convocation;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.Order;
import com.lamyatweng.mmugraduationstudent.R;
import com.lamyatweng.mmugraduationstudent.Session.ConvocationSession;
import com.lamyatweng.mmugraduationstudent.SessionManager;

public class ConvocationSummaryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_convocation_summary, container, false);

        // Get references of views
        final TextInputLayout sessionIdWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_sessionId);
        final TextInputLayout sessionDateWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_sessionDate);
        final TextInputLayout sessionStartTimeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_sessionStartTime);
        final TextInputLayout sessionEndTimeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_sessionEndTime);
        final TextInputLayout attendanceWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_attendance);
        final TextInputLayout robeSizeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_robeSize);
        final TextInputLayout gratitudeMessageWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_gratitudeMessage);
        final TextInputLayout numberOfGuestWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_numberOfGuest);
        final TextInputLayout seatId1Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_seatId1);
        final TextInputLayout seatId2Wrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_seatId2);
        final TextInputLayout feeWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_fee);
        final TextInputLayout paymentDateWrapper = (TextInputLayout) view.findViewById(R.id.wrapper_orders_paymentDate);

        // Get user firebase child key from shared preference
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        final String studentId = session.getStudentId();

        // Get order details from Firebase
        Query orderQuery = Constants.FIREBASE_REF_ORDERS.orderByChild(Constants.FIREBASE_ATTR_ORDERS_STUDENTID).equalTo(studentId);
        orderQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    DataSnapshot orderSnapshot = dataSnapshot.getChildren().iterator().next();
                    Order order = orderSnapshot.getValue(Order.class);

                    // Get session details from Firebase
                    Query sessionQuery = Constants.FIREBASE_REF_SESSIONS.orderByChild(Constants.FIREBASE_ATTR_SESSIONS_ID).equalTo(order.getSessionID());
                    sessionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            DataSnapshot sessionSnapshot = dataSnapshot.getChildren().iterator().next();
                            if (sessionSnapshot != null) {
                                ConvocationSession convocationSession = sessionSnapshot.getValue(ConvocationSession.class);

                                // Set value for display fields
                                sessionIdWrapper.getEditText().setText(Integer.toString(convocationSession.getId()));
                                sessionDateWrapper.getEditText().setText(convocationSession.getDate());
                                sessionStartTimeWrapper.getEditText().setText(convocationSession.getStartTime());
                                sessionEndTimeWrapper.getEditText().setText(convocationSession.getEndTime());
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                        }
                    });
                    // set value for display fields
                    attendanceWrapper.getEditText().setText(order.getAttendance().toString());
                    feeWrapper.getEditText().setText(Double.toString(order.getFee()));
                    paymentDateWrapper.getEditText().setText(order.getPaymentDate());
                    if (order.getAttendance()) {
                        robeSizeWrapper.getEditText().setText(order.getRobeSize());
                        gratitudeMessageWrapper.getEditText().setText(order.getGratitudeMessage());
                        numberOfGuestWrapper.getEditText().setText(Integer.toString(order.getNumberOfGuest()));
                        if (order.getNumberOfGuest() > 0) {
                            seatId1Wrapper.getEditText().setText(Integer.toString(order.getSeatID1()));
                            seatId2Wrapper.getEditText().setText(Integer.toString(order.getSeatID2()));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Convocation Order Summary");

        return view;
    }
}
