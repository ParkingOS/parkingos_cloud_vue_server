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
        String fieldsstr = reqmap.get("fieldsstr");//RequestUtil.processParams(request, "fieldsstr");
        String btime = reqmap.get("btime");//RequestUtil.processParams(request, "btime");
        String etime = reqmap.get("etime");//RequestUtil.processParams(request, "etime");
        Long comid = Long.parseLong(reqmap.get("comid"));//Long.parseLong(searchMap.get("comid")+"");
        if (btime==null){
            btime="";
        }
        if(etime==null){
            etime="";
        }
        if(btime.equals(""))
            btime = nowtime + " 00:00:00";
        if(etime.equals(""))
            etime = nowtime + " 23:59:59";
        Long b = TimeTools.getToDayBeginTime();
        Long e = System.currentTimeMillis()/1000;
        String dstr = btime+"-"+etime;
        if(type.equals("today")){
            sql += " where end_time between "+ b + " and "+e;
            free_sql += " where end_time between "+ b + " and "+e;
            dstr = "今天";
        }else if(type.equals("toweek")){
//            b = TimeTools.getWeekStartSeconds();
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
            dstr = "本周";
        }else if(type.equals("lastweek")){
//            e = TimeTools.getWeekStartSeconds();
//            b= e-7*24*60*60;
//            e = e-1;
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
//            dstr = "上周";
        }else if(type.equals("tomonth")){
//            b=TimeTools.getMonthStartSeconds();
//            sqlInfo =new SqlInfo(" end_time between ? and ? ",
//                    new Object[]{b,e});
//            dstr="本月";
        }else if(!btime.equals("")&&!etime.equals("")){
            b = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(btime);
            e =  TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(etime);
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
}
