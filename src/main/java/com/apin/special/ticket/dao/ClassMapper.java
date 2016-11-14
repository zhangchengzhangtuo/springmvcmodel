package com.apin.special.ticket.dao;

import com.apin.special.ticket.po.ClassRoom;
import com.apin.special.ticket.po.ClassRoomWithStudent;

/**
 * Created by Administrator on 2016/8/30.
 */
public interface ClassMapper {

    public ClassRoom getClassRoomWithSelectMethod(int id);

    public ClassRoom getClassRoomWithResultMapMethod(int id);

    public ClassRoomWithStudent getClassRoomWithStudentWithSelectMethod(int id);

    public ClassRoomWithStudent getClassRoomWithStudentWithResultMapMethod(int id);
}
