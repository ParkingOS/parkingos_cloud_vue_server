package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkDataMapper;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.BolinkAccountTypeEnum;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.*;

@Service
public class ParkOrderanlysisServiceImpl implements ParkOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(ParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private OrderServer orderServer;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkDataMapper bolinkDataMapper;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        //map 里面  放 comid_start


        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = Long.parseLong(reqmap.get("comid"));


        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        logger.info("=====查询车场日报:"+reqmap);

        Long btime = null;
        Long etime = null;
        int istoday = 1;
        if(date==null||"".equals(date)){
//            btime = TimeTools.getThisWeekBeginTime();
            btime = TimeTools.getToDayBeginTime();
            etime =TimeTools.getToDayBeginTime()+86399;
        }else {
            String[] dateArr = date.split("至");
            String end = dateArr[1];
            String start =dateArr[0];
            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(start);
            etime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(end);
        }

        if(btime<TimeTools.getToDayBeginTime()){
            istoday =0 ;
        }


        Double cash_money = 0.0d;//所有的现金金额
        Double ele_money=0.0d;//所有的电子金额
        Double out_money=0.0d;//所有的支出金额
        Double ele_prepay_money=0.0d;//所有的电子预付金额
        Double ele_pay_money=0.0d;//所有的电子结算金额
        Double month_money=0.0d;//所有的月卡金额
        Double prepay_card=0.0d;//所有的储值卡(通用支付)
        Double cash_pay_money=0.0d;//所有的现金结算
        Double cash_prepay_money=0.0d;//所有的现金预付金额;
        Double free_money=0.0d;//所有的免费金额
        Double refund_money=0.0d;//所有的退款金额；
        Double fee_money=0.0d;//所有的手续费金额；
        Double change_money=0.0d;//所有的找零金额；

        List<Map<String,String>> backList = new ArrayList<>();

        if(istoday==1){
            //如果是查询今天的数据  那么是动态的数据

            Long cityId = commonService.getCityIdByComid(comid);
            String tableName = "order_tb_new";
            if(cityId!=null&&cityId>-1){
                reqmap.put("cityId",cityId+"");
                tableName += "_"+cityId%100;
            }
            reqmap.put("tableName",tableName);
            reqmap.put("comid_start",comid+"");
            reqmap.put("end_time","between");
                    reqmap.put("end_time_start",btime+"");
            reqmap.put("end_time_end",etime+"");

            List<Map<String,String>> cashList = orderServer.selectParkDayAnlysis(reqmap);
            logger.info("====>>>>>>车场日报："+cashList);

            /*
            * 查询交易订单记录
            *   第一步  根据comid查询unionId
            *
            * */
            String bolinkTableName = commonService.getTableNameByComid(comid,1);
            List<Map<String,Object>> inTransactions = bolinkDataMapper.getTransactions(bolinkTableName,btime,etime,comid);

            /*
            * 根据、、查询支出记录
            *
            * */
            bolinkTableName=commonService.getTableNameByComid(comid,2);
            List<Map<String,Object>> outTransactions = bolinkDataMapper.getTransactions(bolinkTableName,btime,etime,comid);

            //把所有的日期组织成list
            List<String> timeList = new ArrayList<>();
            if(cashList!=null&&cashList.size()>0){
                for(Map<String,String> cashMap:cashList){
                    timeList.add(cashMap.get("e_time"));
                }
            }
            if(inTransactions!=null&&inTransactions.size()>0){
                for(Map<String,Object> inMap:inTransactions){
                    if (!timeList.contains(inMap.get("p_time")+"")) {
                        timeList.add(inMap.get("p_time") + "");
                    }
                }
            }
            if(outTransactions!=null&&outTransactions.size()>0){
                for(Map<String,Object> outMap:outTransactions){
                    if (!timeList.contains(outMap.get("p_time")+"")) {
                        timeList.add(outMap.get("p_time") + "");
                    }
                }
            }

            Collections.sort(timeList);

            if(timeList!=null&&timeList.size()>0) {
                for (String time : timeList) {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("e_time", time);
                    resultMap.put("ele_prepay", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("ele_pay", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("month_pay", StringUtils.formatDouble(0.0d) + "");
                    //通用支付。
                    resultMap.put("prepay_card", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("ele_total", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("cash_pay", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("cash_prepay", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("cash_total", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("free_pay", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("refund", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("fee", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("change", StringUtils.formatDouble(0.0d) + "");
                    resultMap.put("expense_total", StringUtils.formatDouble(0.0d) + "");
                    Double cash_total = 0.0d;//某一天的现金金额
                    Double eleTotal = 0.0d;//某一天的电子金额
                    Double outTotal = 0.0d;//某一天的支出金额
                    if (cashList != null && cashList.size() > 0) {
                        for (Map<String, String> cashMap : cashList) {
                            if (time.equals(cashMap.get("e_time"))) {
                                cash_money += StringUtils.formatDouble(cashMap.get("cash_pay"));
                                cash_pay_money += StringUtils.formatDouble(cashMap.get("cash_pay"));
                                cash_total += StringUtils.formatDouble(cashMap.get("cash_pay"));
                                free_money += StringUtils.formatDouble(cashMap.get("free_pay"));
                                resultMap.put("cash_pay", StringUtils.formatDouble(cashMap.get("cash_pay")) + "");
                                //                            resultMap.put("cash_total",StringUtils.formatDouble(cash_total)+"");
                                resultMap.put("free_pay", StringUtils.formatDouble(cashMap.get("free_pay")) + "");
                                break;
                            }
                        }
                    }
                    if (inTransactions != null && inTransactions.size() > 0) {
                        for (Map<String, Object> inMap : inTransactions) {
                            if (time.equals(inMap.get("p_time"))) {
                                int type = (int) inMap.get("type");
                                if (type == BolinkAccountTypeEnum.CASH_PREPAY.type) {
                                    cash_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    cash_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    cash_total += StringUtils.formatDouble(inMap.get("pay_money"));
                                    resultMap.put("cash_prepay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.ELE_PREPAY.type) {
                                    ele_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    ele_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                    resultMap.put("ele_prepay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.ELE_PAY.type) {
                                    ele_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    ele_pay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                    resultMap.put("ele_pay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.MONTH_PAY.type) {
                                    ele_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    month_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                    resultMap.put("month_pay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.UNIFIED_ORDER.type) {
                                    ele_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                    prepay_card += StringUtils.formatDouble(inMap.get("pay_money"));
                                    eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                    resultMap.put("prepay_card", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                                }
                            }
                        }
                    }
                    resultMap.put("ele_total", StringUtils.formatDouble(eleTotal) + "");
                    resultMap.put("cash_total", StringUtils.formatDouble(cash_total) + "");

                    if (outTransactions != null && outTransactions.size() > 0) {
                        for (Map<String, Object> outMap : outTransactions) {
                            if (time.equals(outMap.get("p_time"))) {
                                int type = (int) outMap.get("type");
                                if (type == BolinkAccountTypeEnum.FEE.type) {
                                    out_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    fee_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    outTotal += StringUtils.formatDouble(outMap.get("pay_money"));
                                    resultMap.put("fee", StringUtils.formatDouble(outMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.REFUND.type) {
                                    out_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    refund_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    outTotal += StringUtils.formatDouble(outMap.get("pay_money"));
                                    resultMap.put("refund", StringUtils.formatDouble(outMap.get("pay_money")) + "");
                                } else if (type == BolinkAccountTypeEnum.CHANGE.type) {
                                    out_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    change_money += StringUtils.formatDouble(outMap.get("pay_money"));
                                    outTotal += StringUtils.formatDouble(outMap.get("pay_money"));
                                    resultMap.put("change", StringUtils.formatDouble(outMap.get("pay_money")) + "");
                                }
                            }
                        }
                    }
                    resultMap.put("expense_total", StringUtils.formatDouble(outTotal) + "");
                    backList.add(resultMap);
                }
            }
        }else{
            List<Map<String,Object>> parkAnlys = bolinkDataMapper.getParkDaylyStaticAnls(comid,btime,etime);
            logger.info("===>>>>>backList:"+parkAnlys);

            if(parkAnlys==null||parkAnlys.isEmpty()){
                return result;
            }

            for(Map<String,Object> map:parkAnlys){

                cash_money+=StringUtils.formatDouble(map.get("cash_total"));
                ele_money+=StringUtils.formatDouble(map.get("ele_total"));
                out_money+=StringUtils.formatDouble(map.get("out_total"));
                ele_prepay_money+=StringUtils.formatDouble(map.get("ele_prepay"));
                ele_pay_money+=StringUtils.formatDouble(map.get("ele_pay"));
                month_money+=StringUtils.formatDouble(map.get("monthcard_pay"));
                prepay_card+=StringUtils.formatDouble(map.get("other_pay"));
                cash_pay_money+=StringUtils.formatDouble(map.get("cash_pay"));
                cash_prepay_money+=StringUtils.formatDouble(map.get("cash_prepay"));
                free_money+=StringUtils.formatDouble(map.get("reduce"));
                refund_money+=StringUtils.formatDouble(map.get("refund"));
                fee_money+=StringUtils.formatDouble(map.get("fee"));
                change_money+=StringUtils.formatDouble(map.get("recharge"));

                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("e_time",map.get("pay_time_day_str")+"");
                resultMap.put("ele_prepay",StringUtils.formatDouble(map.get("ele_prepay"))+"");
                resultMap.put("ele_pay",StringUtils.formatDouble(map.get("ele_pay"))+"");
                resultMap.put("month_pay",StringUtils.formatDouble(map.get("monthcard_pay"))+"");
                resultMap.put("prepay_card",StringUtils.formatDouble(map.get("other_pay"))+"");
                resultMap.put("ele_total",StringUtils.formatDouble(map.get("ele_total"))+"");
                resultMap.put("cash_pay",StringUtils.formatDouble(map.get("cash_pay"))+"");
                resultMap.put("cash_prepay",StringUtils.formatDouble(map.get("cash_prepay"))+"");
                resultMap.put("cash_total",StringUtils.formatDouble(map.get("cash_total"))+"");
                resultMap.put("free_pay",StringUtils.formatDouble(map.get("reduce"))+"");
                resultMap.put("refund",StringUtils.formatDouble(map.get("refund"))+"");
                resultMap.put("fee",StringUtils.formatDouble(map.get("fee"))+"");
                resultMap.put("change",StringUtils.formatDouble(map.get("recharge"))+"");
                resultMap.put("expense_total",StringUtils.formatDouble(map.get("out_total"))+"");
                backList.add(resultMap);
            }
        }




        /*
        * 4.0实时查数据  下面是增加了定时任务 每天凌晨跑前一天的数据 成静态表
        *
        * */

        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("e_time","合计");
        resultMap.put("ele_prepay",StringUtils.formatDouble(ele_prepay_money)+"");
        resultMap.put("ele_pay",StringUtils.formatDouble(ele_pay_money)+"");
        resultMap.put("month_pay",StringUtils.formatDouble(month_money)+"");
        resultMap.put("prepay_card",StringUtils.formatDouble(prepay_card)+"");
        resultMap.put("ele_total",StringUtils.formatDouble(ele_money)+"");
        resultMap.put("cash_pay",StringUtils.formatDouble(cash_pay_money)+"");
        resultMap.put("cash_prepay",StringUtils.formatDouble(cash_prepay_money)+"");
        resultMap.put("cash_total",StringUtils.formatDouble(cash_money)+"");
        resultMap.put("free_pay",StringUtils.formatDouble(free_money)+"");
        resultMap.put("refund",StringUtils.formatDouble(refund_money)+"");
        resultMap.put("fee",StringUtils.formatDouble(fee_money)+"");
        resultMap.put("change",StringUtils.formatDouble(change_money)+"");
        resultMap.put("expense_total",StringUtils.formatDouble(out_money)+"");
        backList.add(resultMap);

        result.put("rows",JSON.toJSON(backList));
        return result;

    }

    @Override
    public JSONObject selectWorkdetail(String bt, String et, String fieldsstr, String pay_type, String type, Long uid, Long comid,String date) {

        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


//        String btime = "";
//        String etime = "";
//        if(date==null||"".equals(date)){
//            btime= nowtime + " 00:00:00";
//            etime = nowtime + " 23:59:59";
//        }else {
//            String[] dateArr = date.split("至");
//            btime = dateArr[0];
//            etime = dateArr[1];
//        }

        Long btime = TimeTools.getToDayBeginTime();
        Long etime = System.currentTimeMillis()/1000;
        List list = null;//daService.getPage(sql, null, 1, 20);
//        if(type.equals("today")){
//        }else if(type.equals("toweek")){
//            btime = TimeTools.getWeekStartSeconds();
//        }else if(type.equals("lastweek")){
//            etime = TimeTools.getWeekStartSeconds();
//            btime= etime-7*24*60*60;
//            etime = etime-1;
//        }else if(type.equals("tomonth")){
//            btime=TimeTools.getMonthStartSeconds();
//        }else if(type.equals("custom")){
//            if(bt.length()==10){
//                bt = bt + " 00:00:00";
//            }
//            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(bt);
//            etime =  TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(et+" 23:59:59");
//        }else if(!bt.equals("")&&!et.equals("")){
//            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(bt);
//            etime =  TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(et+" 23:59:59");
//        }
        String sql = "select a.id,a.start_time,a.end_time,a.uid,b.worksite_name worksite_id " +
                " from " +
                " parkuser_work_record_tb a left join " +
                " com_worksite_tb b  on b.id=a.worksite_id  " +
                "where a.start_time is not null and (a.end_time  between "+btime+" and "+etime+" or a.start_time between "+btime+" and "+etime+" or (a.start_time between "+btime+" and "+etime+" and " +
                "(a.end_time> "+etime+" or a.end_time is null))) and a.uid = "+uid;// order by a.end_time desc";//查询上班信息
        sql +=" order by a.end_time desc";
        logger.error(sql);
        list = commonDao.getObjectBySql(sql);


        double amountmoney = 0.0;//总金额
        double cashpay = 0.0;//现金结算金额
        double cashprepay = 0.0;//现金预付金额
        double elec_money = 0.0;//电子支付金额
        double reduce_money = 0.0;//减免支付金额
        double free_money = 0.0;//减免支付金额
        int count =0;
        int monthcount =0;
        for (int i = 0; i < list.size(); i++) { //循环组织每个班的统计
            Map work = (Map)list.get(i);
            long start_time = (Long)work.get("start_time");
            long end_time = Long.MAX_VALUE;
            try {
                end_time = (Long)work.get("end_time");
            } catch (Exception e) {
            }
            List alllist = new ArrayList();//总的订单

            //总的订单数和总的金额
            String allsql = "select count(*) ordertotal,sum(amount_receivable) amount_receivable, " +
                    "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
                    "sum(reduce_amount) reduce_pay from order_tb_new where end_time between "+start_time+" and  " +
                    end_time+" and state= 1 and out_uid = "+uid+" and ishd=? and comid= "+comid;
            alllist = commonDao.getObjectBySql(allsql);

            //月卡订单数
            String monthsql = "select count(*) ordertotal from order_tb_new where end_time between "+start_time+" and " + end_time +
                    " and state= 1 and out_uid = "+uid+" and pay_type =3 and ishd=0 and comid="+comid;
            Map monthList = (Map) commonDao.getObjectBySql(monthsql).get(0);

            work.put("monthcount", monthList.get("ordertotal"));
            monthcount+=Integer.parseInt(monthList.get("ordertotal")+"");
            count+=Integer.parseInt((((Map)alllist.get(0)).get("ordertotal"))+"");
            if(alllist!=null&&alllist.size()==1){
                Map<String,Object> oMap = (Map)alllist.get(0);
                int ordertotal = 0;
                double totalMOney = 0 ;
                try{
                    ordertotal = Integer.parseInt((oMap.get("ordertotal"))+"");
                    totalMOney = Double.parseDouble((oMap.get("amount_receivable"))+"");

                }catch (Exception e) {
                    totalMOney=0.0;
                }
                work.put("ordertotal",ordertotal);
                work.put("total",StringUtils.formatDouble(totalMOney));
                amountmoney+=totalMOney;
                //现金结算
                cashpay += StringUtils.formatDouble(oMap.get("cash_pay"));
                work.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(oMap.get("cash_pay"))));
                //现金预付
                cashprepay += StringUtils.formatDouble(oMap.get("cash_prepay"));
                work.put("cash_prepay",String.format("%.2f",StringUtils.formatDouble(oMap.get("cash_prepay"))));


                //电子支付
                elec_money += StringUtils.formatDouble(oMap.get("electronic_pay"))+StringUtils.formatDouble(oMap.get("electronic_prepay"));
                work.put("electronic_pay", StringUtils.formatDouble(oMap.get("electronic_pay"))+StringUtils.formatDouble(oMap.get("electronic_prepay")));
                //减免券支付
                reduce_money +=StringUtils.formatDouble(oMap.get("reduce_pay"));
                work.put("reduce_pay", StringUtils.formatDouble(oMap.get("reduce_pay")));
            }
            //免费订单集合
            String freesql = "select sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay from order_tb_new where end_time between "+start_time+" and ? " +end_time+
                    " and state= 1 and out_uid = "+uid+" and pay_type =8 and ishd=0 and comid="+comid;
            Map freelist = (Map)commonDao.getObjectBySql(freesql).get(0);
            //免费支付
            free_money += Double.parseDouble((freelist.get("free_pay")== null ? "0" : freelist.get("free_pay")+""));
            work.put("free_pay", StringUtils.formatDouble(Double.parseDouble(freelist.get("free_pay")== null ? "0" : (freelist.get("free_pay")+""))));
        }
        String title = "总订单数："+count+"，月卡订单数："+monthcount+"，总结算金额："+String.format("%.2f",amountmoney)+"元，其中现金结算："+String.format("%.2f",cashpay)
                +"元，现金预付 : "+String.format("%.2f",cashprepay)+"元, 电子支付 ："+StringUtils.formatDouble(elec_money)+"元，" +
                "免费金额："+String.format("%.2f",free_money)+"元,减免券支付："+String.format("%.2f",reduce_money)+"元";

        result.put("rows",JSON.toJSON(list));
        result.put("title",title);
        result.put("total",list.size());
        result.put("page",1);

        return result;
    }

    @Override
    public JSONObject getOrderdetail(Long uid, String btime, String etime, String type, Long comid) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        String sql = "select *,(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay from order_tb_new  ";
        //统计总订单数，金额，电子和现金支付
        String countsql = "select count(*) ordertotal,sum(amount_receivable) amount_receivable, " +
                "sum(total) total , sum(cash_prepay) cash_prepay,sum(cash_pay) cash_pay, sum(electronic_pay) electronic_pay," +
                "sum(electronic_prepay) electronic_prepay,sum(reduce_amount) reduce_pay from order_tb_new";

        Long b = TimeTools.getToDayBeginTime();
        Long e = System.currentTimeMillis()/1000;

        sql +=" where end_time between "+b+" and "+e+"  and out_uid="+uid+"  and state= 1 and comid="+comid+" and ishd=0  ";
        countsql +=" where end_time between "+b+" and "+e+"  and out_uid="+uid+"  and state= 1 and comid="+comid+" and ishd=0  ";

        double amountmoney = 0.0;//总金额
        double cashpay = 0.0;//现金结算金额
        double cashprepay = 0.0;//现金预付金额
        double elec_money = 0.0;//电子支付金额
        int count =0;
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        if(uid!=-2){
            List<Map<String, Object>> orders = commonDao.getObjectBySql(sql+"order by end_time desc");
            for(Map<String, Object> order : orders){
                Map<String, Object> work = new HashMap<String, Object>();
                //编号
                work.put("id", order.get("id"));
                //停车日期
                work.put("create_time", order.get("create_time"));
                //结算日期
                work.put("end_time", order.get("end_time"));
                //订单金额
                work.put("amount_receivable", order.get("amount_receivable"));
                //现金支付  order.get("amount_receivable")
//					work.put("cashMoney", StringUtils.formatDouble(order.get("cash_pay"))+StringUtils.formatDouble(order.get("cash_prepay")));
                //现金结算
                work.put("cash_pay", StringUtils.formatDouble(order.get("cash_pay")));
                //现金预付
                work.put("cash_prepay", StringUtils.formatDouble(order.get("cash_prepay")));
                //电子支付
                work.put("elecMoney", StringUtils.formatDouble(order.get("electronic_prepay"))+StringUtils.formatDouble(order.get("electronic_pay")));
                //+ Double.parseDouble((order.get("electronic_prepay")== null ? "0" : order.get("electronic_prepay")+"")));
                //月卡
                //work.put("monthCard", order.get(""));
                //免费支付
                work.put("freeMoney",0.0);
                if(order.get("pay_type")!=null && Integer.parseInt(order.get("pay_type")+"")==8){
                    work.put("freeMoney", StringUtils.formatDouble(Double.parseDouble(order.get("free_pay")== null ? "0" : (order.get("free_pay")+""))));
                }
                //减免支付
                work.put("reduceMoney", order.get("reduce_amount"));
                //停车时长
                work.put("duration", order.get("duration"));
                //支付方式
                work.put("pay_type", order.get("pay_type"));
                //NFC卡号
                work.put("nfc_uuid", order.get(""));
                //车牌号
                work.put("car_number", order.get("car_number"));
                //查看车辆图片
                work.put("order_id_local", order.get("order_id_local"));
                list.add(work);
            }
            List<Map<String, Object>> orderList = commonDao.getObjectBySql(countsql);
            Double reduce_pay = 0.0;
            if(orderList!=null && orderList.size()>0){
                Map<String, Object> map = orderList.get(0);
                amountmoney = StringUtils.formatDouble((map.get("amount_receivable")));
                cashpay = StringUtils.formatDouble((map.get("cash_pay")));
                cashprepay = StringUtils.formatDouble((map.get("cash_prepay")));
                elec_money = StringUtils.formatDouble((map.get("electronic_pay")))+StringUtils.formatDouble((map.get("electronic_prepay")));
                count+=Integer.parseInt((map.get("ordertotal"))+"");
                reduce_pay = StringUtils.formatDouble((map.get("reduce_pay")));
            }
            String title = StringUtils.formatDouble(amountmoney)+"元，其中现金结算："+String.format("%.2f",cashpay)+"元，" +
                    "现金预付 : "+String.format("%.2f",cashprepay)+"元, 电子支付 ："+String.format("%.2f",elec_money)+"元，减免券支付："+String.format("%.2f",reduce_pay)+"元，共"+count+"条";
            result.put("rows",JSON.toJSON(list));
            result.put("page",1);
            result.put("total",list.size());
            result.put("title",title);
            return result;
        }else {
            return result;
        }
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
                values.add(map.get("e_time"));
                values.add(map.get("ele_prepay"));
                values.add(map.get("ele_pay"));
                values.add(map.get("month_pay"));
                values.add(map.get("prepay_card"));
                values.add(map.get("ele_total"));
                values.add(map.get("cash_prepay"));
                values.add(map.get("cash_pay"));
                values.add(map.get("cash_total"));
                values.add(map.get("refund"));
                values.add(map.get("fee"));
                values.add(map.get("change"));
                values.add(map.get("expense_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
