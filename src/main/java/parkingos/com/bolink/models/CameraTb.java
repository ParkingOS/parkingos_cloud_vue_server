package parkingos.com.bolink.models;

public class CameraTb {
    private Long id;

    private String camId;

    private String camName;

    private String camType;

    private Integer mode;

    private Long channelId;

    private Long comid;

    private Long cityid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCamId() {
        return camId;
    }

    public void setCamId(String camId) {
        this.camId = camId == null ? null : camId.trim();
    }

    public String getCamName() {
        return camName;
    }

    public void setCamName(String camName) {
        this.camName = camName == null ? null : camName.trim();
    }

    public String getCamType() {
        return camType;
    }

    public void setCamType(String camType) {
        this.camType = camType == null ? null : camType.trim();
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }

    public Long getCityid() {
        return cityid;
    }

    public void setCityid(Long cityid) {
        this.cityid = cityid;
    }

    @Override
    public String toString() {
        return "CameraTb{" +
                "id=" + id +
                ", camId='" + camId + '\'' +
                ", camName='" + camName + '\'' +
                ", camType='" + camType + '\'' +
                ", mode=" + mode +
                ", channelId=" + channelId +
                ", comid=" + comid +
                ", cityid=" + cityid +
                '}';
    }
}