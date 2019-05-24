package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.mybatis.mapper.OrderTbMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.ComPassTb;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.CityOrderService;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cityorderSpring")
public class CityOrderServiceImpl implements CityOrderService {

    Logger logger = LoggerFactory.getLogger(CityOrderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderTbMapper orderTbMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderServer orderServer;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        //需要显示数据的集合
        List<OrderTb> list =null;
        //统计满足条件的所有订单的集合  用来统计价格
        List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();

        //查询今天的数据显示
        logger.info("=========req"+reqmap);

        Long groupid = Long.parseLong(reqmap.get("groupid"));
        Long cityid = -1L;
        List<Long> comList = null;
        if(groupid!=null&&groupid>-1){
            cityid = orderMapper.getCityIdByGroupId(groupid);
            comList = orderMapper.getComlistByGroupid(groupid);
        }
        if(comList==null||comList.isEmpty()){
            return result;
        }
        reqmap.put("comList",JSON.toJSONString(comList));

        if(cityid!=null&&cityid>-1){
            reqmap.put("cityId",cityid+"");
            reqmap.put("tableName","order_tb_new_"+cityid%100);
        }else{
            reqmap.put("tableName","order_tb_new");
        }
        String endTime = reqmap.get("end_time");
        //组装出场车辆时间参数   默认今天出场
        if(endTime==null||"undefined".equals(endTime)||"".equals(endTime)){
            reqmap.put("end_time","1");
            reqmap.put("end_time_start",(TimeTools.getToDayBeginTime()+""));
        }

        String rp = "20";
        if(reqmap.get("rp")!=null){
            rp = reqmap.get("rp");
        }

        count = orderServer.selectOrdersCount(reqmap);
        if(count>0){
            //价格统计不需要分页  要查询所有
            //带分页的 要显示在页面  的数据list
            if(reqmap.get("export")==null){//不是导出
                reqmap.put("rp",rp);
            }
            list=orderServer.getOrdersByMapConditons(reqmap);
            if (list != null && !list.isEmpty()) {
                for (OrderTb orderTb1 : list) {
                    OrmUtil<OrderTb> otm = new OrmUtil<OrderTb>();
                    Map<String, Object> map = otm.pojoToMap(orderTb1);
                    Long start = (Long) map.get("create_time");
                    Long end = (Long) map.get("end_time");
                    if (start != null && end != null) {
                        map.put("duration",StringUtils.getTimeString(start, end));
                    } else {
                        map.put("duration","");
                    }
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }
        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        return result;
    }

    private List<OrderTb> getOrdersListByGroupid(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
        example.createCriteria().andIshdEqualTo(0);
        example.createCriteria().andStateEqualTo(1);
        example = ExampleUtis.createOrderExample(example,reqmap);
        logger.info("example~~~~~~~~"+example);
        List<OrderTb> orders = orderMapper.selectOrders(example);
        return orders;
    }

    private int getOrdersCountByGroupid(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
        example.createCriteria().andIshdEqualTo(0);
        example.createCriteria().andStateEqualTo(1);
        reqmap.remove("rp");
        example = ExampleUtis.createOrderExample(example,reqmap);
        int count = orderMapper.selectOrdersCount(example);
        logger.info("count~~~~~~~~"+count);
        return count;
    }

    private Map getMoneyMap(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
        example.createCriteria().andIshdEqualTo(0);
        example = ExampleUtis.createOrderExample(example,reqmap);
        Map map = orderTbMapper.selectMoneyByExample(example);
        return map;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("rp");
        //标记为导出
        reqParameterMap.put("export","1");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<OrderTb> orderlist = JSON.parseArray(result.get("rows").toString(), OrderTb.class);

        logger.error("=========>>>>>>.导出订单" + orderlist.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (orderlist != null && orderlist.size() > 0) {
            String[] f = new String[]{"id","comid", "uin", "out_uid","c_type", "car_number","car_type", "create_time", "end_time", "duration", "pay_type", "freereasons","amount_receivable", "total", "electronic_prepay", "cash_prepay", "electronic_pay", "cash_pay", "reduce_amount",  "state", "in_passid", "out_passid","order_id_local"};
            Map<Long, String> passNameMap = new HashMap<Long, String>();
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            Map<Long, String> comNameMap = new HashMap<Long, String>();
            for (OrderTb orderTb : orderlist) {
                List<Object> values = new ArrayList<Object>();
                OrmUtil<OrderTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(orderTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if(v == null)
                        v = "";
                    if("c_type".equals(field)){
                        try{
                            switch(Integer.valueOf(v + "")){
                                case 0:values.add("NFC刷卡");break;
                                case 1:values.add("Ibeacon");break;
                                case 2:values.add("手机扫牌");break;
                                case 3:values.add("通道扫牌");break;
                                case 4:values.add("直付");break;
                                case 5:values.add("全天月卡");break;
                                case 6:values.add("车位二维码");break;
                                case 7:values.add("月卡第二辆车");break;
                                case 8:values.add("分段月卡");break;
                                default:values.add("");
                            }
                        }catch (Exception e){
                            values.add(v);
                        }
                    }else if("comid".equals(field)){
                        Long comid = -1L;
                        if (Check.isLong(v + ""))
                            comid = Long.valueOf(v + "");
                        if (comNameMap.containsKey(comid)) {
                            values.add(comNameMap.get(comid));
                        } else {
                            String name = getComName(Long.valueOf(map.get(field) + ""));
                            values.add(name);
                            comNameMap.put(comid, name);
                        }

                    }else if ("uid".equals(field) || "out_uid".equals(field)) {
                        Long uid = -1L;
                        if (Check.isLong(v + ""))
                            uid = Long.valueOf(v + "");
                        if (uinNameMap.containsKey(uid)) {
                            values.add(uinNameMap.get(uid));
                        } else {
                            String name = getUinName(Long.valueOf(map.get(field) + ""));
                            values.add(name);
                            uinNameMap.put(uid, name);
                        }
                    } else if("uin".equals(field)){
                        values.add(map.get("out_uid"));
                    }else if ("pay_type".equals(field)) {
                        switch (Integer.valueOf(v + "")) {//0:NFC,1:IBeacon,2:照牌   3通道照牌 4直付 5月卡用户
                            case 0: values.add("账户支付");break;
                            case 1: values.add("现金支付");break;
                            case 2: values.add("手机支付");break;
                            case 3: values.add("包月");break;
                            case 8: values.add("免费");break;
                            default: values.add("");
                        }
                    }
                    else if("duration".equals(field)){
                        Long start = (Long)map.get("create_time");
                        Long end = (Long)map.get("end_time");
                        if(start != null && end != null){
                            values.add(StringUtils.getTimeString(start, end));
                        }else{
                            values.add("");
                        }
                    }else if("state".equals(field)){
                        switch(Integer.valueOf(v + "")){
                            case 0:values.add("未支付");break;
                            case 1:values.add("已支付");break;
                            default:values.add("");
                        }
                    }else if("in_passid".equals(field) || "out_passid".equals(field)){
                        if (!"".equals(v.toString()) && Check.isNumber(v.toString())) {
                            Long passId = Long.valueOf(v.toString());
                            if (passNameMap.containsKey(passId))
                                values.add(passNameMap.get(passId));
                            else {
                                String passName = getPassName(passId);
                                values.add(passName);
                                passNameMap.put(passId, passName);
                            }
                        } else {
                            values.add(v + "");
                        }
                    }else if("create_time".equals(field) || "end_time".equals(field)){
                        if(!"".equals(v.toString())){
                            values.add(TimeTools.getTime_yyyyMMdd_HHmmss(Long.valueOf((v+""))*1000));
                        }else{
                            values.add("");
                        }
                    }else if("total".equals(field)){
                        //"electronic_prepay", "cash_prepay", "electronic_pay", "cash_pay"
                        Double elePrepay = Double.parseDouble(map.get("electronic_prepay")+"");
                        Double elePay = Double.parseDouble(map.get("electronic_pay")+"");
                        Double cashPrepay = Double.parseDouble(map.get("cash_prepay")+"");
                        Double cashPay = Double.parseDouble(map.get("cash_pay")+"");
                        Double total = StringUtils.formatDouble(elePrepay+elePay+cashPrepay+cashPay);
                        values.add(total + "");
                    }else{
                        values.add(v + "");
                    }
                }
                bodyList.add(values);
            }
        }
        return bodyList;
    }


    private String getPassName(Long passId) {
        ComPassTb comPassTb = new ComPassTb();
        comPassTb.setId(passId);
        comPassTb = (ComPassTb)commonDao.selectObjectByConditions(comPassTb);
        if(comPassTb!=null&&comPassTb.getPassname()!=null){
            return comPassTb.getPassname();
        }
        return "";
    }

    private String getUinName(Long uin) {
        UserInfoTb userInfoTb = new UserInfoTb();
        userInfoTb.setId(uin);
        userInfoTb = (UserInfoTb)commonDao.selectObjectByConditions(userInfoTb);
        if(userInfoTb!=null&&userInfoTb.getNickname()!=null){
            return userInfoTb.getNickname();
        }
        return "";
    }

    private String getComName(Long comid){
        ComInfoTb comInfoTb  = new ComInfoTb();
        comInfoTb.setId(comid);
        comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
        if(comInfoTb!=null&&comInfoTb.getCompanyName()!=null){
            return comInfoTb.getCompanyName();
        }
        return "";
    }
}
