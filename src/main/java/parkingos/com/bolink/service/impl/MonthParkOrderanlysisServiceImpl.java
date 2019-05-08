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
import parkingos.com.bolink.service.MonthParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonthParkOrderanlysisServiceImpl implements MonthParkOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(MonthParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderServer orderServer;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkDataMapper bolinkDataMapper;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) throws Exception{

        //map里面  放comid_start

        String resstr = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(resstr);


        Long comid = Long.parseLong(reqmap.get("comid"));

//        Long groupid =orderMapper.getGroupIdByComId(comid);
//        Long cityid = -1L;
//        if(groupid!=null&&groupid>-1){
//            cityid = orderMapper.getCityIdByGroupId(groupid);
//        }else {
//            cityid = orderMapper.getCityIdByComId(comid);
//        }
        Long cityid = commonService.getCityIdByComid(comid);
        String tableName = "order_tb_new";
        if(cityid!=null&&cityid>-1){
            reqmap.put("cityId",cityid+"");
            tableName += "_"+cityid%100;
        }
        String btime = reqmap.get("btime");
        String etime = reqmap.get("etime");
        Long b= null;
        Long e = null;
        if(!Check.isEmpty(btime)){
            b = TimeTools.getLongMilliSecondFrom_YYYYMM(btime)/1000;
        }else{
            b=TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(TimeTools.getThisYearBeginTime());
        }
        if (etime!=null&&!"".equals(etime)) {
            e =TimeTools.getDateFromStr2(etime)/1000;
        }else{
            e = TimeTools.getNextMonthStartMillis()/1000;
        }

        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        reqmap.put("end_time_start",b+"");
        reqmap.put("end_time_end",e+"");
        reqmap.put("comid_start",comid+"");
        List<Map<String,String>> cashList = orderServer.selectParkMonthAnlysis(reqmap);

        String bolinkTableName = commonService.getTableNameByComid(comid,1);

        //获取车场所有的收入金额
        List<Map<String, Object>> inTransactions = bolinkDataMapper.getMonthTransactionsByComid(comid,b,e,bolinkTableName);

        bolinkTableName =commonService.getTableNameByComid(comid,2);

        List<Map<String, Object>> outTransactions = bolinkDataMapper.getMonthOutTransactionsByComid(comid,b,e,bolinkTableName);

        List<Map<String,String>> backList = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        if(cashList!=null&&cashList.size()>0){
            for(Map<String,String> map:cashList){
                String sdate = map.get("sdate");
                dateList.add(sdate);
            }
        }

        if(inTransactions!=null&&inTransactions.size()>0){
            for(Map<String,Object> map:inTransactions){
                String sdate = map.get("sdate")+"";
                if(!dateList.contains(sdate)) {
                    dateList.add(sdate);
                }
            }
        }

        if(dateList!=null&&dateList.size()>0){
            Double act_money = 0.0d;//所有的收入金额(下面cash_pay_money+ele_prepay_money)
            Double cash_pay_money=0.0d;//所有的现金 (包括结算和预付)
            Double ele_prepay_money=0.0d;//所有的电子支付金额
            Double free_money=0.0d;//所有的免费金额
            Double outMoney=0.0d;//所有的支出
            for (String sdate:dateList){
                //根据userId获取名称
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("sdate",sdate);
                resultMap.put("cash_pay", StringUtils.formatDouble(0.0d)+"");
                resultMap.put("ele_prepay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("act_total",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("out_money",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("free_pay",StringUtils.formatDouble(0.0d)+"");

                Double actReceive = 0.0d;
                Double cashTotal = 0.0d;
                Double eleMoney = 0.0d;
                if(cashList!=null&&cashList.size()>0){
                    for(Map<String,String>cashMap:cashList){
                        if(cashMap.get("sdate").equals(sdate)){
                            actReceive+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            cash_pay_money+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            cashTotal+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            free_money+=StringUtils.formatDouble(cashMap.get("free_pay"));
                            resultMap.put("free_pay",cashMap.get("free_pay"));
                        }
                    }
                }

                if (inTransactions!=null&&inTransactions.size()>0){
                    for(Map<String,Object> inMap:inTransactions){
                        String date = inMap.get("sdate")+"";
                        if(date.equals(sdate)){
                            int type = (int) inMap.get("type");
                            actReceive += StringUtils.formatDouble(inMap.get("pay_money"));
                            if(type== BolinkAccountTypeEnum.CASH_PREPAY.type) {
                                cashTotal += StringUtils.formatDouble(inMap.get("pay_money"));
                                cash_pay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                            }
                            //不再需要电子预付
                            else{
                                eleMoney+=StringUtils.formatDouble(inMap.get("pay_money"));
                                ele_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
//                                resultMap.put("ele_prepay", inMap.get("pay_money") + "");
                            }
                        }
                    }
                }
                if (outTransactions!=null&&outTransactions.size()>0){
                    for(Map<String,Object> outMap:outTransactions){
                        String date = outMap.get("sdate")+"";
                        if(date.equals(sdate)){
                            outMoney += StringUtils.formatDouble(outMap.get("pay_money"));
                            resultMap.put("out_money", StringUtils.formatDouble(outMap.get("pay_money") + "")+"");
                        }
                    }
                }

                act_money+=StringUtils.formatDouble(actReceive);
                resultMap.put("act_total",StringUtils.formatDouble(actReceive)+"");
                resultMap.put("cash_pay",StringUtils.formatDouble(cashTotal)+"");
                resultMap.put("ele_prepay", StringUtils.formatDouble(eleMoney)+"");
                backList.add(resultMap);
            }

            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("sdate","合计");
            resultMap.put("cash_pay",StringUtils.formatDouble(cash_pay_money)+"");
            resultMap.put("ele_prepay",StringUtils.formatDouble(ele_prepay_money)+"");
            resultMap.put("act_total",StringUtils.formatDouble(act_money)+"");
            resultMap.put("out_money",StringUtils.formatDouble(outMoney)+"");
            resultMap.put("free_pay",StringUtils.formatDouble(free_money)+"");
            backList.add(resultMap);
        }


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
                values.add(map.get("cash_pay"));
                values.add(map.get("ele_prepay"));
                values.add(map.get("act_total"));
                values.add(map.get("out_money"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
