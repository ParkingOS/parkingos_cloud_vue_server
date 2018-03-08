package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.models.ComPassTb;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.CityOrderService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

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


    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        int count =0;
        Double sumtotal = 0.00;
        Double cashpay = 0.00;
        Double elepay = 0.00;
        List<OrderTb> list =null;
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
        OrderTb baseQuery =(OrderTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            if(config==null){
                config = new PageOrderConfig();
                config.setPageInfo(null,null);
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            }else{
                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
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

                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

//        String groupid = reqmap.get("groupid");
//        String cityid = reqmap.get("cityid");
//        System.out.println("=====groupid:"+groupid+"===cityid:"+cityid);
//
//        Map searchMap = supperSearchService.getBaseSearch(orderTb,reqmap);
//        logger.info(searchMap);
//        if(searchMap!=null&&!searchMap.isEmpty()){
//            OrderTb baseQuery =(OrderTb)searchMap.get("base");
//            List<SearchBean> supperQuery = null;
//            if(searchMap.containsKey("supper"))
//                supperQuery = (List<SearchBean>)searchMap.get("supper");
//            PageOrderConfig config = null;
//            if(searchMap.containsKey("config"))
//                config = (PageOrderConfig)searchMap.get("config");
//
//            List parks =new ArrayList();
//
//            if(groupid !=null&&!"".equals(groupid)){
//                parks = commonMethods.getParks(Long.parseLong(groupid));
//            }else if(cityid !=null&&!"".equals(cityid)){
//                parks = commonMethods.getparks(Long.parseLong(cityid));
//            }
//
//            System.out.println("=======parks:"+parks);
//
//            //封装searchbean  集团或者城市下面所有车场
//            SearchBean searchBean = new SearchBean();
//            searchBean.setOperator(FieldOperator.CONTAINS);
//            searchBean.setFieldName("comid");
//            searchBean.setBasicValue(parks);
//
//            if (supperQuery == null) {
//                supperQuery = new ArrayList<>();
//            }
//            supperQuery.add( searchBean );
//
//            count = commonDao.selectCountByConditions(baseQuery,supperQuery);
//            if(count>0){
//                list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
//                if (list != null && !list.isEmpty()) {
//
//                    for (OrderTb order : list) {
//                        OrmUtil<OrderTb> otm = new OrmUtil<>();
//                        Map<String, Object> map = otm.pojoToMap(order);
//                        sumtotal+=StringUtils.formatDouble(Double.parseDouble(map.get("total")+""));
//                        cashpay+= (StringUtils.formatDouble(Double.parseDouble(map.get("cash_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("cash_pay")+"")));
//                        elepay+= (StringUtils.formatDouble(Double.parseDouble(map.get("electronic_prepay")+""))+StringUtils.formatDouble(Double.parseDouble(map.get("electronic_pay")+"")));
//                        resList.add(map);
//                    }
//                    result.put("rows", JSON.toJSON(resList));
//                }
//            }
//        }
        result.put("sumtotal",String.format("%.2f",sumtotal));
        result.put("cashpay",String.format("%.2f",cashpay));
        result.put("elepay",String.format("%.2f",elepay));
        result.put("total",count);
        if(reqmap.get("page")!=null){
            result.put("page",Integer.parseInt(reqmap.get("page")));
        }
        logger.error("============>>>>>返回数据"+result);
        return result;
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
//        List<List<String>> bodyList = new ArrayList<List<String>>();
        if (orderlist != null && orderlist.size() > 0) {
            String[] f = new String[]{"id","comid","car_number","c_type","create_time","end_time","duration","total","prepaid","uid","collector","state","in_passid","out_passid"};
            Map<Long, String> uinNameMap = new HashMap<Long, String>();
            Map<Long, String> passNameMap = new HashMap<Long, String>();
            for (OrderTb orderTb : orderlist) {
//                List<String> values = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
                OrmUtil<OrderTb> otm = new OrmUtil<>();
                Map map = otm.pojoToMap(orderTb);
                for (String field : f) {
                    Object v = map.get(field);
                    if(v == null)
                        v = "";
                    if("c_type".equals(field)){
                        try{
                            switch(Integer.valueOf(v + "")){//0:NFC,1:IBeacon,2:照牌   3通道照牌 4直付 5月卡用户
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
                            values.add((String) v);
                        };

                    }else if("comid".equals(field)){
                        ComInfoTb comInfoTb = new ComInfoTb();
                        if(v!=null&&Check.isNumber(v+"")){
                            comInfoTb.setId(Long.parseLong(v+""));
                            comInfoTb = (ComInfoTb)commonDao.selectObjectByConditions(comInfoTb);
                            if(comInfoTb.getCompanyName()!=null){
                                values.add(comInfoTb.getCompanyName());
                            }else{
                                values.add("");
                            }
                        }else{
                            values.add(v+"");
                        }
                    }else if("duration".equals(field)){
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
                    }else if("prepaid".equals(field)){
                        if(map.get("prepaid") != null){
                            Double prepaid = Double.valueOf(map.get("prepaid") + "");
                            if(prepaid > 0){
                                values.add(prepaid + "");
                            }else{
                                values.add("");
                            }
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
}
