package parkingos.com.bolink.qo;

/**
 * 分页排序查询对象
 */
public class PageOrderConfig {

    private String orderField = "id";

    private String orderType = "desc";

    private Integer limit = 20;

    private Integer offset = 0;

    public String getOrderField() {
        return orderField;
    }
    public String getOrderType() { return orderType; }

    public void setOrderInfo(String orderField, String orderType) {
        this.orderType = orderType;
        this.orderField = orderField;
    }

    public Integer getLimit() { return limit; }
    public Integer getOffset() {
        return offset;
    }

    public void setPageInfo(Integer pageNum, Integer pageSize) {
        if(pageSize==null||pageNum==null){
            this.limit = null;
            this.offset = null;
        }else{
            this.limit = pageSize;
            this.offset = pageNum==0?0:(pageNum - 1) * pageSize;
        }
    }

}
