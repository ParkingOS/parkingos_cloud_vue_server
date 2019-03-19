package parkingos.com.bolink.enums;

/**
 *
 */
public enum AddValueServerEnum {
    MESSAGE("Msg"),//购买短信
    BIGSCREEN("Screen"),//大屏
    PROGRAM("Program"),//小程序
    SHOPAPP("ShopApp"),//固定码公众号
    ;
    public String type;//类型
    AddValueServerEnum(String type ) {
        this.type = type;

    }
}
