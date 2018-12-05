package parkingos.com.bolink.models;

public class ParkMessageSet {
    private Long id;

    private Integer beforeNotice;

    private Integer sendFreq;

    private Integer noticeSwitch;

    private Long createTime;

    private Long updateTime;

    private Long parkId;

    private Integer messageCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBeforeNotice() {
        return beforeNotice;
    }

    public void setBeforeNotice(Integer beforeNotice) {
        this.beforeNotice = beforeNotice;
    }

    public Integer getSendFreq() {
        return sendFreq;
    }

    public void setSendFreq(Integer sendFreq) {
        this.sendFreq = sendFreq;
    }

    public Integer getNoticeSwitch() {
        return noticeSwitch;
    }

    public void setNoticeSwitch(Integer noticeSwitch) {
        this.noticeSwitch = noticeSwitch;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public Integer getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(Integer messageCount) {
        this.messageCount = messageCount;
    }
}