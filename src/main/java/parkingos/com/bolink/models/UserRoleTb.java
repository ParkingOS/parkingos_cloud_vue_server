package parkingos.com.bolink.models;

public class UserRoleTb {
    private Long id;

    private String roleName;

    private Integer state;

    private Long oid;

    private String resume;

    private Integer type;

    private Long adminid;

    private Integer isInspect;

    private Integer isCollector;

    private Integer isOpencard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getOid() {
        return oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume == null ? null : resume.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAdminid() {
        return adminid;
    }

    public void setAdminid(Long adminid) {
        this.adminid = adminid;
    }

    public Integer getIsInspect() {
        return isInspect;
    }

    public void setIsInspect(Integer isInspect) {
        this.isInspect = isInspect;
    }

    public Integer getIsCollector() {
        return isCollector;
    }

    public void setIsCollector(Integer isCollector) {
        this.isCollector = isCollector;
    }

    public Integer getIsOpencard() {
        return isOpencard;
    }

    public void setIsOpencard(Integer isOpencard) {
        this.isOpencard = isOpencard;
    }
}