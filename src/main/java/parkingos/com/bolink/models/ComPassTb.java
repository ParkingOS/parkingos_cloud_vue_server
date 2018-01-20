package parkingos.com.bolink.models;

public class ComPassTb {

    private Long id;

    private Long worksiteId;

    private String passname;

    private String passtype;

    private String description;

    private Long comid;

    private Integer state;

    private Integer monthSet;

    private Integer month2Set;

    private String channelId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorksiteId() {
        return worksiteId;
    }

    public void setWorksiteId(Long worksiteId) {
        this.worksiteId = worksiteId;
    }

    public String getPassname() {
        return passname;
    }

    public void setPassname(String passname) {
        this.passname = passname;
    }

    public String getPasstype() {
        return passtype;
    }

    public void setPasstype(String passtype) {
        this.passtype = passtype;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getMonthSet() {
        return monthSet;
    }

    public void setMonthSet(Integer monthSet) {
        this.monthSet = monthSet;
    }

    public Integer getMonth2Set() {
        return month2Set;
    }

    public void setMonth2Set(Integer month2Set) {
        this.month2Set = month2Set;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
