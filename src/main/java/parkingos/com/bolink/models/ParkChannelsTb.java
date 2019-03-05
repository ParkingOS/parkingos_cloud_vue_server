package parkingos.com.bolink.models;

public class ParkChannelsTb {
    private Long id;

    private Long parkId;

    private String localId;

    private String localName;

    private String localIdNoVersion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getLocalId() {
        return localId;
    }

    public void setLocalId(String localId) {
        this.localId = localId == null ? null : localId.trim();
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName == null ? null : localName.trim();
    }

    public String getLocalIdNoVersion() {
        return localIdNoVersion;
    }

    public void setLocalIdNoVersion(String localIdNoVersion) {
        this.localIdNoVersion = localIdNoVersion == null ? null : localIdNoVersion.trim();
    }
}