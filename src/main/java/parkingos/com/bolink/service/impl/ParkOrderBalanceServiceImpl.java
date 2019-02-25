package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkDataMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.enums.BolinkAccountTypeEnum;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.ParkOrdeBalanceService;
import parkingos.com.bolink.service.ParkOrderAnlysisService;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkOrderBalanceServiceImpl implements ParkOrdeBalanceService {

    Logger logger = LoggerFactory.getLogger(ParkOrderBalanceServiceImpl.class);

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private OrderServer orderServer;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkDataMapper bolinkDataMapper;


    @Override
    public JSONObject getBalanceTrade(Map<String, String> reqmap) {

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = Long.parseLong(reqmap.get("comid"));

        Long cityId = commonService.getCityIdByComid(comid);

        String tableName = "order_tb_new";
        if(cityId!=null&&cityId>-1){
            reqmap.put("cityId",cityId+"");
            tableName += "_"+cityId%100;
        }
        reqmap.put("tableName",tableName);
        reqmap.put("comid_start",comid+"");
        reqmap.put("end_time","between");

        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        logger.error("=====date:"+date);

        Long btime = null;
        Long etime = null;
        if(date==null||"".equals(date)){
            btime = TimeTools.getThisWeekBeginTime();
            etime =TimeTools.getToDayBeginTime()+86399;
        }else {
            String[] dateArr = date.split("至");
            String end = dateArr[1];
            String start =dateArr[0];
            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(start);
            etime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(end);
        }
        reqmap.put("end_time_start",btime+"");
        reqmap.put("end_time_end",etime+"");

        /*3.0从订单取出所有金额数据。4.0开始泊链开始对账数据取泊链数据。现在该接口只取现金和减免
           cash_pay
           free_pay
           e_time
        */
//        List<Map<String,String>> backList = new ArrayList<>();
        List<Map<String,String>> cashList = orderServer.selectParkDayAnlysis(reqmap);
        logger.info("====>>>>>>车场日报："+cashList);


        /*
        * 查询交易订单记录
        *   第一步  根据comid查询unionId
        *
        * */
        String bolinkTableName = commonService.getTableNameByComid(comid,1);
        List<Map<String,Object>> inTransactions = bolinkDataMapper.getTransactions(bolinkTableName,btime,etime,comid);


        //把所有的日期组织成list
        List<String> timeList = new ArrayList<>();
        if(cashList!=null&&cashList.size()>0){
            for(Map<String,String> cashMap:cashList){
                timeList.add(cashMap.get("e_time"));
            }
        }
        if(inTransactions!=null&&inTransactions.size()>0){
            for(Map<String,Object> inMap:inTransactions){
                if(!timeList.contains(inMap.get("p_time")+"")) {
                    timeList.add(inMap.get("p_time") + "");
                }
            }
        }

        List<Map<String,String>> backList = new ArrayList<>();
        if(timeList!=null&&timeList.size()>0){
            Double ele_prepay_money=0.0d;//所有的电子预付金额
            Double ele_pay_money=0.0d;//所有的电子结算金额
            Double cash_pay_money=0.0d;//所有的现金结算
            Double cash_prepay_money=0.0d;//所有的现金预付金额;
            Double cashTotal = 0.0d;//所有现金
            Double eleTotal = 0.0d;//所有电子
            Double free_money=0.0d;//所有的免费金额
            for(String time:timeList){
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("e_time",time);
                resultMap.put("ele_prepay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("ele_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("cash_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("cash_prepay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("cash_total",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("ele_total",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("free_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("act_total",StringUtils.formatDouble(0.0d)+"");
                Double time_cash_total=0.0d;//某一天的现金金额
                Double time_ele_total = 0.0d;//某一天的电子金额
                Double act_total=0.0d;//所有的金额
                if(cashList!=null&&cashList.size()>0){
                    for(Map<String,String>cashMap:cashList){
                        if(time.equals(cashMap.get("e_time"))){
                            cashTotal+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            cash_pay_money+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            time_cash_total+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            act_total+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            free_money+=StringUtils.formatDouble(cashMap.get("free_pay"));
                            resultMap.put("cash_pay",StringUtils.formatDouble(cashMap.get("cash_pay"))+"");
//                            resultMap.put("cash_total",StringUtils.formatDouble(cash_total)+"");
                            resultMap.put("free_pay",StringUtils.formatDouble(cashMap.get("free_pay"))+"");
                            break;
                        }
                    }
                }
                if(inTransactions!=null&&inTransactions.size()>0){
                    for(Map<String,Object> inMap:inTransactions){
                        if(time.equals(inMap.get("p_time"))) {
                            int type = (int) inMap.get("type");
                            if (type == BolinkAccountTypeEnum.CASH_PREPAY.type) {
                                act_total+=StringUtils.formatDouble(inMap.get("pay_money"));
                                cashTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                cash_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                time_cash_total += StringUtils.formatDouble(inMap.get("pay_money"));
                                resultMap.put("cash_prepay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                            } else if (type == BolinkAccountTypeEnum.ELE_PREPAY.type) {
                                act_total+=StringUtils.formatDouble(inMap.get("pay_money"));
                                eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                ele_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                time_ele_total += StringUtils.formatDouble(inMap.get("pay_money"));
                                resultMap.put("ele_prepay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                            } else if (type == BolinkAccountTypeEnum.ELE_PAY.type) {
                                act_total+=StringUtils.formatDouble(inMap.get("pay_money"));
                                eleTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                ele_pay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                time_ele_total += StringUtils.formatDouble(inMap.get("pay_money"));
                                resultMap.put("ele_pay", StringUtils.formatDouble(inMap.get("pay_money")) + "");
                            }
                        }
                    }
                }
                resultMap.put("cash_total",StringUtils.formatDouble(time_cash_total)+"");
                resultMap.put("ele_total",StringUtils.formatDouble(time_ele_total)+"");
                //单条电子 + 现金
                resultMap.put("act_total",StringUtils.formatDouble(act_total)+"");
                backList.add(resultMap);
            }
            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("e_time","合计");
            resultMap.put("ele_prepay",StringUtils.formatDouble(ele_prepay_money)+"");
            resultMap.put("ele_pay",StringUtils.formatDouble(ele_pay_money)+"");
            resultMap.put("cash_pay",StringUtils.formatDouble(cash_pay_money)+"");
            resultMap.put("cash_prepay",StringUtils.formatDouble(cash_prepay_money)+"");
            resultMap.put("cash_total",StringUtils.formatDouble(cashTotal)+"");
            resultMap.put("ele_total",StringUtils.formatDouble(eleTotal)+"");
            resultMap.put("free_pay",StringUtils.formatDouble(free_money)+"");
            backList.add(resultMap);
        }

        result.put("rows",JSON.toJSON(backList));
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {


        //删除分页条件  查询该条件下所有  不然为一页数据
        reqParameterMap.remove("orderby");
        //获得要导出的结果
        JSONObject result = getBalanceTrade(reqParameterMap);
        List<Object> resList = JSON.parseArray(result.get("rows").toString());

//        logger.error("=========>>>>>>" + resList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (resList != null && resList.size() > 0) {
            for (Object object : resList) {
                Map<String,Object> map = (Map)object;
                List<Object> values = new ArrayList<Object>();
                values.add(map.get("e_time"));
                values.add(map.get("ele_prepay"));
                values.add(map.get("ele_pay"));
                values.add(map.get("ele_total"));
                values.add(map.get("cash_prepay"));
                values.add(map.get("cash_pay"));
                values.add(map.get("cash_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
