package org.diems.diemsapp;

class MarksSubject {
    String name, rollNo, marks_ct1, marks_ct2, marks_average, _class, branch, division;

    public MarksSubject(String name, String rollNo, String marks_ct1, String marks_ct2, String marks_average, String _class, String branch, String division) {
        this.name = name;
        this.rollNo = rollNo;
        this.marks_ct1 = marks_ct1;
        this.marks_ct2 = marks_ct2;
        this.marks_average = marks_average;
        this._class = _class;
        this.branch = branch;
        this.division = division;
    }
}
