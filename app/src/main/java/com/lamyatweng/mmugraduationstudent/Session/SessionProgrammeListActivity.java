package com.lamyatweng.mmugraduationstudent.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SessionProgrammeListActivity extends AppCompatActivity {
    Bundle mBundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_programme_list);

        // Receive session key from the Intent
        Intent intent = getIntent();
        final String sessionKey = intent.getStringExtra(Constants.EXTRA_SESSION_KEY);
        mBundle.putString(getString(R.string.key_session_key), sessionKey);

        // Get references of views
        final ListView programmeList = (ListView) findViewById(R.id.session_programme_list_view);

        // Retrieve session details from Firebase and display
        Firebase sessionRef = new Firebase(Constants.FIREBASE_STRING_SESSIONS_REF);
        sessionRef.child(sessionKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConvocationSession session = dataSnapshot.getValue(ConvocationSession.class);
                // Null checking is required for handling removed item from Firebase
                if (session != null) {
                    Map<String, Boolean> programme = session.getProgrammes();
                    if (programme != null) {
                        List<String> programmeKey = new ArrayList<>();
                        for (String key : programme.keySet()) {
                            programmeKey.add(key);
                            Log.e("programmeKey", key);
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_list_item_1, programmeKey);
                        programmeList.setAdapter(adapter);
                    } else {
                        EmptyProgrammeListDialogFragment emptyProgrammeListDialogFragment = new EmptyProgrammeListDialogFragment();
                        emptyProgrammeListDialogFragment.setArguments(mBundle);
                        getFragmentManager().beginTransaction().add(emptyProgrammeListDialogFragment, null).addToBackStack(null).commit();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        // Launch a dialog to add programme into the session when user click floating action button
        FloatingActionButton addProgrammeFab = (FloatingActionButton) findViewById(R.id.add_programme_fab);
        addProgrammeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SessionProgrammeAddActivity.class);
                intent.putExtra(Constants.EXTRA_SESSION_KEY, sessionKey);
                startActivity(intent);
            }
        });

        // Set up Toolbar with back, edit and delete button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(Constants.TITLE_SESSION + " " + Constants.TITLE_PROGRAMME + "s");
        // Close sMainActivity
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
