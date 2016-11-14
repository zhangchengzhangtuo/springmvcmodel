package com.apin.special.ticket.po;

import com.apin.common.jdbc.ApinColumn;
import com.apin.common.jdbc.ApinTable;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
@ApinTable(name="apin_flight_info")
public class FlightInfo implements Serializable{

    private static final long serialVersionUID = -1730495906290671827L;

    @ApinColumn(name = "id",isPrimaryKey = true)
    private Integer id;
    private String departDate;
    private String departPlaceCode;
    private String departPlace;
    private String destPlaceCode;
    private String destPlace;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartPlaceCode() {
        return departPlaceCode;
    }

    public void setDepartPlaceCode(String departPlaceCode) {
        this.departPlaceCode = departPlaceCode;
    }

    public String getDepartPlace() {
        return departPlace;
    }

    public void setDepartPlace(String departPlace) {
        this.departPlace = departPlace;
    }

    public String getDestPlaceCode() {
        return destPlaceCode;
    }

    public void setDestPlaceCode(String destPlaceCode) {
        this.destPlaceCode = destPlaceCode;
    }

    public String getDestPlace() {
        return destPlace;
    }

    public void setDestPlace(String destPlace) {
        this.destPlace = destPlace;
    }

}
