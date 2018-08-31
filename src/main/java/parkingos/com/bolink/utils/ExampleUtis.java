package parkingos.com.bolink.utils;

import parkingos.com.bolink.dao.mybatis.OrderTbExample;

import java.math.BigDecimal;
import java.util.Map;

/**
 *把map转成生成example 对象
 *
 * @version
 * 
 */
public class ExampleUtis {
	public static OrderTbExample createOrderExample(OrderTbExample example,Map<String, String> reqmap){
        for(String key : reqmap.keySet()){
            switch (key) {
                case "id":
                    String value = reqmap.get("id");
                    String startValue = reqmap.get("id_start");
                    String endValue = reqmap.get("id_end");
                    if (value.equals("between")) {
                        if (Check.isLong(startValue) && Check.isLong(endValue)) {
                            example.createCriteria().andIdBetween(Long.valueOf(startValue), Long.valueOf(endValue));
                        }
                    } else if ("1".equals(value)) {
                        if (Check.isLong(startValue))
                            example.createCriteria().andIdGreaterThanOrEqualTo(Long.valueOf(startValue));
                    } else if ("2".equals(value)) {
                        if (Check.isLong(endValue))
                            example.createCriteria().andIdLessThanOrEqualTo(Long.valueOf(endValue));
                    } else if ("3".equals(value)) {
                        if (Check.isLong(startValue))
                            example.createCriteria().andIdEqualTo(Long.valueOf(startValue));
                    }
                    break;

                case "tableName":
//                    example.setCityid(Long.parseLong(reqmap.get("cityid")));
                    example.setTableName(reqmap.get("tableName"));
                    break;
                case "rp":
                    example.setLimit(Integer.parseInt(reqmap.get("rp")));
                    example.setOffset(Integer.parseInt(reqmap.get("rp")) * (Integer.parseInt(reqmap.get("page")) - 1));
                    break;
                case "orderby":
                    example.setOrderType(reqmap.get("orderby"));
                    break;
                case "orderfield":
                    if(reqmap.get("orderfield")!=null&&!"".equals(reqmap.get("orderfield"))){
                        example.setOrderByClause(reqmap.get("orderfield"));
                    }
                    break;
                case "comid":
//                    example.createCriteria().andComidEqualTo(Long.valueOf(reqmap.get("comid")));
                    String startvalue = reqmap.get("comid_start");
                    if (Check.isLong(startvalue))
                        example.createCriteria().andComidEqualTo(Long.valueOf(startvalue));
                    break;

                case "groupid":
                    String groupid = reqmap.get("groupid");
                    if (Check.isLong(groupid))
                        example.createCriteria().andGroupidEqualTo(Long.valueOf(groupid));
                    break;
                case "c_type":
                    String cType = reqmap.get("c_type");
                    if (!Check.isEmpty(cType))
                        example.createCriteria().andCTypeLike(cType);
                    break;
                case "out_uid":
                    String outUid = reqmap.get("out_uid");
                    String start = reqmap.get("out_uid_start");
                    String end = reqmap.get("out_uid_end");
                    if (outUid.equals("between")) {
                        if (Check.isLong(start) && Check.isLong(end)) {
                            example.createCriteria().andOutUidBetween(Long.valueOf(start), Long.valueOf(end));
                        }
                    } else if ("1".equals(outUid)) {
                        if (Check.isLong(start))
                            example.createCriteria().andOutUidGreaterThanOrEqualTo(Long.valueOf(start));
                    } else if ("2".equals(outUid)) {
                        if (Check.isLong(end))
                            example.createCriteria().andOutUidLessThanOrEqualTo(Long.valueOf(end));
                    } else if ("3".equals(outUid)) {
                        if (Check.isLong(start))
                            example.createCriteria().andOutUidEqualTo(Long.valueOf(start));
                    }
                    break;
                case "car_number":
                    String car_number = reqmap.get("car_number");
                    if (!Check.isEmpty(car_number))
                        example.createCriteria().andCarNumberLike(car_number);
                    break;
                case "create_time":
                    String create_time = reqmap.get("create_time");
                    String create_time_start = reqmap.get("create_time_start");
                    String create_time_end = reqmap.get("create_time_end");

                    if (create_time_start != null && create_time_start.length() > 10) {
                        create_time_start = create_time_start.substring(0, 10);
                    }
                    if (create_time_end != null && create_time_end.length() > 10) {
                        create_time_end = create_time_end.substring(0, 10);
                    }

                    if (create_time.equals("between")) {
                        if (Check.isLong(create_time_start) && Check.isLong(create_time_end)) {
                            example.createCriteria().andCreateTimeBetween(Long.valueOf(create_time_start), Long.valueOf(create_time_end));
                        }
                    } else if ("1".equals(create_time)) {
                        if (Check.isLong(create_time_start))
                            example.createCriteria().andCreateTimeGreaterThanOrEqualTo(Long.valueOf(create_time_start));
                    } else if ("2".equals(create_time)) {
                        if (Check.isLong(create_time_end))
                            example.createCriteria().andCreateTimeLessThanOrEqualTo(Long.valueOf(create_time_end));
                    } else if ("3".equals(create_time)) {
                        if (Check.isLong(create_time_start))
                            example.createCriteria().andCreateTimeEqualTo(Long.valueOf(create_time_start));
                    }
                    break;
                case "end_time":
                    String end_time = reqmap.get("end_time");
                    String end_time_start = reqmap.get("end_time_start");
                    String end_time_end = reqmap.get("end_time_end");

                    if (end_time_start != null && end_time_start.length() > 10) {
                        end_time_start = end_time_start.substring(0, 10);
                    }
                    if (end_time_end != null && end_time_end.length() > 10) {
                        end_time_end = end_time_end.substring(0, 10);
                    }
                    if (end_time.equals("between")) {
                        if (Check.isLong(end_time_start) && Check.isLong(end_time_end)) {

                            example.createCriteria().andEndTimeBetween(Long.valueOf(end_time_start), Long.valueOf(end_time_end));
                        }
                    } else if ("1".equals(end_time)) {
                        if (Check.isLong(end_time_start))
                            example.createCriteria().andEndTimeGreaterThanOrEqualTo(Long.valueOf(end_time_start));
                    } else if ("2".equals(end_time)) {
                        if (Check.isLong(end_time_end))
                            example.createCriteria().andEndTimeLessThanOrEqualTo(Long.valueOf(end_time_end));
                    } else if ("3".equals(end_time)) {
                        if (Check.isLong(end_time_start))
                            example.createCriteria().andEndTimeEqualTo(Long.valueOf(end_time_start));
                    }
                    break;

                case "pay_type":
                    String pay_type = reqmap.get("pay_type");
                    if (Check.isLong(pay_type))
                        example.createCriteria().andPayTypeEqualTo(Integer.parseInt(pay_type));
                    break;


                case "amount_receivable":
                    String amount_receivable = reqmap.get("amount_receivable");
                    String amount_receivable_start = reqmap.get("amount_receivable_start");
                    String amount_receivable_end = reqmap.get("amount_receivable_end");
                    if (amount_receivable.equals("between")) {
                        if (Check.isLong(amount_receivable_start) && Check.isLong(amount_receivable_end)) {
                            example.createCriteria().andAmountReceivableBetween(new BigDecimal(amount_receivable_start), new BigDecimal(amount_receivable_end));
                        }
                    } else if ("1".equals(amount_receivable)) {
                        if (Check.isDouble(amount_receivable_start))
                            example.createCriteria().andAmountReceivableGreaterThanOrEqualTo(new BigDecimal(amount_receivable_start));
                    } else if ("2".equals(amount_receivable)) {
                        if (Check.isDouble(amount_receivable_end))
                            example.createCriteria().andAmountReceivableLessThanOrEqualTo(new BigDecimal(amount_receivable_end));
                    } else if ("3".equals(amount_receivable)) {
                        if (Check.isDouble(amount_receivable_start))
                            example.createCriteria().andAmountReceivableEqualTo(new BigDecimal(amount_receivable_start));
                    }
                    break;

                case "total":
                    String total = reqmap.get("total");
                    String total_start = reqmap.get("total_start");
                    String total_end = reqmap.get("total_end");
                    if (total.equals("between")) {
                        if (Check.isLong(total_start) && Check.isLong(total_end)) {
                            example.createCriteria().andTotalBetween(new BigDecimal(total_start), new BigDecimal(total_end));
                        }
                    } else if ("1".equals(total)) {
                        if (Check.isDouble(total_start))
                            example.createCriteria().andTotalGreaterThanOrEqualTo(new BigDecimal(total_start));
                    } else if ("2".equals(total)) {
                        if (Check.isDouble(total_end))
                            example.createCriteria().andTotalLessThanOrEqualTo(new BigDecimal(total_end));
                    } else if ("3".equals(total)) {
                        if (Check.isDouble(total_start))
                            example.createCriteria().andTotalEqualTo(new BigDecimal(total_start));
                    }
                    break;
                case "state":
                    String state = reqmap.get("state_start");
                    if (Check.isLong(state))
                        example.createCriteria().andStateEqualTo(Integer.valueOf(state));
                    break;

                case "in_passid":
                    String in_passid = reqmap.get("in_passid");
                    if (!Check.isEmpty(in_passid))
                        example.createCriteria().andInPassidLike(in_passid);
                    break;

                case "out_passid":
                    String out_passid = reqmap.get("out_passid");
                    if (!Check.isEmpty(out_passid))
                        example.createCriteria().andOutPassidLike(out_passid);
                    break;

                case "order_id_local":
                    String order_id_local = reqmap.get("order_id_local");
                    if (!Check.isEmpty(order_id_local))
                        example.createCriteria().andOrderIdLocalLike(order_id_local);
                    break;
                default:
                    break;
            }
        }
	    return example;
    }
}
