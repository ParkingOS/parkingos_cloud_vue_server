package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class OrderExceptionTb {
    private Long id;

    private Long createTime;

    private Long comid;

    private Integer state;

    private Long endTime;

    private String carNumber;

    private Long groupid;

    private Long outUidBefore;

    private String orderIdLocal;

    private BigDecimal amountReceivable;

    private BigDecimal cashPayBefore;

    private BigDecimal reduceAmountBefore;

    private Long cityid;

    private Long updateTime;

    private Long outUidAfter;

    private BigDecimal cashPayAfter;

    private BigDecimal reduceAmountAfter;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public Long getGroupid() {
        return groupid;
    }

    public void setGroupid(Long groupid) {
        this.groupid = groupid;
    }

    public Long getOutUidBefore() {
        return outUidBefore;
    }

    public void setOutUidBefore(Long outUidBefore) {
        this.outUidBefore = outUidBefore;
    }

    public String getOrderIdLocal() {
        return orderIdLocal;
    }

    public void setOrderIdLocal(String orderIdLocal) {
        this.orderIdLocal = orderIdLocal == null ? null : orderIdLocal.trim();
    }

    public BigDecimal getAmountReceivable() {
        return amountReceivable;
    }

    public void setAmountReceivable(BigDecimal amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public BigDecimal getCashPayBefore() {
        return cashPayBefore;
    }

    public void setCashPayBefore(BigDecimal cashPayBefore) {
        this.cashPayBefore = cashPayBefore;
    }

    public BigDecimal getReduceAmountBefore() {
        return reduceAmountBefore;
    }

    public void setReduceAmountBefore(BigDecimal reduceAmountBefore) {
        this.reduceAmountBefore = reduceAmountBefore;
    }

    public Long getCityid() {
        return cityid;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOutUidAfter() {
        return outUidAfter;
    }

    public void setOutUidAfter(Long outUidAfter) {
        this.outUidAfter = outUidAfter;
    }

    public BigDecimal getCashPayAfter() {
        return cashPayAfter;
    }

    public void setCashPayAfter(BigDecimal cashPayAfter) {
        this.cashPayAfter = cashPayAfter;
    }

    public BigDecimal getReduceAmountAfter() {
        return reduceAmountAfter;
    }

    public void setReduceAmountAfter(BigDecimal reduceAmountAfter) {
        this.reduceAmountAfter = reduceAmountAfter;
    }
}