package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.OrderTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.CityOrderTbMapper;
import parkingos.com.bolink.dao.mybatis.mapper.OrderTbMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.*;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityOrderService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cityorderSpring")
public class CityOrderServiceImpl implements CityOrderService {

    Logger logger = Logger.getLogger(CityOrderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;
    @Autowired
    private OrderTbMapper orderTbMapper;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);



        int count =0;
        Double sumtotal = 0.00;
        Double cashpay = 0.00;
        Double elepay = 0.00;
        //需要显示数据的集合
        List<OrderTb> list =null;
        //统计满足条件的所有订单的集合  用来统计价格
        List<OrderTb> moneyList =null;
        List<Map<String, Object>> resList =new ArrayList<>();

        //查询今天的数据显示
        logger.error("=========..req"+reqmap.size());
        OrderTb orderTb = new OrderTb();

        orderTb.setState(1);
        orderTb.setIshd(0);

        //下拉选  车场 ,拼装参数车场参数
        String comidStr = reqmap.get("comid_start");
        if(comidStr!=null&&!"".equals(comidStr)){
            Long comid = Long.parseLong(comidStr);
            if(comid>-1){
                orderTb.setComid(comid);
            }
        }

        String endTime = reqmap.get("end_time");
        logger.error("===>>>endTime"+endTime);
        //组装出场车辆时间参数   默认今天出场
        if(endTime==null||"undefined".equals(endTime)||"".equals(endTime)){
            reqmap.put("end_time","1");
            reqmap.put("end_time_start",(TimeTools.getToDayBeginTime()+""));
            logger.error("=========..req"+reqmap.size());
        }

        Map searchMap = supperSearchService.getGroupOrCitySearch(orderTb,reqmap);
        if(searchMap==null){
            return result;
        }
        OrderTb baseQuery =(OrderTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);

        Map moneymap = new HashMap();
        if(count>0){
            if(config==null){
                config = new PageOrderConfig();
                config.setPageInfo(null,null);
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            }else{
                //价格统计不需要分页  要查询所有
                PageOrderConfig newPage = new PageOrderConfig();
                newPage.setPageInfo(null,null);

                moneymap = getMoneyMap(reqmap);
//                moneyList =  commonDao.selectListByConditions(baseQuery,supperQuery,newPage);
                //带分页的 要显示在页面  的数据list
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);

//                if(moneyList!=null&&!moneyList.isEmpty()){
//                    list = new ArrayList<>();
//                    Integer pageSize =Integer.valueOf(reqmap.get("rp"));
//                    Integer pageNum = Integer.valueOf(reqmap.get("page"));
//                    for(int i = (pageNum-1)*pageSize;i<pageNum*pageSize;i++){
//                        if(i<moneyList.size()){
//                            list.add(moneyList.get(i));
//                        }
//                    }
//                }
            }
            if (list != null && !list.isEmpty()) {
                for (OrderTb orderTb1 : list) {
                    OrmUtil<OrderTb> otm = new OrmUtil<>();
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

                if(moneyList!=null&&!moneyList.isEmpty()){
                    for(OrderTb newOrder:moneyList){
                        OrmUtil<OrderTb> otm = new OrmUtil<>();
                        Map<String, Object> map = otm.pojoToMap(newOrder);
                        if(!Check.isEmpty(map.get("amount_receivable")+"")){
                            sumtotal+=StringUtils.formatDouble(Double.parseDouble(map.get("amount_receivable")+""));
                        }
                        Double cashPrepay =0.00;
                        Double cashPay = 0.00;
                        Double elePrepay = 0.00;
                        Double elePay = 0.00;
                        if(!Check.isEmpty( map.get("cash_prepay")+"")){
                            cashPrepay = StringUtils.formatDouble(Double.parseDouble(map.get("cash_prepay")+""));
                        }
                        if(!Check.isEmpty( map.get("cash_pay")+"")){
                            cashPay = StringUtils.formatDouble(Double.parseDouble(map.get("cash_pay")+""));
                        }
                        cashpay+=(cashPay+cashPrepay);

                        if(!Check.isEmpty( map.get("electronic_prepay")+"")){
                            elePrepay = StringUtils.formatDouble(Double.parseDouble(map.get("electronic_prepay")+""));
                        }
                        if(!Check.isEmpty( map.get("electronic_pay")+"")){
                            elePay = StringUtils.formatDouble(Double.parseDouble(map.get("electronic_pay")+""));
                        }
                        elepay+=(elePay+elePrepay);
                    }
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }
        if(moneymap!=null){
            result.put("sumtotal",moneymap.get("total"));
            result.put("cashpay",moneymap.get("cashpay"));
            result.put("elepay",moneymap.get("elepay"));
        }else{
            result.put("sumtotal",0.00);
            result.put("cashpay",0.00);
            result.put("elepay",0.00);
        }

//
//        result.put("sumtotal",String.format("%.2f",sumtotal));
//        result.put("cashpay",String.format("%.2f",cashpay));
//        result.put("elepay",String.format("%.2f",elepay));
        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        logger.error("============>>>>>返回数据"+result);
        return result;
    }

    private Map getMoneyMap(Map<String, String> reqmap) {
        OrderTbExample example = new OrderTbExample();
//        example.createCriteria().andIdEqualTo(47198650L);
        for(String key : reqmap.keySet()){
            switch (key){
                case "id":
                    String value = reqmap.get("id");
                    String startValue = reqmap.get("id_start");
                    String endValue = reqmap.get("id_end");
                    if(value.equals("between")){
                        if(Check.isLong(startValue)&&Check.isLong(endValue)){
                            example.createCriteria().andIdBetween(Long.valueOf(startValue),Long.valueOf(endValue));
                        }
                    }else if("1".equals(value)){
                        if(Check.isLong(startValue))
                            example.createCriteria().andIdGreaterThanOrEqualTo(Long.valueOf(startValue));
                    }else if("2".equals(value)){
                        if(Check.isLong(endValue))
                            example.createCriteria().andIdLessThanOrEqualTo(Long.valueOf(endValue));
                    }else if("3".equals(value)){
                        if(Check.isLong(startValue))
                            example.createCriteria().andIdEqualTo(Long.valueOf(startValue));
                    }
                    break;
                case "comid":
                    String startvalue= reqmap.get("comid_start");
                    if(Check.isLong(startvalue))
                        example.createCriteria().andComidEqualTo(Long.valueOf(startvalue));
                    break;

                case "groupid":
                    String groupid= reqmap.get("groupid");
                    if(Check.isLong(groupid))
                        example.createCriteria().andGroupidEqualTo(Long.valueOf(groupid));
                    break;
                case "c_type":
                    String cType= reqmap.get("c_type");
                    if(!Check.isEmpty(cType))
                        example.createCriteria().andCTypeLike(cType);
                    break;
                case "out_uid":
                    String outUid = reqmap.get("out_uid");
                    String start = reqmap.get("out_uid_start");
                    String end= reqmap.get("out_uid_end");
                    if(outUid.equals("between")){
                        if(Check.isLong(start)&&Check.isLong(end)){
                            example.createCriteria().andOutUidBetween(Long.valueOf(start),Long.valueOf(end));
                        }
                    }else if("1".equals(outUid)){
                        if(Check.isLong(start))
                            example.createCriteria().andOutUidGreaterThanOrEqualTo(Long.valueOf(start));
                    }else if("2".equals(outUid)){
                        if(Check.isLong(end))
                            example.createCriteria().andOutUidLessThanOrEqualTo(Long.valueOf(end));
                    }else if("3".equals(outUid)){
                        if(Check.isLong(start))
                            example.createCriteria().andOutUidEqualTo(Long.valueOf(start));
                    }
                    break;
                case "car_number":
                    String car_number= reqmap.get("car_number");
                    if(!Check.isEmpty(car_number))
                        example.createCriteria().andCarNumberLike(car_number);
                    break;
                case "create_time":
                    String create_time = reqmap.get("create_time");
                    String create_time_start = reqmap.get("create_time_start");
                    String create_time_end = reqmap.get("create_time_end");

                    if (create_time_start!=null&&create_time_start.length() > 10) {
                        create_time_start = create_time_start.substring(0,10);
                    }
                    if (create_time_end!=null&&create_time_end.length() > 10) {
                        create_time_end=create_time_end.substring(0,10);
                    }

                    if(create_time.equals("between")){
                        if(Check.isLong(create_time_start)&&Check.isLong(create_time_end)){
                            example.createCriteria().andCreateTimeBetween(Long.valueOf(create_time_start),Long.valueOf(create_time_end));
                        }
                    }else if("1".equals(create_time)){
                        if(Check.isLong(create_time_start))
                            example.createCriteria().andCreateTimeGreaterThanOrEqualTo(Long.valueOf(create_time_start));
                    }else if("2".equals(create_time)){
                        if(Check.isLong(create_time_end))
                            example.createCriteria().andCreateTimeLessThanOrEqualTo(Long.valueOf(create_time_end));
                    }else if("3".equals(create_time)){
                        if(Check.isLong(create_time_start))
                            example.createCriteria().andCreateTimeEqualTo(Long.valueOf(create_time_start));
                    }
                    break;
                case "end_time":
                    String end_time = reqmap.get("end_time");
                    String end_time_start = reqmap.get("end_time_start");
                    String end_time_end = reqmap.get("end_time_end");

                    if (end_time_start!=null&&end_time_start.length() > 10) {
                        end_time_start = end_time_start.substring(0,10);
                    }
                    if (end_time_end!=null&&end_time_end.length() > 10) {
                        end_time_end=end_time_end.substring(0,10);
                    }
                    if(end_time.equals("between")){
                        if(Check.isLong(end_time_start)&&Check.isLong(end_time_end)){

                            example.createCriteria().andEndTimeBetween(Long.valueOf(end_time_start),Long.valueOf(end_time_end));
                        }
                    }else if("1".equals(end_time)){
                        if(Check.isLong(end_time_start))
                            example.createCriteria().andEndTimeGreaterThanOrEqualTo(Long.valueOf(end_time_start));
                    }else if("2".equals(end_time)){
                        if(Check.isLong(end_time_end))
                            example.createCriteria().andEndTimeLessThanOrEqualTo(Long.valueOf(end_time_end));
                    }else if("3".equals(end_time)){
                        if(Check.isLong(end_time_start))
                            example.createCriteria().andEndTimeEqualTo(Long.valueOf(end_time_start));
                    }
                    break;

                case "pay_type":
                    String pay_type= reqmap.get("pay_type");
                    if(Check.isLong(pay_type))
                        example.createCriteria().andPayTypeEqualTo(Integer.parseInt(pay_type));
                    break;


                case "amount_receivable":
                    String amount_receivable = reqmap.get("amount_receivable");
                    String amount_receivable_start = reqmap.get("amount_receivable_start");
                    String amount_receivable_end = reqmap.get("amount_receivable_end");
                    if(amount_receivable.equals("between")){
                        if(Check.isLong(amount_receivable_start)&&Check.isLong(amount_receivable_end)){
                            example.createCriteria().andAmountReceivableBetween(new BigDecimal(amount_receivable_start),new BigDecimal(amount_receivable_end));
                        }
                    }else if("1".equals(amount_receivable)){
                        if(Check.isDouble(amount_receivable_start))
                            example.createCriteria().andAmountReceivableGreaterThanOrEqualTo(new BigDecimal(amount_receivable_start));
                    }else if("2".equals(amount_receivable)){
                        if(Check.isDouble(amount_receivable_end))
                            example.createCriteria().andAmountReceivableLessThanOrEqualTo(new BigDecimal(amount_receivable_end));
                    }else if("3".equals(amount_receivable)){
                        if(Check.isDouble(amount_receivable_start))
                            example.createCriteria().andAmountReceivableEqualTo(new BigDecimal(amount_receivable_start));
                    }
                    break;

                case "total":
                    String total = reqmap.get("total");
                    String total_start = reqmap.get("total_start");
                    String total_end = reqmap.get("total_end");
                    if(total.equals("between")){
                        if(Check.isLong(total_start)&&Check.isLong(total_end)){
                            example.createCriteria().andTotalBetween(new BigDecimal(total_start),new BigDecimal(total_end));
                        }
                    }else if("1".equals(total)){
                        if(Check.isDouble(total_start))
                            example.createCriteria().andTotalGreaterThanOrEqualTo(new BigDecimal(total_start));
                    }else if("2".equals(total)){
                        if(Check.isDouble(total_end))
                            example.createCriteria().andTotalLessThanOrEqualTo(new BigDecimal(total_end));
                    }else if("3".equals(total)){
                        if(Check.isDouble(total_start))
                            example.createCriteria().andTotalEqualTo(new BigDecimal(total_start));
                    }
                    break;
                case "state":
                    String state= reqmap.get("state_start");
                    if(Check.isLong(state))
                        example.createCriteria().andStateEqualTo(Integer.valueOf(state));
                    break;

                case "in_passid":
                    String in_passid= reqmap.get("in_passid");
                    if(!Check.isEmpty(in_passid))
                        example.createCriteria().andInPassidLike(in_passid);
                    break;

                case "out_passid":
                    String out_passid= reqmap.get("out_passid");
                    if(!Check.isEmpty(out_passid))
                        example.createCriteria().andOutPassidLike(out_passid);
                    break;

                case "order_id_local":
                    String order_id_local= reqmap.get("order_id_local");
                    if(!Check.isEmpty(order_id_local))
                        example.createCriteria().andOrderIdLocalLike(order_id_local);
                    break;
                default:
                    break;
            }
        }
        Map map = orderTbMapper.selectMoneyByExample(example);
        return map;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderfield");
        reqParameterMap.remove("orderby");

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

//                        if (Check.isLong(v+"")){
//                            Long comid =  Long.parseLong(v+"");
//                            values.add(getComName(comid));
//                        }else {
//                            values.add("");
//                        }
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
//                    else if("comid".equals(field)){
//                        ComInfoTb comInfoTb = new ComInfoTb();
//                        if(v!=null&&Check.isNumber(v+"")){
//                            comInfoTb.setId(Long.parseLong(v+""));
//                            comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
//                            if(comInfoTb.getCompanyName()!=null){
//                                values.add(comInfoTb.getCompanyName());
//                            }else{
//                                values.add("");
//                            }
//                        }else{
//                            values.add(v+"");
//                        }
//                    }
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
