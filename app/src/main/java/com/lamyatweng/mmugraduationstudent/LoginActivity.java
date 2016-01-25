package com.lamyatweng.mmugraduationstudent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.lamyatweng.mmugraduationstudent.Student.Student;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    Boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Destroy MainActivity so that MainActivity is removed from back stack
        MainActivity.sMainActivity.finish();

        // Redirects to LoginActivity if user is not logged in
        final TextInputLayout usernameWrapper = (TextInputLayout) findViewById(R.id.wrapper_student_id);
        final TextInputLayout passwordWrapper = (TextInputLayout) findViewById(R.id.wrapper_login_password);
        final SessionManager sessionManager = new SessionManager(getApplicationContext());
        final ProgressBar spinner = (ProgressBar) findViewById(R.id.progressBar_login);

        // Detecting Firebase Connection State
        Constants.FIREBASE_REF_CONNECTED_STUDENT.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    mIsConnected = true;
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                } else {
                    mIsConnected = false;
                    Toast.makeText(getApplicationContext(), "Not connected", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError error) {
                System.err.println("Listener was cancelled");
            }
        });

        Button loginButton = (Button) findViewById(R.id.button_login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();

                final String email = usernameWrapper.getEditText().getText().toString();
                String password = passwordWrapper.getEditText().getText().toString();

                if (!validateEmail(email)) {
                    usernameWrapper.setError("Not a valid email address!");
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                } else {
                    spinner.setVisibility(View.VISIBLE);
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    Constants.FIREBASE_REF_ROOT_STUDENT.authWithPassword(email, password, new Firebase.AuthResultHandler() {
                        @Override
                        public void onAuthenticated(AuthData authData) {
                            Query studentQuery = Constants.FIREBASE_REF_STUDENTS.orderByChild(Constants.FIREBASE_ATTR_STUDENTS_EMAIL).equalTo(email);
                            studentQuery.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    // Student profile is found in students child nodes, save student key into shared preference
                                    if (dataSnapshot.hasChildren()) {
                                        DataSnapshot studentSnapshot = dataSnapshot.getChildren().iterator().next();
                                        Student student = studentSnapshot.getValue(Student.class);
                                        sessionManager.createLoginSession(email, studentSnapshot.getKey(), student.getName());

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        // Student profile is not found in students child nodes
                                        spinner.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), "Your profile has been removed. Please contact administrator.", Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {
                                }
                            });
                        }

                        @Override
                        public void onAuthenticationError(FirebaseError firebaseError) {
                            spinner.setVisibility(View.GONE);
                            if (mIsConnected)
                                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validatePassword(String password) {
        return password.length() > 0;
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
