package com.lamyatweng.mmugraduationstudent.Seat;

public class Seat {

    int id;
    String row;
    String column;
    String status;
    int sessionID;
    String studentID;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Seat() {
    }

    public Seat(int id, String row, String column, String status, int sessionID, String studentID) {
        this.id = id;
        this.row = row;
        this.column = column;
        this.status = status;
        this.sessionID = sessionID;
        this.studentID = studentID;
    }

    public int getId() {
        return id;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSessionID() {
        return sessionID;
    }

    public String getStudentID() {
        return studentID;
    }
}
