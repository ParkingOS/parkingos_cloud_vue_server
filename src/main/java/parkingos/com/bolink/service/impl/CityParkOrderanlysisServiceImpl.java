package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.CityParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityParkOrderanlysisServiceImpl implements CityParkOrderAnlysisService {

    Logger logger = Logger.getLogger(CityParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        String comidStr = reqmap.get("comid_start");
        System.out.println("CityParkOrderanlysis>>>>comidStr:"+comidStr);

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String nowtime= df2.format(System.currentTimeMillis());
        String sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
                "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
                "sum(reduce_amount) reduce_pay,to_char(to_timestamp(end_time),'yyyy-MM-dd') e_time from order_tb where comid";
        String free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,to_char(to_timestamp(end_time),'yyyy-MM-dd') e_time from order_tb where comid";
        String groupby = " group by to_char(to_timestamp(end_time),'yyyy-MM-dd')";
        if(Check.isNumber(comidStr)){
            sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
                    "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
                    "sum(reduce_amount) reduce_pay,to_char(to_timestamp(end_time),'yyyy-MM-dd') e_time,comid from order_tb where comid";
            free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,to_char(to_timestamp(end_time),'yyyy-MM-dd') e_time,comid from order_tb where comid";
            groupby = " group by to_char(to_timestamp(end_time),'yyyy-MM-dd'),comid";
        }

        if(Check.isNumber(comidStr)){
            sql +=" = "+Long.parseLong(comidStr)+" and end_time ";
            free_sql +=" = "+Long.parseLong(comidStr)+" and end_time ";
        }else {
            sql +=" in ( select id from com_info_tb where state<>1 and groupid= "+Long.parseLong(reqmap.get("groupid"))+" )  and end_time  ";
            free_sql +=" in ( select id from com_info_tb where state<>1 and groupid= "+Long.parseLong(reqmap.get("groupid"))+" )  and end_time  ";
        }


        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        logger.error("=====date:"+date);

        Long btime = null;
        Long etime = null;
        if(date==null||"".equals(date)){
            btime = TimeTools.getToDayBeginTime()-86400*9;
            etime =TimeTools.getToDayBeginTime()+86399;
        }else {
            String[] dateArr = date.split("至");
            System.out.println("陈博文"+dateArr.length);
            String start =dateArr[0];
            String end = dateArr[1];
            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(start);
            etime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(end);
        }
//        if(date!=null&&!Check.isEmpty(date)){
//            String start = reqmap.get("time_start");//RequestUtil.getString(request, "ctime_start");
//            String end = reqmap.get("time_end");//RequestUtil.getString(request, "ctime_end");
//            System.out.println("默认开始时间:"+start+"默认结束时间:"+end);
//            if("3".equals(date)&&Check.isEmpty(start)&&Check.isEmpty(end)){
//                date="between";
//            }else{
//                btime = Check.isLong(start)?Long.valueOf(start)/1000:TimeTools.getToDayBeginTime();
//                etime = Check.isLong(end)?Long.valueOf(end)/1000:TimeTools.getToDayBeginTime();
//            }
//
//        }
        logger.info("=====>>>>>>btime="+btime+"=====>>>etime="+etime);

//        if("between".equals(date)){
//            sql +=" between "+btime+" and "+etime;
//            free_sql +=" between "+btime+" and "+etime;
//
//        }else if("1".equals(date)){
//            sql +=" >= "+btime;
//            free_sql +=" >= "+btime;
//        }else if("2".equals(date)){
//            sql +=" <= "+etime;
//            free_sql +=" <= "+etime;
//        }else if("3".equals(date)){
////            String stime = TimeTools.getTimeStr_yyyy_MM_dd(btime*1000);
////            btime = TimeTools.getStrDateToSecond(stime+" 00:00:00");
//            logger.info(btime);
//            sql +=" = "+btime;
//            free_sql +=" = "+btime;
//        }else {
//            sql +=" between "+btime+" and "+etime;
//            free_sql +=" between "+btime+" and "+etime;
//        }

        sql +=" between "+btime+" and "+etime;
        free_sql +=" between "+btime+" and "+etime;
        sql +=" and state= 1 and out_uid > -1 and ishd=0 ";
        free_sql +=" and state= 1 and out_uid >-1 and ishd=0 ";


        logger.error("====groupby:"+groupby);
        logger.error("====sql:"+sql);
        logger.error("====free_sql:"+free_sql);
        //总订单集合
        List<Map<String, Object>> totalList =commonDao.getObjectBySql(sql +groupby+" order by  e_time ");
        //免费订单集合
        List<Map<String, Object>> freeList = commonDao.getObjectBySql(free_sql +" and pay_type=8 "+groupby+" order by e_time  ");//pgOnlyReadService.getAllMap(free_sql +" and pay_type=8 group by out_uid,comid order by scount desc ",params);
        int totalCount = 0;//总订单数
        double totalMoney = 0.0;//订单金额
        double cashMoney = 0.0;//现金支付金额
        double elecMoney = 0.0;//电子支付金额
        double actFreeMoney = 0.0;//免费金额+减免支付
        double actRecMoney =0.0;//电子结算+现金结算
        List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
        if(totalList != null && totalList.size() > 0) {
            for (Map<String, Object> totalOrder : totalList) {
                if (totalOrder.containsKey("comid")) {
                    Long comid = (Long) totalOrder.get("comid");
                    List<Map<String, Object>> list = commonDao.getObjectBySql("select company_name from com_info_tb where id =" + comid);
                    logger.error("=========车场:" + list.get(0));
                    if (list.get(0) != null && !list.get(0).isEmpty()) {
                        totalOrder.put("comid", list.get(0).get("company_name"));
                    }
                } else {
                    totalOrder.put("comid", "-");
                }
                totalCount += Integer.parseInt(totalOrder.get("scount") + "");

                totalMoney += Double.parseDouble(totalOrder.get("amount_receivable") + "");

                totalOrder.put("time", totalOrder.get("e_time"));
                //格式化应收
                totalOrder.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("amount_receivable")+""))));

                //现金支付
                cashMoney +=StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"));
                totalOrder.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"))));
                //电子支付
                elecMoney += StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"));
                totalOrder.put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"))));
                //每一行的合计 = 现金支付+电子支付
                totalOrder.put("act_total", String.format("%.2f",StringUtils.formatDouble(Double.parseDouble(totalOrder.get("cash_pay")+"")+Double.parseDouble(totalOrder.get("electronic_pay")+""))));

                //减免支付
                double reduceAmount = StringUtils.formatDouble(Double.parseDouble((totalOrder.get("reduce_pay") == null ? "0.00" : totalOrder.get("reduce_pay") + "")));
                double actFreePay = reduceAmount;
                //遍历免费集合
                if (freeList != null && freeList.size() > 0) {
                    for (Map<String, Object> freeOrder : freeList) {
                        if(freeOrder.get("e_time").equals(totalOrder.get("e_time"))){
                            double freePay = StringUtils.formatDouble(Double.parseDouble((freeOrder.get("free_pay") == null ? "0.00" : freeOrder.get("free_pay") + "")));
                            actFreePay = freePay+reduceAmount;
                            logger.error("========>>>>actFreePay"+actFreePay);
                        }
                    }
                }
                actFreeMoney+=actFreePay;
                totalOrder.put("free_pay",  String.format("%.2f",actFreePay));
                backList.add(totalOrder);
            }
        }

        if(backList.size()>0){
            Map sumMap = new HashMap();
            sumMap.put("time","合计");
            sumMap.put("comid","-");
            sumMap.put("amount_receivable",String.format("%.2f",StringUtils.formatDouble(totalMoney)));
            sumMap.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(cashMoney)));
            sumMap.put("electronic_pay",String.format("%.2f",StringUtils.formatDouble(elecMoney)));
            sumMap.put("act_total",String.format("%.2f",StringUtils.formatDouble((cashMoney+elecMoney))));
            sumMap.put("free_pay",String.format("%.2f",StringUtils.formatDouble(actFreeMoney)));
            backList.add(sumMap);
        }

        result.put("rows",JSON.toJSON(backList));
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {

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
                values.add(map.get("time"));
                values.add(map.get("comid"));
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
