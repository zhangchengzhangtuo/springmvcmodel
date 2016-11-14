package com.apin.special.ticket.po;

import java.util.List;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ClassRoomWithStudent {

    private Integer id;

    private String className;

    private List<Student> students;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append("{");
        sb.append("className:"+className);
        sb.append(",students:"+students.toString());
        sb.append("}");
        return sb.toString();
    }
}
