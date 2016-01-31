package com.lamyatweng.mmugraduationstudent;

public class Order {
    Boolean attendance;
    Double fee;
    String gratitudeMessage;
    int numberOfGuest;
    String paymentDate;
    String robeSize;
    int seatID1;
    int seatID2;
    int sessionID;
    String studentID;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Order() {
    }

    public Order(Boolean attendance, Double fee, String paymentDate, int sessionID, String studentID) {
        this.attendance = attendance;
        this.fee = fee;
        this.paymentDate = paymentDate;
        this.sessionID = sessionID;
        this.studentID = studentID;
    }

    public Order(Boolean attendance, Double fee, String gratitudeMessage, int numberOfGuest, String paymentDate, String robeSize, int sessionID, String studentID) {
        this.attendance = attendance;
        this.fee = fee;
        this.gratitudeMessage = gratitudeMessage;
        this.numberOfGuest = numberOfGuest;
        this.paymentDate = paymentDate;
        this.robeSize = robeSize;
        this.sessionID = sessionID;
        this.studentID = studentID;
    }

    public Order(Boolean attendance, Double fee, String gratitudeMessage, int numberOfGuest, String paymentDate, String robeSize, int seatID1, int sessionID, String studentID) {
        this.attendance = attendance;
        this.fee = fee;
        this.gratitudeMessage = gratitudeMessage;
        this.numberOfGuest = numberOfGuest;
        this.paymentDate = paymentDate;
        this.robeSize = robeSize;
        this.seatID1 = seatID1;
        this.sessionID = sessionID;
        this.studentID = studentID;
    }

    public Order(Boolean attendance, Double fee, String gratitudeMessage, int numberOfGuest, String paymentDate, String robeSize, int seatID1, int seatID2, int sessionID, String studentID) {
        this.attendance = attendance;
        this.fee = fee;
        this.gratitudeMessage = gratitudeMessage;
        this.numberOfGuest = numberOfGuest;
        this.paymentDate = paymentDate;
        this.robeSize = robeSize;
        this.seatID1 = seatID1;
        this.seatID2 = seatID2;
        this.sessionID = sessionID;
        this.studentID = studentID;
    }

    public Boolean getAttendance() {
        return attendance;
    }

    public Double getFee() {
        return fee;
    }

    public String getGratitudeMessage() {
        return gratitudeMessage;
    }

    public int getNumberOfGuest() {
        return numberOfGuest;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getRobeSize() {
        return robeSize;
    }

    public int getSeatID1() {
        return seatID1;
    }

    public int getSeatID2() {
        return seatID2;
    }

    public int getSessionID() {
        return sessionID;
    }

    public String getStudentID() {
        return studentID;
    }
}
