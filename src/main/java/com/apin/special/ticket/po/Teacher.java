package com.apin.special.ticket.po;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Teacher {

    private Integer id;

    private String name;

    private int age;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        sb.append("id:"+id);
        sb.append(",name:"+name);
        sb.append(",age:"+age);
        sb.append("}");
        return sb.toString();
    }
}
