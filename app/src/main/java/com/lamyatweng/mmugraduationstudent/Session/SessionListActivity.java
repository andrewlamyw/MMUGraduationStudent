package com.lamyatweng.mmugraduationstudent.Session;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class SessionListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_list);

        // Receive convocation year from the Intent
        Intent intent = getIntent();
        final int convocationYear = intent.getIntExtra(Constants.EXTRA_CONVOCATION_YEAR, 0);

        // Populate sessions sort by session number from Firebase into ListView
        final SessionCustomAdapter adapter = new SessionCustomAdapter(this);
        final Firebase sessionRef = new Firebase(Constants.FIREBASE_STRING_SESSIONS_REF);
        Query sessionQuery = sessionRef.orderByChild("convocationYear").equalTo(convocationYear);
        sessionQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ConvocationSession session;
                adapter.clear();
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    session = sessionSnapshot.getValue(ConvocationSession.class);
                    adapter.add(session);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        ListView sessionListView = (ListView) findViewById(R.id.session_list_view);
        sessionListView.setAdapter(adapter);

        // Launch a dialog to display programme details
        sessionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final ConvocationSession selectedSession = (ConvocationSession) parent.getItemAtPosition(position);

                // Retrieve selected session key from Firebase and save in intent
                Query queryRef = sessionRef.orderByChild("id").equalTo(selectedSession.getId());
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                            ConvocationSession firebaseSession = sessionSnapshot.getValue(ConvocationSession.class);
                            if (firebaseSession.getSessionNumber() == selectedSession.getSessionNumber()) {
                                Intent intent = new Intent(getApplicationContext(), SessionDisplayDetailActivity.class);
                                intent.putExtra(Constants.EXTRA_SESSION_KEY, sessionSnapshot.getKey());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        // Launch a dialog to add new student when user click floating action button
        FloatingActionButton addSessionFab = (FloatingActionButton) findViewById(R.id.add_session_fab);
        addSessionFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SessionAddActivity.class);
                intent.putExtra(Constants.EXTRA_CONVOCATION_YEAR, convocationYear);
                startActivity(intent);
            }
        });

        // Set Toolbar back button
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Set Toolbar title
        toolbar.setTitle(Constants.TITLE_SESSION + "s of " + convocationYear);
    }
}
