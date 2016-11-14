package com.apin.special.ticket.po;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Student {

    private Integer id;

    private Integer classId;

    private String studentName;

    private String studentBirthday;

    private String studentSex;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentBirthday() {
        return studentBirthday;
    }

    public void setStudentBirthday(String studentBirthday) {
        this.studentBirthday = studentBirthday;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        sb.append("studentName:"+studentName);
        sb.append(",studentBirthday:"+studentBirthday);
        sb.append(",studentSex:"+studentSex);
        sb.append("}");
        return sb.toString();
    }
}
