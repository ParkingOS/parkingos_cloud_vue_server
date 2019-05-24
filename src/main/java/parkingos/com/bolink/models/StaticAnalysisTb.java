package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class StaticAnalysisTb {
    private Long id;

    private Long parkId;

    private Long cityId;

    private Long createTime;

    private Long payTime;

    private Long groupId;

    private BigDecimal cashPay;

    private BigDecimal cashPrepay;

    private BigDecimal elePrepay;

    private BigDecimal elePay;

    private String payTimeDayStr;

    private String payTimeMonthStr;

    private BigDecimal monthcardPay;

    private BigDecimal otherPay;

    private BigDecimal fee;

    private BigDecimal refund;

    private BigDecimal recharge;

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

    public BigDecimal getCashPay() {
        return cashPay;
    }

    public void setCashPay(BigDecimal cashPay) {
        this.cashPay = cashPay;
    }

    public BigDecimal getCashPrepay() {
        return cashPrepay;
    }

    public void setCashPrepay(BigDecimal cashPrepay) {
        this.cashPrepay = cashPrepay;
    }

    public BigDecimal getElePrepay() {
        return elePrepay;
    }

    public void setElePrepay(BigDecimal elePrepay) {
        this.elePrepay = elePrepay;
    }

    public BigDecimal getElePay() {
        return elePay;
    }

    public void setElePay(BigDecimal elePay) {
        this.elePay = elePay;
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

    public BigDecimal getMonthcardPay() {
        return monthcardPay;
    }

    public void setMonthcardPay(BigDecimal monthcardPay) {
        this.monthcardPay = monthcardPay;
    }

    public BigDecimal getOtherPay() {
        return otherPay;
    }

    public void setOtherPay(BigDecimal otherPay) {
        this.otherPay = otherPay;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getRefund() {
        return refund;
    }

    public void setRefund(BigDecimal refund) {
        this.refund = refund;
    }

    public BigDecimal getRecharge() {
        return recharge;
    }

    public void setRecharge(BigDecimal recharge) {
        this.recharge = recharge;
    }

    public BigDecimal getReduce() {
        return reduce;
    }

    public void setReduce(BigDecimal reduce) {
        this.reduce = reduce;
    }
}