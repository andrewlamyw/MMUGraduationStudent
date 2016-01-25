package com.lamyatweng.mmugraduationstudent.Session;

import java.util.Map;

public class ConvocationSession {
    int columnSize;
    int convocationYear;
    String date;
    String endTime;
    int id;
    Map<String, Boolean> programmes;
    int rowSize;
    int sessionNumber;
    String startTime;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public ConvocationSession() {
    }

    public ConvocationSession(int columnSize, int convocationYear, String date, String endTime, int id, Map<String, Boolean> programmes, int rowSize, int sessionNumber, String startTime) {
        this.columnSize = columnSize;
        this.convocationYear = convocationYear;
        this.date = date;
        this.endTime = endTime;
        this.id = id;
        this.programmes = programmes;
        this.rowSize = rowSize;
        this.sessionNumber = sessionNumber;
        this.startTime = startTime;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getConvocationYear() {
        return convocationYear;
    }

    public String getDate() {
        return date;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getId() {
        return id;
    }

    public Map<String, Boolean> getProgrammes() {
        return programmes;
    }

    public int getRowSize() {
        return rowSize;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public String getStartTime() {
        return startTime;
    }
}
