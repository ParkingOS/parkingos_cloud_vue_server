package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class PrepayCardTrade {
    private Long id;

    private String cardId;

    private Long parkId;

    private String tradeNo;

    private Long payTime;

    private Long createTime;

    private String payType;

    private String carNumber;

    private String remark;

    private BigDecimal addMoney;

    private String mobile;

    private String name;

    private BigDecimal addMoneyAfter;

    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAddMoneyAfter() {
        return addMoneyAfter;
    }

    public void setAddMoneyAfter(BigDecimal addMoneyAfter) {
        this.addMoneyAfter = addMoneyAfter;
    }

    public BigDecimal getAddMoneyBefore() {
        return addMoneyBefore;
    }

    public void setAddMoneyBefore(BigDecimal addMoneyBefore) {
        this.addMoneyBefore = addMoneyBefore;
    }

    private BigDecimal addMoneyBefore;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public Long getPayTime() {
        return payTime;
    }

    public void setPayTime(Long payTime) {
        this.payTime = payTime;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public BigDecimal getAddMoney() {
        return addMoney;
    }

    public void setAddMoney(BigDecimal addMoney) {
        this.addMoney = addMoney;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }
}