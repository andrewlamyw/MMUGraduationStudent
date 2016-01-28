package com.lamyatweng.mmugraduationstudent;

public class SessionProgramme {
    String name;
    String faculty;
    String level;
    int sessionID;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public SessionProgramme() {
    }

    public SessionProgramme(String name, String faculty, String level, int sessionID) {
        this.name = name;
        this.faculty = faculty;
        this.level = level;
        this.sessionID = sessionID;
    }

    public String getName() {
        return name;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getLevel() {
        return level;
    }

    public int getSessionID() {
        return sessionID;
    }
}
