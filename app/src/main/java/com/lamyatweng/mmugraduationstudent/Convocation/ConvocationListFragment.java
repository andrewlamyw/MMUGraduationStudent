package com.lamyatweng.mmugraduationstudent.Convocation;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Constants;
import com.lamyatweng.mmugraduationstudent.R;

public class ConvocationListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_convocation, container, false);

        // Populate convocations sort by year from Firebase into ListView
        final ConvocationCustomAdapter adapter = new ConvocationCustomAdapter(getActivity());
        Firebase.setAndroidContext(getActivity());
        final Firebase convocationRef = new Firebase(Constants.FIREBASE_STRING_CONVOCATIONS_REF);
        Query convocationQuery = convocationRef.orderByChild("year");
        convocationQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Convocation convocation;
                adapter.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    convocation = studentSnapshot.getValue(Convocation.class);
                    adapter.add(convocation);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
        ListView convocationListView = (ListView) view.findViewById(R.id.convocation_list_view);
        convocationListView.setAdapter(adapter);

        // Launch a dialog to display programme details
        convocationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Convocation selectedConvocation = (Convocation) parent.getItemAtPosition(position);

                // Retrieve selected programme key from Firebase and save in bundle
                Query queryRef = convocationRef.orderByChild("year").equalTo(selectedConvocation.getYear());
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot convocationSnapshot : dataSnapshot.getChildren()) {
                            Convocation firebaseConvocation = convocationSnapshot.getValue(Convocation.class);
                            if (firebaseConvocation.getOpenRegistrationDate().equals(selectedConvocation.getOpenRegistrationDate())) {
                                Intent intent = new Intent(getActivity(), ConvocationDisplayDetailActivity.class);
                                intent.putExtra(Constants.EXTRA_CONVOCATION_KEY, convocationSnapshot.getKey());
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
        FloatingActionButton addConvocationFab = (FloatingActionButton) view.findViewById(R.id.add_convocation_fab);
        addConvocationFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConvocationAddActivity.class);
                startActivity(intent);
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
            actionBar.setTitle(Constants.TITLE_CONVOCATION + "s");
    }
}
