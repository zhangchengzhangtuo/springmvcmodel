package com.apin.special.ticket.po;

import com.apin.common.jdbc.ApinColumn;
import com.apin.common.jdbc.ApinTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
@ApinTable(name="apin_flight_info")
public class FlightInfoConstruct implements Serializable{

    private static final long serialVersionUID = 2694634414511812905L;

    @ApinColumn(name = "id",isPrimaryKey = true)
    private Integer id;
    private String departDate;
    private String departPlaceCode;
    private String departPlace;
    private String destPlaceCode;
    private String destPlace;

    public FlightInfoConstruct(Integer id,String departDate,String departPlaceCode,String departPlace,String destPlaceCode,String destPlace){
        this.id=id;
        this.departDate=departDate;
        this.departPlaceCode=departPlaceCode;
        this.departPlace=departPlace;
        this.destPlaceCode=destPlaceCode;
        this.destPlace=destPlace;
    }

    @Override
    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("{");
        sb.append("departDate:"+departDate);
        sb.append(",departPlaceCode:"+departPlaceCode);
        sb.append(",departPlace:"+departPlace);
        sb.append(",destPlaceCode:"+destPlaceCode);
        sb.append(",destPlace:"+destPlace);
        sb.append("}");
        return sb.toString();
    }
}
