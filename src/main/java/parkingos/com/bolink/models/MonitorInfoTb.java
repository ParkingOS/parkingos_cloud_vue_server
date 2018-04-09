package parkingos.com.bolink.models;

/**
 * 监视器pojo
 */
public class MonitorInfoTb {

   private Long id;

   private Integer netStatus;

   private Integer isShow;

   private Integer showOrder;

   private String playSrc;

   private String comid;

   private String groupid;

   private Long channelId;

   private String name;

   private Integer state;

  /* PhoneInfoTb phoneInfoTb = new PhoneInfoTb();

    public Long getMonitorId() {
        return phoneInfoTb.getMonitorId();
    }

    public void setMonitorId(Long monitorId) {
        phoneInfoTb.setMonitorId(monitorId);
    }*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(Integer netStatus) {
        this.netStatus = netStatus;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getPlaySrc() {
        return playSrc;
    }

    public void setPlaySrc(String playSrc) {
        this.playSrc = playSrc;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
