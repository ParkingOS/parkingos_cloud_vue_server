package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class PrepayCardDeductedTb {
    private Long id;

    private String orderId;

    private String cardId;

    private String carNumber;

    private BigDecimal deductedMoney;

    private BigDecimal deductedMoneyBefore;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getDeductedMoneyBefore() {
        return deductedMoneyBefore;
    }

    public void setDeductedMoneyBefore(BigDecimal deductedMoneyBefore) {
        this.deductedMoneyBefore = deductedMoneyBefore;
    }

    public BigDecimal getDeductedMoneyAfter() {

        return deductedMoneyAfter;
    }

    public void setDeductedMoneyAfter(BigDecimal deductedMoneyAfter) {
        this.deductedMoneyAfter = deductedMoneyAfter;
    }

    private BigDecimal deductedMoneyAfter;

    private Long ctime;

    private Long parkId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId == null ? null : cardId.trim();
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber == null ? null : carNumber.trim();
    }

    public BigDecimal getDeductedMoney() {
        return deductedMoney;
    }

    public void setDeductedMoney(BigDecimal deductedMoney) {
        this.deductedMoney = deductedMoney;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }
}