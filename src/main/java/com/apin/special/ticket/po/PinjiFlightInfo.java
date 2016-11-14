package com.apin.special.ticket.po;

import com.apin.common.jdbc.ApinColumn;
import com.apin.common.jdbc.ApinTable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/14.
 */
@ApinTable(name="apin_flight_info")
public class PinjiFlightInfo implements Serializable{

    private static final long serialVersionUID = 6788560368512042349L;

    @ApinColumn(name = "id",isPrimaryKey = true)
    private Integer id;

    private Integer parentId;

    private Integer tripNumber;

    private String flightType;

    private Integer merchantId;

    private String airComp;

    private String flightNo;

    private String planeType;

    private String departPlaceCode;

    private String departPlace;

    private String departAirport;

    private String destPlaceCode;

    private String destPlace;

    private String departDate;

    private String departTime;

    private String arriveAirport;

    private String arriveDate;

    private String arriveTime;

    private Double flyingTime;

    private String travelType;

    private Integer remainTicketNum;

    private Integer teamNumber;

    private String passengerType;

    private String hasTurn;

    private String feeType;

    private String currency;

    private BigDecimal totalPriceInctax;

    private BigDecimal orginTicketPrice;

    private BigDecimal taxFee;

    private BigDecimal oilFee;

    private BigDecimal buildFee;

    private String cabinType;

    private String cabinName;

    private String source;

    private String routeType;

    private String isShelves;

    private String imgUrl;

    private BigDecimal accidentInsurancePrice;

    private BigDecimal delayDiskPrice;

    private Date createTime;

    private String lastDateTicket;

    public String getArriveDate() {
        return arriveDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(Integer tripNumber) {
        this.tripNumber = tripNumber;
    }

    public String getFlightType() {
        return flightType;
    }

    public void setFlightType(String flightType) {
        this.flightType = flightType == null ? null : flightType.trim();
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public String getAirComp() {
        return airComp;
    }

    public void setAirComp(String airComp) {
        this.airComp = airComp == null ? null : airComp.trim();
    }

    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo == null ? null : flightNo.trim();
    }

    public String getPlaneType() {
        return planeType;
    }

    public void setPlaneType(String planeType) {
        this.planeType = planeType == null ? null : planeType.trim();
    }

    public String getDepartPlaceCode() {
        return departPlaceCode;
    }

    public void setDepartPlaceCode(String departPlaceCode) {
        this.departPlaceCode = departPlaceCode == null ? null : departPlaceCode.trim();
    }

    public String getDepartPlace() {
        return departPlace;
    }

    public void setDepartPlace(String departPlace) {
        this.departPlace = departPlace == null ? null : departPlace.trim();
    }

    public String getDepartAirport() {
        return departAirport;
    }

    public void setDepartAirport(String departAirport) {
        this.departAirport = departAirport == null ? null : departAirport.trim();
    }

    public String getDestPlaceCode() {
        return destPlaceCode;
    }

    public void setDestPlaceCode(String destPlaceCode) {
        this.destPlaceCode = destPlaceCode == null ? null : destPlaceCode.trim();
    }

    public String getDestPlace() {
        return destPlace;
    }

    public void setDestPlace(String destPlace) {
        this.destPlace = destPlace == null ? null : destPlace.trim();
    }

    public String getDepartDate() {
        return departDate;
    }

    public void setDepartDate(String departDate) {
        this.departDate = departDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public String getDepartTime() {
        return departTime;
    }

    public void setDepartTime(String departTime) {
        this.departTime = departTime == null ? null : departTime.trim();
    }

    public String getArriveAirport() {
        return arriveAirport;
    }

    public void setArriveAirport(String arriveAirport) {
        this.arriveAirport = arriveAirport == null ? null : arriveAirport.trim();
    }


    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime == null ? null : arriveTime.trim();
    }

    public Double getFlyingTime() {
        return flyingTime;
    }

    public void setFlyingTime(Double flyingTime) {
        this.flyingTime = flyingTime;
    }

    public String getTravelType() {
        return travelType;
    }

    public void setTravelType(String travelType) {
        this.travelType = travelType == null ? null : travelType.trim();
    }

    public Integer getRemainTicketNum() {
        return remainTicketNum;
    }

    public void setRemainTicketNum(Integer remainTicketNum) {
        this.remainTicketNum = remainTicketNum;
    }

    public Integer getTeamNumber() {
        return teamNumber;
    }

    public void setTeamNumber(Integer teamNumber) {
        this.teamNumber = teamNumber;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType == null ? null : passengerType.trim();
    }

    public String getHasTurn() {
        return hasTurn;
    }

    public void setHasTurn(String hasTurn) {
        this.hasTurn = hasTurn == null ? null : hasTurn.trim();
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType == null ? null : feeType.trim();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency == null ? null : currency.trim();
    }

    public BigDecimal getTotalPriceInctax() {
        return totalPriceInctax;
    }

    public void setTotalPriceInctax(BigDecimal totalPriceInctax) {
        this.totalPriceInctax = totalPriceInctax;
    }

    public BigDecimal getOrginTicketPrice() {
        return orginTicketPrice;
    }

    public void setOrginTicketPrice(BigDecimal orginTicketPrice) {
        this.orginTicketPrice = orginTicketPrice;
    }

    public BigDecimal getTaxFee() {
        return taxFee;
    }

    public void setTaxFee(BigDecimal taxFee) {
        this.taxFee = taxFee;
    }

    public BigDecimal getOilFee() {
        return oilFee;
    }

    public void setOilFee(BigDecimal oilFee) {
        this.oilFee = oilFee;
    }

    public BigDecimal getBuildFee() {
        return buildFee;
    }

    public void setBuildFee(BigDecimal buildFee) {
        this.buildFee = buildFee;
    }

    public String getCabinType() {
        return cabinType;
    }

    public void setCabinType(String cabinType) {
        this.cabinType = cabinType == null ? null : cabinType.trim();
    }

    public String getCabinName() {
        return cabinName;
    }

    public void setCabinName(String cabinName) {
        this.cabinName = cabinName == null ? null : cabinName.trim();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType == null ? null : routeType.trim();
    }

    public String getIsShelves() {
        return isShelves;
    }

    public void setIsShelves(String isShelves) {
        this.isShelves = isShelves == null ? null : isShelves.trim();
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl == null ? null : imgUrl.trim();
    }

    public BigDecimal getAccidentInsurancePrice() {
        return accidentInsurancePrice;
    }

    public void setAccidentInsurancePrice(BigDecimal accidentInsurancePrice) {
        this.accidentInsurancePrice = accidentInsurancePrice;
    }

    public BigDecimal getDelayDiskPrice() {
        return delayDiskPrice;
    }

    public void setDelayDiskPrice(BigDecimal delayDiskPrice) {
        this.delayDiskPrice = delayDiskPrice;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLastDateTicket() {
        return lastDateTicket;
    }

    public void setLastDateTicket(String lastDateTicket) {
        this.lastDateTicket = lastDateTicket;
    }
}
