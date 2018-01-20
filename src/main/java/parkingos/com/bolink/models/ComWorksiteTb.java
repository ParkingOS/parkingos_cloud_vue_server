package parkingos.com.bolink.models;

public class ComWorksiteTb {

    private Long id;

    private Long comid;

    private String worksiteName;

    private String description;

    private Integer netType;

    private String host_name;

    private String host_memory;

    private String host_internal;

    private Integer upload_time;

    private Integer state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNetType() {
        return netType;
    }

    public void setNetType(Integer netType) {
        this.netType = netType;
    }

    public String getHost_name() {
        return host_name;
    }

    public void setHost_name(String host_name) {
        this.host_name = host_name;
    }

    public String getHost_memory() {
        return host_memory;
    }

    public void setHost_memory(String host_memory) {
        this.host_memory = host_memory;
    }

    public String getHost_internal() {
        return host_internal;
    }

    public void setHost_internal(String host_internal) {
        this.host_internal = host_internal;
    }

    public Integer getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Integer upload_time) {
        this.upload_time = upload_time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
