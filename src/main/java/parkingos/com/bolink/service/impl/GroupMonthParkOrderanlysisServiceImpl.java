package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.controller.OrderServiceController;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.GroupMonthParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GroupMonthParkOrderanlysisServiceImpl implements GroupMonthParkOrderAnlysisService {

    Logger logger = Logger.getLogger(GroupMonthParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderServiceController orderServiceController;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) throws Exception{

        String strq = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(strq);


        Long groupid = Long.parseLong(reqmap.get("groupid"));

        Long cityid = orderMapper.getCityIdByGroupId(groupid);
        String tableName = "order_tb_new";
        if(cityid!=null&&cityid>-1){
            tableName += "_"+cityid%100;
        }

        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        String btime = reqmap.get("btime");
        String etime = reqmap.get("etime");
        Long b= null;
        Long e = null;
        if(!Check.isEmpty(btime)){
            b = TimeTools.getLongMilliSecondFrom_YYYYMM(btime)/1000;
        }else{
            b=TimeTools.getMonthStartSeconds();
        }
        if (etime!=null&&!"".equals(etime)) {
            e =TimeTools.getDateFromStr2(etime)/1000;
        }else{
            e = TimeTools.getNextMonthStartMillis()/1000;
        }
        logger.info("======>>>btime:"+b);
        logger.info("======>>>etime:"+e);

        reqmap.put("end_time_start",b+"");
        reqmap.put("end_time_end",e+"");


        List<Map<String,String>> backList = orderServiceController.selectCityMonthAnlysis(reqmap);

//        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");
//        String nowtime = df2.format(System.currentTimeMillis());
//
//
//        String btime = reqmap.get("btime");
//        String etime = reqmap.get("etime");
//        Long b= null;
//        Long e = null;
//        if(!Check.isEmpty(btime)){
//            b = TimeTools.getLongMilliSecondFrom_YYYYMM(btime)/1000;
//        }else{
////            b=TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(TimeTools.getThisYearBeginTime());
//            b=TimeTools.getMonthStartSeconds();
//        }
//        if (etime!=null&&!"".equals(etime)) {
//            e =TimeTools.getDateFromStr2(etime)/1000;
//        }else{
//            e = TimeTools.getNextMonthStartMillis()/1000;
//        }
//        logger.info("======>>>btime:"+b);
//        logger.info("======>>>etime:"+e);
//
//        List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
//        int totalCount = 0;//总订单数
//        double totalMoney = 0.0;//订单金额
//        double cashMoney = 0.0;//现金支付金额
//        double elecMoney = 0.0;//电子支付金额
//        double actFreeMoney = 0.0;//免费金额+减免支付
//
//
//        String sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
//                "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
//                "sum(reduce_amount) reduce_pay,to_char(to_timestamp(end_time),'yyyy-MM') e_time from "+tableName+" where groupid="+groupid;
//        String free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,to_char(to_timestamp(end_time),'yyyy-MM') e_time from "+tableName+" where groupid="+groupid;
//
////            List parkList = commonMethods.getParks(Long.parseLong(reqmap.get("groupid")));
////            String preParams  ="";
////            if(parkList!=null&&!parkList.isEmpty()){
////                for(Object parkid : parkList){
////                    if(preParams.equals(""))
////                        preParams =parkid+"";
////                    else
////                        preParams += ","+parkid;
////                }
////                sql +=" in ("+ preParams+" )  and end_time  ";
////                free_sql +=" in ( "+preParams+" )  and end_time";
////            }else{
////                return result;
////            }
//
//
//        sql +=" and end_time between "+b+" and "+e;
//        free_sql +=" and end_time between "+b+" and "+e;
//
//        sql +=" and state= 1 and out_uid > -1 and ishd=0 group by e_time limit 12";
//        free_sql +=" and state= 1 and out_uid >-1 and ishd=0 and pay_type=8 group by e_time limit 12";
//
//        //总订单集合
//        List<Map<String, Object>> totalList =commonDao.getObjectBySql(sql);
//        //免费订单集合
//        List<Map<String, Object>> freeList = commonDao.getObjectBySql(free_sql);
//
//        if (totalList != null && totalList.size() > 0) {
//            for(int i=0;i<totalList.size();i++) {
//                if (Integer.parseInt(totalList.get(i).get("scount") + "") > 0) {
//                    totalList.get(i).put("sdate", totalList.get(i).get("e_time"));
//
//                    totalCount += Integer.parseInt(totalList.get(i).get("scount") + "");
//
//                    totalMoney += Double.parseDouble(totalList.get(i).get("amount_receivable") + "");
//
//                    //格式化应收
//                    totalList.get(i).put("amount_receivable", String.format("%.2f", StringUtils.formatDouble(Double.parseDouble(totalList.get(i).get("amount_receivable") + ""))));
//
//                    //现金支付
//                    cashMoney += StringUtils.formatDouble(totalList.get(i).get("cash_pay")) + StringUtils.formatDouble(totalList.get(i).get("cash_prepay"));
//                    totalList.get(i).put("cash_pay", String.format("%.2f", StringUtils.formatDouble(totalList.get(i).get("cash_pay")) + StringUtils.formatDouble(totalList.get(i).get("cash_prepay"))));
//                    //电子支付
//                    elecMoney += StringUtils.formatDouble(totalList.get(i).get("electronic_pay")) + StringUtils.formatDouble(totalList.get(i).get("electronic_prepay"));
//                    totalList.get(i).put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(totalList.get(i).get("electronic_pay")) + StringUtils.formatDouble(totalList.get(i).get("electronic_prepay"))));
//                    //每一行的合计 = 现金支付+电子支付
//                    totalList.get(i).put("act_total", String.format("%.2f", StringUtils.formatDouble(Double.parseDouble(totalList.get(i).get("cash_pay") + "") + Double.parseDouble(totalList.get(i).get("electronic_pay") + ""))));
//
//                    //减免支付
//                    double reduceAmount = StringUtils.formatDouble(Double.parseDouble((totalList.get(i).get("reduce_pay") == null ? "0.00" : totalList.get(i).get("reduce_pay") + "")));
//                    double actFreePay = reduceAmount;
//                    //遍历免费集合
//                    if (freeList != null && freeList.size() > 0) {
//                        for (Map<String, Object> freeOrder : freeList) {
//                            if(freeOrder.get("e_time").equals(totalList.get(i).get("e_time"))){
//                                double freePay = StringUtils.formatDouble(Double.parseDouble((freeOrder.get("free_pay") == null ? "0.00" : freeOrder.get("free_pay") + "")));
//                                actFreePay = freePay+reduceAmount;
//                            }
//                        }
//                    }
//                    actFreeMoney += actFreePay;
//                    totalList.get(i).put("free_pay",  String.format("%.2f",actFreePay));
//                    backList.add(totalList.get(i));
//                }
//            }
//        }
//
//        if (backList!=null&&backList.size() > 0) {
//            Map totalMap = new HashMap();
//            totalMap.put("sdate", "合计");
//            totalMap.put("scount", totalCount);
//            totalMap.put("amount_receivable", String.format("%.2f", StringUtils.formatDouble(totalMoney)));
//            totalMap.put("cash_pay", String.format("%.2f", StringUtils.formatDouble(cashMoney)));
//            totalMap.put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(elecMoney)));
//            totalMap.put("free_pay", String.format("%.2f", StringUtils.formatDouble(actFreeMoney)));
//            totalMap.put("act_total", String.format("%.2f", StringUtils.formatDouble((cashMoney + elecMoney))));
//            backList.add(totalMap);
//        }

        result.put("rows",JSON.toJSON(backList));
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) throws Exception{

        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");

        //获得要导出的结果
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<Object> resList = JSON.parseArray(result.get("rows").toString());

        logger.error("=========>>>>>>.导出订单" + resList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (resList != null && resList.size() > 0) {
            for (Object object : resList) {
                Map<String,Object> map = (Map)object;
                List<Object> values = new ArrayList<Object>();
                values.add(map.get("sdate"));
                values.add(map.get("scount"));
                values.add(map.get("amount_receivable"));
                values.add(map.get("cash_pay"));
                values.add(map.get("electronic_pay"));
                values.add(map.get("act_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
