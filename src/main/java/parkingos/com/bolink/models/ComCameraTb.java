package parkingos.com.bolink.models;

public class ComCameraTb {

    private Long id;

    private String camera_name;

    private String ip;

    private String port;

    private String cusername;

    private String cassword;

    private String manufacturer;

    private Long passid;

    private Integer state;

    private Integer upload_time;

    private Long comid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getCusername() {
        return cusername;
    }

    public void setCusername(String cusername) {
        this.cusername = cusername;
    }

    public String getCassword() {
        return cassword;
    }

    public void setCassword(String cassword) {
        this.cassword = cassword;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Long getPassid() {
        return passid;
    }

    public void setPassid(Long passid) {
        this.passid = passid;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getUpload_time() {
        return upload_time;
    }

    public void setUpload_time(Integer upload_time) {
        this.upload_time = upload_time;
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }
}
