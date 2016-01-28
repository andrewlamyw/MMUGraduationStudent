package com.lamyatweng.mmugraduationstudent.Student;

public class Student {
    int balanceCreditHour;
    double cgpa;
    String email;
    String faculty;
    double financialDue;
    String id;
    String level;
    int muet;
    String name;
    String programme;
    String status;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public Student() {
    }

    public Student(int balanceCreditHour, double cgpa, String email, String faculty,
                   double financialDue, String id, String level, int muet, String name,
                   String programme, String status) {
        this.balanceCreditHour = balanceCreditHour;
        this.cgpa = cgpa;
        this.email = email;
        this.faculty = faculty;
        this.financialDue = financialDue;
        this.id = id;
        this.level = level;
        this.muet = muet;
        this.name = name;
        this.programme = programme;
        this.status = status;
    }

    public int getBalanceCreditHour() {
        return balanceCreditHour;
    }

    public double getCgpa() {
        return cgpa;
    }

    public String getEmail() {
        return email;
    }

    public String getFaculty() {
        return faculty;
    }

    public double getFinancialDue() {
        return financialDue;
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public int getMuet() {
        return muet;
    }

    public String getName() {
        return name;
    }

    public String getProgramme() {
        return programme;
    }

    public String getStatus() {
        return status;
    }
}
