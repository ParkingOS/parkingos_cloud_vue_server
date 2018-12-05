package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class KfcAuthcodePay {
    private Long id;

    private Long ctime;

    private Long utime;

    private Long unionId;

    private Long parkId;

    private BigDecimal amount;

    private String outTradeNo;

    private String returnData;

    private String requestData;

    private String callbackData;

    private Integer payState;

    private String addParams;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUnionId() {
        return unionId;
    }

    public void setUnionId(Long unionId) {
        this.unionId = unionId;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo == null ? null : outTradeNo.trim();
    }

    public String getReturnData() {
        return returnData;
    }

    public void setReturnData(String returnData) {
        this.returnData = returnData == null ? null : returnData.trim();
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData == null ? null : requestData.trim();
    }

    public String getCallbackData() {
        return callbackData;
    }

    public void setCallbackData(String callbackData) {
        this.callbackData = callbackData == null ? null : callbackData.trim();
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getAddParams() {
        return addParams;
    }

    public void setAddParams(String addParams) {
        this.addParams = addParams == null ? null : addParams.trim();
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    @Override
    public String toString() {
        return "KfcAuthcodePay{" +
                "id=" + id +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", unionId=" + unionId +
                ", parkId='" + parkId + '\'' +
                ", amount=" + amount +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", returnData='" + returnData + '\'' +
                ", requestData='" + requestData + '\'' +
                ", callbackData='" + callbackData + '\'' +
                ", payState=" + payState +
                ", addParams='" + addParams + '\'' +
                '}';
    }
}