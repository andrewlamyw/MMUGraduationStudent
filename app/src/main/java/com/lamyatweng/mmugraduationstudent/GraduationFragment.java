package com.lamyatweng.mmugraduationstudent;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GraduationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graduation, container, false);

        // Get email of currently logged in user
        SessionManager session = new SessionManager(getActivity().getApplicationContext());
        session.checkLogin();
        String userEmail = session.getUserEmail();

        // scenario 1: student which status has already completed. that means they already approved by staff
        // scenario 2: student has already successfully apply for graduation, waiting for staff approval. status is pending approval
        // scenario 3: student has not successful register
        // scenario 3.1: because student hasn't click register
        // scenario 3.2: because student doesn't fullfill one of the requirement

        // Get student academic status from Firebase

        String studentStatus = "active";

        switch (studentStatus) {
            case "complete":
                break;

            case "pending approval":
                break;

            default:
                // check student whether fulfill all the requirements
                // scenario 1: fullfills, then show register button
                // scenario 2: unfullfills, then show which requirements doesn't fullfill
                break;
        }

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
