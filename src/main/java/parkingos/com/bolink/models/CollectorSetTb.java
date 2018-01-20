package parkingos.com.bolink.models;

public class CollectorSetTb {
    private Long id;

    private Long roleId;

    private String photoset;

    private Integer changePrepay;

    private Integer viewPlot;

    private String printSign;

    private String prepayset;

    private Integer isprepay;

    private Integer hidedetail;

    private Integer isSensortime;

    private String password;

    private String signoutPassword;

    private Integer signoutValid;

    private Integer isShowCard;

    private Integer printOrderPlace2;

    private Short isDuplicateOrder;

    private Short isPrintName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getPhotoset() {
        return photoset;
    }

    public void setPhotoset(String photoset) {
        this.photoset = photoset == null ? null : photoset.trim();
    }

    public Integer getChangePrepay() {
        return changePrepay;
    }

    public void setChangePrepay(Integer changePrepay) {
        this.changePrepay = changePrepay;
    }

    public Integer getViewPlot() {
        return viewPlot;
    }

    public void setViewPlot(Integer viewPlot) {
        this.viewPlot = viewPlot;
    }

    public String getPrintSign() {
        return printSign;
    }

    public void setPrintSign(String printSign) {
        this.printSign = printSign == null ? null : printSign.trim();
    }

    public String getPrepayset() {
        return prepayset;
    }

    public void setPrepayset(String prepayset) {
        this.prepayset = prepayset == null ? null : prepayset.trim();
    }

    public Integer getIsprepay() {
        return isprepay;
    }

    public void setIsprepay(Integer isprepay) {
        this.isprepay = isprepay;
    }

    public Integer getHidedetail() {
        return hidedetail;
    }

    public void setHidedetail(Integer hidedetail) {
        this.hidedetail = hidedetail;
    }

    public Integer getIsSensortime() {
        return isSensortime;
    }

    public void setIsSensortime(Integer isSensortime) {
        this.isSensortime = isSensortime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getSignoutPassword() {
        return signoutPassword;
    }

    public void setSignoutPassword(String signoutPassword) {
        this.signoutPassword = signoutPassword == null ? null : signoutPassword.trim();
    }

    public Integer getSignoutValid() {
        return signoutValid;
    }

    public void setSignoutValid(Integer signoutValid) {
        this.signoutValid = signoutValid;
    }

    public Integer getIsShowCard() {
        return isShowCard;
    }

    public void setIsShowCard(Integer isShowCard) {
        this.isShowCard = isShowCard;
    }

    public Integer getPrintOrderPlace2() {
        return printOrderPlace2;
    }

    public void setPrintOrderPlace2(Integer printOrderPlace2) {
        this.printOrderPlace2 = printOrderPlace2;
    }

    public Short getIsDuplicateOrder() {
        return isDuplicateOrder;
    }

    public void setIsDuplicateOrder(Short isDuplicateOrder) {
        this.isDuplicateOrder = isDuplicateOrder;
    }

    public Short getIsPrintName() {
        return isPrintName;
    }

    public void setIsPrintName(Short isPrintName) {
        this.isPrintName = isPrintName;
    }
}