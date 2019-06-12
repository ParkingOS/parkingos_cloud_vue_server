package parkingos.com.bolink.models;

public class GroupMessageTb {
    private Long id;

    private Long groupId;

    private Integer selectAll;

    private Long ctime;

    private Long utime;

    private String parks;


    @Override
    public String toString() {
        return "GroupMessageTb{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", selectAll=" + selectAll +
                ", ctime=" + ctime +
                ", utime=" + utime +
                ", parks='" + parks + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getSelectAll() {
        return selectAll;
    }

    public void setSelectAll(Integer selectAll) {
        this.selectAll = selectAll;
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

    public String getParks() {
        return parks;
    }

    public void setParks(String parks) {
        this.parks = parks;
    }
}