package com.lamyatweng.mmugraduationstudent.Convocation;

public class Convocation {
    int year;
    String openRegistrationDate;
    String closeRegistrationDate;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Convocation() {
    }

    public Convocation(int year, String openRegistrationDate, String closeRegistrationDate) {
        this.year = year;
        this.openRegistrationDate = openRegistrationDate;
        this.closeRegistrationDate = closeRegistrationDate;
    }

    public int getYear() {
        return year;
    }

    public String getOpenRegistrationDate() {
        return openRegistrationDate;
    }

    public String getCloseRegistrationDate() {
        return closeRegistrationDate;
    }
}
