package com.apin.special.ticket.vo;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/8/16.
 */
public class FlightInfoVo {

    private String arriveDate;
    private String arriveTime;
    private String departDate;
    private String departTime;
    private String departPlaceCode;
    private String departPlace;
    private String destPlaceCode;
    private String destPlace;
    private BigDecimal totalPriceInctax;
    private String routeType;


    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime;
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

    public BigDecimal getTotalPriceInctax() {
        return totalPriceInctax;
    }

    public void setTotalPriceInctax(BigDecimal totalPriceInctax) {
        this.totalPriceInctax = totalPriceInctax;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    @Override
    public String toString(){
        StringBuffer sb=new StringBuffer();
        sb.append("{");
        sb.append("departDate:"+departDate);
        sb.append(",departTime:"+departTime);
        sb.append(",arriveDate:"+arriveDate);
        sb.append(",arriveTime:"+arriveTime);
        sb.append(",departPlaceCode:"+departPlaceCode);
        sb.append(",departPlace:"+departPlace);
        sb.append(",destPlaceCode:"+destPlaceCode);
        sb.append(",destPlace:"+destPlace);
        sb.append(",totalPriceInctax:"+totalPriceInctax);
        sb.append(",routeType:"+routeType);
        sb.append("}");
        return sb.toString();
    }
}
