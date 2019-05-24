package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderExceptionTb;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.models.OrgGroupTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.OrderExceptionService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderExceptionServiceImpl implements OrderExceptionService {


    @Autowired
    private SupperSearchService<OrderExceptionTb> supperSearchService;

    @Autowired
    CommonDao commonDao;

    Logger logger = LoggerFactory.getLogger(OrderExceptionServiceImpl.class);

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqParameterMap) {
        logger.info("===>>>异常订单："+reqParameterMap);
        OrderExceptionTb orderExceptionTb = new OrderExceptionTb();
        Long parkId = Long.parseLong(reqParameterMap.get("comid"));
        orderExceptionTb.setComid(parkId);
        JSONObject result = supperSearchService.supperSearch(orderExceptionTb,reqParameterMap);
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        Long comid = Long.parseLong(reqParameterMap.get("comid"));

        List<OrderExceptionTb> orderlist = JSON.parseArray(result.get("rows").toString(), OrderExceptionTb.class);

        logger.error("=========>>>>>>.导出订单" + orderlist.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
//        List<List<String>> bodyList = new ArrayList<List<String>>();
        if (orderlist != null && orderlist.size() > 0) {
            //{"更新时间","STR"},{"车牌号","STR"},{"订单号","STR"},{"入场时间","STR"},{"出场时间","STR"},{"现金结算","STR"},{"更新金额","STR"},{"减免金额","STR"},{"更新减免","STR"},{"出场收费员","STR"},{"更新收费员","STR"}
            String[] f = new String[]{"update_time", "car_number", "order_id_local", "create_time", "end_time", "cash_pay_before", "cash_pay_after", "reduce_amount_before", "reduce_amount_after", "out_uid_before", "out_uid_after"};
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            Map<Integer, String> passNameMap = new HashMap<Integer, String>();
            for (OrderExceptionTb orderTb : orderlist) {
//                List<String> values = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                OrmUtil<OrderExceptionTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(orderTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if (v == null) {
                        v = "";
                    }
                    if ("out_uid_after".equals(field) || "out_uid_before".equals(field)) {
                        Long uid = -1L;
                        if (Check.isLong(v + "")) {
                            uid = Long.valueOf(v + "");
                        }
                        if (uinNameMap.containsKey(uid)) {
                            values.add(uinNameMap.get(uid));
                        } else {
                            String name = getUinName(Long.valueOf(map.get(field) + ""));
                            values.add(name);
                            uinNameMap.put(uid, name);
                        }
                    } else {
                        if ("create_time".equals(field) || "end_time".equals(field)||"update_time".equals(field)) {
                            if (!"".equals(v.toString())) {
                                values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((v + "")) * 1000));
                            } else {
                                values.add("null");
                            }
                        } else {
                            values.add(v + "");
                        }
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }


    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb) commonDao.selectObjectByConditions(userInfoTb);

        String uinName = "";
        if (userInfoTb != null && userInfoTb.getNickname() != null) {
            return userInfoTb.getNickname();
        }
        return uinName;
    }
}
