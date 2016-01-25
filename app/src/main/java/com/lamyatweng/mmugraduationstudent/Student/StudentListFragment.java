package com.lamyatweng.mmugraduationstudent.Student;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class StudentListFragment extends Fragment {
    Bundle bundle = new Bundle();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_student, container, false);
        this.setHasOptionsMenu(true);

        // Populate students sort by name from Firebase into ListView
        final StudentCustomAdapter adapter = new StudentCustomAdapter(getActivity());
        Firebase.setAndroidContext(getActivity());
        final Firebase studentRef = new Firebase(Constants.FIREBASE_STRING_STUDENTS_REF);
        Query studentQuery = studentRef.orderByChild("name");
        studentQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Student student;
                adapter.clear();
                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    student = studentSnapshot.getValue(Student.class);
                    adapter.add(student);
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Snackbar.make(view, firebaseError.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
        ListView studentListView = (ListView) view.findViewById(R.id.student_list_view);
        studentListView.setAdapter(adapter);

        // Launch a dialog to display student details
        studentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Student selectedStudent = (Student) parent.getItemAtPosition(position);

                // Retrieve selected student key from Firebase and save in bundle
                Query queryRef = studentRef.orderByChild("id").equalTo(selectedStudent.getId());
                queryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                            Student firebaseStudent = studentSnapshot.getValue(Student.class);
                            if (firebaseStudent.getName().equals(selectedStudent.getName())) {
                                bundle.putString(getString(R.string.key_student_key), studentSnapshot.getKey());
                                StudentDisplayDetailDialogFragment studentDisplayDetailDialogFragment = new StudentDisplayDetailDialogFragment();
                                studentDisplayDetailDialogFragment.setArguments(bundle);
                                getFragmentManager().beginTransaction().add(studentDisplayDetailDialogFragment, null).
                                        addToBackStack(studentDisplayDetailDialogFragment.getClass().getName()).commit();
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
        FloatingActionButton addStudentFab = (FloatingActionButton) view.findViewById(R.id.add_student_fab);
        addStudentFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudentAddDialogFragment studentAddDialogFragment = new StudentAddDialogFragment();
                getFragmentManager().beginTransaction().add(studentAddDialogFragment, null).addToBackStack(null).commit();
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
            actionBar.setTitle(Constants.TITLE_STUDENT + "s");
    }

    /**
     * Set menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.student_menu, menu);
    }

    /**
     * Set menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;

            case R.id.action_filter:

                return true;

            case R.id.action_settings:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
