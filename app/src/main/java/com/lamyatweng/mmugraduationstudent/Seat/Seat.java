package com.lamyatweng.mmugraduationstudent.Seat;

public class Seat {

    int id;
    String row;
    String column;
    String status;
    int sessionID;
    String studentID;

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

    public int getSessionID() {
        return sessionID;
    }

    public String getStudentID() {
        return studentID;
    }
}
