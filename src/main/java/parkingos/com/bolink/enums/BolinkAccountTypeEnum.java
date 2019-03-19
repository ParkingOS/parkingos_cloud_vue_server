package parkingos.com.bolink.enums;

/**
 *
 */
public enum BolinkAccountTypeEnum {
    CASH_PREPAY(1),//缴费机现金预付
    ELE_PREPAY(2),//电子预付
    ELE_PAY(3),//电子支付
    MONTH_PAY(4),//公众号月卡续费
    UNIFIED_ORDER(5),//通用支付
    FEE(6),//手续费
    REFUND(7),//退款
    CHANGE(8),//找零
    ;
    public int type;//类型
    BolinkAccountTypeEnum(int type ) {
        this.type = type;

    }
}
