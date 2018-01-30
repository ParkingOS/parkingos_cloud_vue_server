package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.models.UserInfoTb;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkOrderanlysisServiceImpl implements ParkOrderAnlysisService {

    Logger logger = Logger.getLogger(ParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"total\":12,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);


        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String nowtime= df2.format(System.currentTimeMillis());
        String type = "";//reqmap.get("type");
        String sql = "select count(*) scount,sum(amount_receivable) amount_receivable, " +
                "sum(total) total , sum(cash_pay) cash_pay,sum(cash_prepay) cash_prepay, sum(electronic_pay) electronic_pay,sum(electronic_prepay) electronic_prepay, " +
                "sum(reduce_amount) reduce_pay, out_uid,comid from order_tb  ";
        String free_sql = "select count(*) scount,sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay,out_uid,comid from order_tb";
        String fieldsstr = reqmap.get("fieldsstr");
        Long comid = Long.parseLong(reqmap.get("comid"));

        String date = reqmap.get("date");
        String btime = "";
        String etime = "";
        if(date==null||"".equals(date)){
            btime= nowtime + " 00:00:00";
            etime = nowtime + " 23:59:59";
        }else {
//            date:2017-12-28 00:00:00至2018-01-27 23:59:59
            String[] dateArr = date.split("至");
            btime = dateArr[0];
            etime = dateArr[1];
        }

        String dstr = btime+"-"+etime;

//        String btime = reqmap.get("btime");//RequestUtil.processParams(request, "btime");
//        String etime = reqmap.get("etime");//RequestUtil.processParams(request, "etime");

//        if (btime==null){
//            btime="";
//        }
//        if(etime==null){
//            etime="";
//        }
//        if(btime.equals(""))
//            btime = nowtime + " 00:00:00";
//        if(etime.equals(""))
//            etime = nowtime + " 23:59:59";
//        Long b = TimeTools.getToDayBeginTime();
//        Long e = System.currentTimeMillis()/1000;
//        if(type.equals("today")){
//            sql += " where end_time between "+ b + " and "+e;
//            free_sql += " where end_time between "+ b + " and "+e;
//            dstr = "今天";
//        }else if(type.equals("toweek")){
//            b = TimeTools.getWeekStartSeconds();
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
//            dstr = "本周";
//        }else if(type.equals("lastweek")){
//            e = TimeTools.getWeekStartSeconds();
//            b= e-7*24*60*60;
//            e = e-1;
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
//            dstr = "上周";
//        }else if(type.equals("tomonth")){
//            b=TimeTools.getMonthStartSeconds();
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
//            dstr="本月";
//        }else if(!btime.equals("")&&!etime.equals("")){
//            b = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(btime);
//            e =  TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(etime);
//            sql += " where end_time between "+ b + " and "+e;
//            free_sql += " where end_time between "+ b + " and "+e;
//        }

        if(!btime.equals("")&&!etime.equals("")){
            Long b = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(btime);
            Long e =  TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(etime);
            sql += " where end_time between "+ b + " and "+e;
            free_sql += " where end_time between "+ b + " and "+e;
        }
        sql +=" and comid="+comid+ " and state= 1 and out_uid> -1 and ishd=0 ";
        free_sql +=" and comid="+comid+ " and state= 1 and out_uid> -1 and ishd=0 ";
        //总订单集合
        List<Map<String, Object>> totalList =commonDao.getObjectBySql(sql +" group by out_uid,comid order by scount desc ");
//        List<Map<String, Object>> totalList = pgOnlyReadService.getAllMap(sql +" group by out_uid,comid order by scount desc ",params);
        //月卡订单集合
        List<Map<String, Object>> monthList = commonDao.getObjectBySql(sql +" and pay_type=3 group by out_uid,comid order by scount desc ");//pgOnlyReadService.getAllMap(sql +" and pay_type=3 group by out_uid,comid order by scount desc ",params);
        //免费订单集合
        List<Map<String, Object>> freeList = commonDao.getObjectBySql(free_sql +" and pay_type=8 group by out_uid,comid order by scount desc ");//pgOnlyReadService.getAllMap(free_sql +" and pay_type=8 group by out_uid,comid order by scount desc ",params);
        int totalCount = 0;//总订单数
        int monthCount = 0;
        double cashpay = 0.0;//现金结算
        double cashprepay = 0.0;//现金预付
        double totalMoney = 0.0;//订单金额
        double cashMoney = 0.0;//现金支付金额
        double elecMoney = 0.0;//电子支付金额
        double freeMoney = 0.0;//免费金额
        double reduce_amount = 0.0;//减免支付
        List<Map<String, Object>> backList = new ArrayList<Map<String, Object>>();
        if(totalList != null && totalList.size() > 0) {
            Map<Long, String> nameMap = new HashMap<>();
            for (Map<String, Object> totalOrder : totalList) {
                Long _comid = (Long) totalOrder.get("comid");
                String names = nameMap.get(_comid);
                if (names == null) {
//                    Map<String,Object> namesMap = daService.getMap("select c.company_name,g.name from com_info_tb c left join" +
//                            " org_group_tb g on c.groupid = g.id where c.id =?",new Object[]{_comid});
                    List<Map<String, Object>> list = commonDao.getObjectBySql("select c.company_name,g.name from com_info_tb c left join" +
                            " org_group_tb g on c.groupid = g.id where c.id =" + _comid);
                    logger.error(list.get(0));
                    if (list.get(0) != null && !list.get(0).isEmpty()) {
                        nameMap.put(_comid, list.get(0).get("company_name") + "bolink" + list.get(0).get("name"));
                        totalOrder.put("comid", list.get(0).get("company_name"));
                        totalOrder.put("groupid", list.get(0).get("name"));
                    } else {
                        nameMap.put(_comid, "bolink");
                    }
                } else {
                    totalOrder.put("comid", names.split("bolink")[0]);
                    totalOrder.put("groupid", names.split("bolink")[1]);
                }
                totalCount += Integer.parseInt(totalOrder.get("scount") + "");
                totalMoney += Double.parseDouble(totalOrder.get("amount_receivable") + "");
                //设定默认值
//                String sql_worker = "select nickname from user_info_tb where id = ?";
//                Object []val_worker = new Object[]{Long.parseLong(totalOrder.get("out_uid")+"")};
//                Map worker = daService.getMap(sql_worker ,val_worker);
                UserInfoTb worker = new UserInfoTb();
                worker.setId(Long.parseLong(totalOrder.get("out_uid") + ""));
                worker = (UserInfoTb) commonDao.selectObjectByConditions(worker);

                if (worker != null && worker.getNickname() != null) {
                    //出场收费员Id
                    totalOrder.put("id", totalOrder.get("out_uid"));
                    //收费员名称
                    totalOrder.put("name", worker.getNickname());
                }
                //时间段
                totalOrder.put("sdate", dstr);
                //月卡订单数
                totalOrder.put("monthcount", 0);
                //遍历月卡集合
                if (monthList != null && monthList.size() > 0) {
                    for (Map<String, Object> monthOrder : monthList) {
                        if (totalOrder.get("out_uid").equals(monthOrder.get("out_uid"))) {
                            monthCount += Integer.parseInt(monthOrder.get("scount") + "");
                            totalOrder.put("monthcount", monthOrder.get("scount"));
                        }
                    }
                }
                //现金结算
                cashpay += StringUtils.formatDouble(totalOrder.get("cash_pay"));
                totalOrder.put("cash_pay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("cash_pay"))));
                //现金预付
                cashprepay += StringUtils.formatDouble(totalOrder.get("cash_prepay"));
                totalOrder.put("cash_prepay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("cash_prepay"))));

//					cashMoney +=StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"));
//					totalOrder.put("cash_pay",String.format("%.2f",StringUtils.formatDouble(totalOrder.get("cash_pay"))+StringUtils.formatDouble(totalOrder.get("cash_prepay"))));
                //电子支付

                elecMoney += StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"));
                totalOrder.put("electronic_pay", String.format("%.2f", StringUtils.formatDouble(totalOrder.get("electronic_pay")) + StringUtils.formatDouble(totalOrder.get("electronic_prepay"))));
                //免费支付
                totalOrder.put("free_pay", 0.0);
                //遍历免费集合
                if (freeList != null && freeList.size() > 0) {
                    for (Map<String, Object> freeOrder : freeList) {
                        if (totalOrder.get("out_uid").equals(freeOrder.get("out_uid"))) {
                            freeMoney += Double.parseDouble((freeOrder.get("free_pay") == null ? "0" : freeOrder.get("free_pay") + ""));
                            totalOrder.put("free_pay", StringUtils.formatDouble(Double.parseDouble((freeOrder.get("free_pay") == null ? "0" : freeOrder.get("free_pay") + ""))));
                        }
                    }
                }
                reduce_amount += Double.parseDouble((totalOrder.get("reduce_pay") == null ? "0" : totalOrder.get("reduce_pay") + ""));
                backList.add(totalOrder);
            }
        }

        String money = "总订单数："+totalCount+",月卡订单数:"+monthCount+",订单金额:"+StringUtils.formatDouble(totalMoney)+"元," +
                "现金结算:"+StringUtils.formatDouble(cashpay)+"现金预付:"+StringUtils.formatDouble(cashprepay)+"元,电子支付 :"+StringUtils.formatDouble(elecMoney)+"元," +
                "免费金额:"+StringUtils.formatDouble(freeMoney)+"元,减免劵支付:"+StringUtils.formatDouble(reduce_amount)+"元";
        result.put("rows",JSON.toJSON(backList));
        result.put("total",backList.size());
        result.put("money",money);
        result.put("page",1);
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


        logger.error(list);
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
                    "sum(reduce_amount) reduce_pay from order_tb where end_time between "+start_time+" and  " +
                    end_time+" and state= 1 and out_uid = "+uid+" and ishd=? and comid= "+comid;
            alllist = commonDao.getObjectBySql(allsql);

            //月卡订单数
            String monthsql = "select count(*) ordertotal from order_tb where end_time between "+start_time+" and " + end_time +
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
                //减免劵支付
                reduce_money +=StringUtils.formatDouble(oMap.get("reduce_pay"));
                work.put("reduce_pay", StringUtils.formatDouble(oMap.get("reduce_pay")));
            }
            //免费订单集合
            String freesql = "select sum(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay from order_tb where end_time between "+start_time+" and ? " +end_time+
                    " and state= 1 and out_uid = "+uid+" and pay_type =8 and ishd=0 and comid="+comid;
            Map freelist = (Map)commonDao.getObjectBySql(freesql).get(0);
            //免费支付
            free_money += Double.parseDouble((freelist.get("free_pay")== null ? "0" : freelist.get("free_pay")+""));
            work.put("free_pay", StringUtils.formatDouble(Double.parseDouble(freelist.get("free_pay")== null ? "0" : (freelist.get("free_pay")+""))));
        }
        String title = "总订单数："+count+"，月卡订单数："+monthcount+"，总结算金额："+String.format("%.2f",amountmoney)+"元，其中现金结算："+String.format("%.2f",cashpay)
                +"元，现金预付 : "+String.format("%.2f",cashprepay)+"元, 电子支付 ："+StringUtils.formatDouble(elec_money)+"元，" +
                "免费金额："+String.format("%.2f",free_money)+"元,减免劵支付："+String.format("%.2f",reduce_money)+"元";

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


        String sql = "select *,(amount_receivable-electronic_prepay-cash_prepay-reduce_amount) free_pay from order_tb  ";
        //统计总订单数，金额，电子和现金支付
        String countsql = "select count(*) ordertotal,sum(amount_receivable) amount_receivable, " +
                "sum(total) total , sum(cash_prepay) cash_prepay,sum(cash_pay) cash_pay, sum(electronic_pay) electronic_pay," +
                "sum(electronic_prepay) electronic_prepay,sum(reduce_amount) reduce_pay from order_tb";

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
}
