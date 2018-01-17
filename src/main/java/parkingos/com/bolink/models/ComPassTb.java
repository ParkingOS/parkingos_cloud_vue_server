package parkingos.com.bolink.models;

public class ComPassTb {

    private Long id;

    private Integer worksite_id;

    private String passname;

    private String passtype;

    private String description;

    private Long comid;

    private Integer state;

    private Integer month_set;

    private Integer month2_set;

    private String channel_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getWorksite_id() {
        return worksite_id;
    }

    public void setWorksite_id(Integer worksite_id) {
        this.worksite_id = worksite_id;
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

    public Integer getMonth_set() {
        return month_set;
    }

    public void setMonth_set(Integer month_set) {
        this.month_set = month_set;
    }

    public Integer getMonth2_set() {
        return month2_set;
    }

    public void setMonth2_set(Integer month2_set) {
        this.month2_set = month2_set;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }
}
