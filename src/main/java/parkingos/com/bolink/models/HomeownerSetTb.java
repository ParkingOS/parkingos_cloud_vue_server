package parkingos.com.bolink.models;

public class HomeownerSetTb {
    private Long id;

    private Integer accessCert;

    private Integer autoCert;

    private Integer accessNotCert;

    private Integer autoNotCert;

    private Long comid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAccessCert() {
        return accessCert;
    }

    public void setAccessCert(Integer accessCert) {
        this.accessCert = accessCert;
    }

    public Integer getAutoCert() {
        return autoCert;
    }

    public void setAutoCert(Integer autoCert) {
        this.autoCert = autoCert;
    }

    public Integer getAccessNotCert() {
        return accessNotCert;
    }

    public void setAccessNotCert(Integer accessNotCert) {
        this.accessNotCert = accessNotCert;
    }

    public Integer getAutoNotCert() {
        return autoNotCert;
    }

    public void setAutoNotCert(Integer autoNotCert) {
        this.autoNotCert = autoNotCert;
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }
}