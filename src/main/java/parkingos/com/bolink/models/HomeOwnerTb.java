package parkingos.com.bolink.models;

public class HomeOwnerTb {
    private Long id;

    private String name;

    private String homeNumber;

    private String phone;

    private String identityCard;

    private Integer state;

    private String remark;

    private Long comid;

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

    public String getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(String homeNumber) {
        this.homeNumber = homeNumber == null ? null : homeNumber.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Long getComid() {
        return comid;
    }

    public void setComid(Long comid) {
        this.comid = comid;
    }

    @Override
    public String toString() {
        return "HomeOwnerTb{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", homeNumber='" + homeNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", identityCard='" + identityCard + '\'' +
                ", state=" + state +
                ", remark='" + remark + '\'' +
                ", comid=" + comid +
                '}';
    }
}