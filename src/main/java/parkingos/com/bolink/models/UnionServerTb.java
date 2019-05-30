package parkingos.com.bolink.models;

import java.math.BigDecimal;

public class UnionServerTb {
    private Long id;

    private String name;

    private Long unionId;

    private Integer state;

    private Long ctime;

    private Long utime;

    private BigDecimal balance;

    private String phone;

    private String area;

    private String mobile;

    private Long pid;

    private Long bolinkServerId;

    private Long cityId;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

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
        this.name = name == null ? null : name.trim();
    }

    public Long getUnionId() {
        return unionId;
    }

    public void setUnionId(Long unionId) {
        this.unionId = unionId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Long getCtime() {
        return ctime;
    }

    public void setCtime(Long ctime) {
        this.ctime = ctime;
    }

    public Long getUtime() {
        return utime;
    }

    public void setUtime(Long utime) {
        this.utime = utime;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getBolinkServerId() {
        return bolinkServerId;
    }

    public void setBolinkServerId(Long bolinkServerId) {
        this.bolinkServerId = bolinkServerId;
    }
}