package com.lamyatweng.mmugraduationstudent;

import com.firebase.client.Firebase;

public interface Constants {
    String FIREBASE_STRING_STAFF_REF = "https://mmugraduationstaff.firebaseio.com/";
    String FIREBASE_STRING_ROOT_REF = "https://mmugraduation.firebaseio.com/";
    String FIREBASE_STRING_STUDENTS_REF = "https://mmugraduation.firebaseio.com/students";
    String FIREBASE_STRING_PROGRAMMES_REF = "https://mmugraduation.firebaseio.com/programmes";
    String FIREBASE_STRING_CONVOCATIONS_REF = "https://mmugraduation.firebaseio.com/convocations";
    String FIREBASE_STRING_SESSIONS_REF = "https://mmugraduation.firebaseio.com/sessions";
    String FIREBASE_STRING_SEATS_REF = "https://mmugraduation.firebaseio.com/seats";

    Firebase FIREBASE_REF_ROOT_STUDENT = new Firebase("https://mmugraduation.firebaseio.com/");
    Firebase FIREBASE_REF_ROOT_STAFF = new Firebase("https://mmugraduationstaff.firebaseio.com/");
    Firebase FIREBASE_REF_STUDENTS = new Firebase("https://mmugraduation.firebaseio.com/students");
    Firebase FIREBASE_REF_PROGRAMMES = new Firebase("https://mmugraduation.firebaseio.com/programmes");
    Firebase FIREBASE_REF_CONVOCATIONS = new Firebase("https://mmugraduation.firebaseio.com/convocations");
    Firebase FIREBASE_REF_SESSIONS = new Firebase("https://mmugraduation.firebaseio.com/sessions");
    Firebase FIREBASE_REF_SEATS = new Firebase("https://mmugraduation.firebaseio.com/seats");
    Firebase FIREBASE_REF_CONNECTED = new Firebase("https://mmugraduation.firebaseio.com/.info/connected");

    String FIREBASE_ATTR_STUDENTS_EMAIL = "email";
    String FIREBASE_ATTR_SEATS_SESSIONID = "sessionID";
    String FIREBASE_ATTR_SEATS_ID = "id";
    String FIREBASE_ATTR_SESSIONS_ID = "id";
    String FIREBASE_ATTR_SESSIONS_ROWSIZE = "rowSize";
    String FIREBASE_ATTR_SESSIONS_CONVOCATIONYEAR = "convocationYear";
    String FIREBASE_ATTR_SESSIONS_COLUMNSIZE = "columnSize";

    String TITLE_NEWS = "News";
    String TITLE_PROGRAMME = "Programme";
    String TITLE_PROFILE = "Profile";
    String TITLE_STUDENT = "Student";
    String TITLE_GRADUATION = "Graduation";
    String TITLE_CONVOCATION = "Convocation";
    String TITLE_SEAT = "Seat";
    String TITLE_SESSION = "Session";

    String TITLE_LOGOUT = "Logout";
    String MENU_ADD = "Add";
    String MENU_SAVE = "Save";
    String MENU_EDIT = "Edit";

    String MENU_DELETE = "Delete";
    String EXTRA_CONVOCATION_KEY = "com.lamyatweng.mmugraduationstaff.CONVOCATION_KEY";
    String EXTRA_CONVOCATION_YEAR = "com.lamyatweng.mmugraduationstaff.CONVOCATION_YEAR";
    String EXTRA_SESSION_KEY = "com.lamyatweng.mmugraduationstaff.SESSION_KEY";
    String EXTRA_SESSION_ID = "com.lamyatweng.mmugraduationstaff.SESSION_ID";
    String EXTRA_SESSION_COLUMN_SIZE = "com.lamyatweng.mmugraduationstaff.SESSION_COLUMN_SIZE";
    String EXTRA_SESSION_ROW_SIZE = "com.lamyatweng.mmugraduationstaff.SESSION_ROW_SIZE";
    String EXTRA_SESSION_CONVOCATION_YEAR = "com.lamyatweng.mmugraduationstaff.SESSION_CONVOCATION_YEAR";
    String EXTRA_SEAT_ID = "com.lamyatweng.mmugraduationstaff.SEAT_ID";
    String EXTRA_SEAT_KEY = "com.lamyatweng.mmugraduationstaff.SEAT_KEY";

}
