package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class StaticAnalysisCollectorTb {
    private Long id;

    private Long parkId;

    private Long cityId;

    private Long createTime;

    private Long payTime;

    private Long groupId;

    private Long outUid;

    private BigDecimal cashPay;

    private BigDecimal cashPrepay;

    public BigDecimal getCashPrepay() {
        return cashPrepay;
    }

    public void setCashPrepay(BigDecimal cashPrepay) {
        this.cashPrepay = cashPrepay;
    }

    private String payTimeDayStr;

    private String payTimeMonthStr;

    private BigDecimal reduce;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getOutUid() {
        return outUid;
    }

    public void setOutUid(Long outUid) {
        this.outUid = outUid;
    }

    public BigDecimal getCashPay() {
        return cashPay;
    }

    public void setCashPay(BigDecimal cashPay) {
        this.cashPay = cashPay;
    }

    public String getPayTimeDayStr() {
        return payTimeDayStr;
    }

    public void setPayTimeDayStr(String payTimeDayStr) {
        this.payTimeDayStr = payTimeDayStr == null ? null : payTimeDayStr.trim();
    }

    public String getPayTimeMonthStr() {
        return payTimeMonthStr;
    }

    public void setPayTimeMonthStr(String payTimeMonthStr) {
        this.payTimeMonthStr = payTimeMonthStr == null ? null : payTimeMonthStr.trim();
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}