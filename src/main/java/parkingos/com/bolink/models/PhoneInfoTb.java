package parkingos.com.bolink.models;

/**
 * 对讲管理pojo
 */
public class PhoneInfoTb {

   private Long id;

   private String name;

   private Long telePhone;

   private String comid;

   private Long parkPhone;

   private Long groupPhone;

   private Long monitorId;

   private String groupid;

   private Integer isCall;

   private Long callTime;

   private Integer mainPhoneType;

   private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTelePhone() {
        return telePhone;
    }

    public void setTelePhone(Long telePhone) {
        this.telePhone = telePhone;
    }

    public String getComid() {
        return comid;
    }

    public void setComid(String comid) {
        this.comid = comid;
    }

    public Long getParkPhone() {
        return parkPhone;
    }

    public void setParkPhone(Long parkPhone) {
        this.parkPhone = parkPhone;
    }

    public Long getGroupPhone() {
        return groupPhone;
    }

    public void setGroupPhone(Long groupPhone) {
        this.groupPhone = groupPhone;
    }

    public Long getMonitorId() {
        return monitorId;
    }

    public void setMonitorId(Long monitorId) {
        this.monitorId = monitorId;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Integer getIsCall() {
        return isCall;
    }

    public void setIsCall(Integer isCall) {
        this.isCall = isCall;
    }

    public Long getCallTime() {
        return callTime;
    }

    public void setCallTime(Long callTime) {
        this.callTime = callTime;
    }

    public Integer getMainPhoneType() {
        return mainPhoneType;
    }

    public void setMainPhoneType(Integer mainPhoneType) {
        this.mainPhoneType = mainPhoneType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
