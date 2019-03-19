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
import parkingos.com.bolink.service.CityParkOrderAnlysisService;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityParkOrderanlysisServiceImpl implements CityParkOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(CityParkOrderanlysisServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderServer orderServer;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkDataMapper bolinkDataMapper;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        //map  里面放 time

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        String comidStr = reqmap.get("comid_start");

        Long groupid = Long.parseLong(reqmap.get("groupid"));
        Long cityid=orderMapper.getCityIdByGroupId(groupid);
        logger.info("select cityid result:"+cityid);

        String tableName = "order_tb_new";
        if(cityid!=null&&cityid>-1){
            reqmap.put("cityId",cityid+"");
            tableName += "_"+cityid%100;
        }
        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        Long btime = null;
        Long etime = null;
        String time = null;
        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));
        if(date==null||"".equals(date)){
            btime = TimeTools.getToDayBeginTime();
        }else {
            btime = Long.parseLong(date);
        }
        time = TimeTools.getTimeStr_yyyy_MM_dd(btime*1000);
        etime =btime+86399;
        reqmap.put("end_time_start",btime+"");
        reqmap.put("end_time_end",etime+"");
        reqmap.put("time",time);
        List<Map<String,String>> cashList = orderServer.selectCityParkDayAnlysis(reqmap);

        logger.info("===>>>>cashList:"+cashList);

        String bolinkTableName = commonService.getTableNameByGroupId(groupid,1);
        List<Map<String, Object>> inTransactions = new ArrayList<>();
        List<Map<String, Object>> outTransactions = new ArrayList<>();

        if(!Check.isEmpty(comidStr)) {
            //查询某一个车场的收入和支出
            inTransactions = bolinkDataMapper.getTransactionsByComid(bolinkTableName, btime, etime, Long.parseLong(comidStr));

            bolinkTableName = commonService.getTableNameByGroupId(groupid,2);
            outTransactions= bolinkDataMapper.getOutTransactionsByComid(bolinkTableName, btime, etime, Long.parseLong(comidStr));
        }else{
            //查询所有车场的收入
            inTransactions = bolinkDataMapper.getTransactionsByGroupId(bolinkTableName, btime, etime, groupid);

            bolinkTableName = commonService.getTableNameByGroupId(groupid,2);
            outTransactions= bolinkDataMapper.getOutTransactionsByGroupId(bolinkTableName, btime, etime, groupid);
        }
        logger.info("===>>>>inTransactions:"+inTransactions);
        logger.info("===>>>>outTransactions:"+outTransactions);
        List<Map<String,String>> backList = new ArrayList<>();
        List<Long> comList = new ArrayList<>();
        if(cashList!=null&&cashList.size()>0){
            for(Map<String,String> map:cashList){
                String comid = map.get("comid");
                if(Check.isLong(comid)){
                    comList.add(Long.parseLong(comid));
                }
            }
        }

        if(inTransactions!=null&&inTransactions.size()>0){
            for(Map<String,Object> map:inTransactions){
                Long comid = (Long)map.get("comid");
                if(!comList.contains(comid)) {
                    comList.add(comid);
                }
            }
        }

        if(outTransactions!=null&&outTransactions.size()>0){
            for(Map<String,Object> map:outTransactions){
                Long comid = (Long)map.get("comid");
                if(!comList.contains(comid)) {
                    comList.add(comid);
                }
            }
        }


        if(comList!=null&&comList.size()>0){
            Double act_money = 0.0d;//所有的收入金额
            Double cash_pay_money=0.0d;//所有的现金结算
            Double ele_money=0.0d;//所有的电子支付金额
            Double outMoney = 0.0d;//所有的支出金额
            Double free_money=0.0d;//所有的免费金额
            for (Long comId:comList){
                //根据userId获取名称
                String name = commonService.getParkNameById(comId);
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("name",name);
                resultMap.put("time",time);
                resultMap.put("cash_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("ele_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("act_total",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("out_money",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("free_pay",StringUtils.formatDouble(0.0d)+"");

                Double actReceive = 0.0d;
                Double cashTotal = 0.0d;
                Double eleTotal = 0.0d;
                Double outTotal = 0.0d;
                if(cashList!=null&&cashList.size()>0){
                    for(Map<String,String>cashMap:cashList){
                        if(cashMap.get("comid").equals(comId+"")){
                            cashTotal+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            actReceive+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            cash_pay_money+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            free_money+=StringUtils.formatDouble(cashMap.get("free_pay"));
                            resultMap.put("free_pay",cashMap.get("free_pay"));
                        }
                    }
                }

                if (inTransactions!=null&&inTransactions.size()>0){
                    for(Map<String,Object> inMap:inTransactions){
                        Long user = (Long)inMap.get("comid");
                        if(user.equals(comId)){
                            int type = (int) inMap.get("type");
                            if(type== BolinkAccountTypeEnum.CASH_PREPAY.type) {
                                cashTotal+=StringUtils.formatDouble(inMap.get("pay_money"));
                                actReceive += StringUtils.formatDouble(inMap.get("pay_money"));
                                cash_pay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                            }
                            else {
                                actReceive += StringUtils.formatDouble(inMap.get("pay_money"));
                                eleTotal+=StringUtils.formatDouble(inMap.get("pay_money"));
                                ele_money+=StringUtils.formatDouble(inMap.get("pay_money"));
                            }
                        }
                    }
                }

                if (outTransactions!=null&&outTransactions.size()>0){
                    for(Map<String,Object> outMap:outTransactions){
                        Long user = (Long)outMap.get("comid");
                        if(user.equals(comId)){
                            outMoney+=StringUtils.formatDouble(outMap.get("pay_money"));
                            outTotal += StringUtils.formatDouble(outMap.get("pay_money"));
                        }
                    }
                }
                act_money+=StringUtils.formatDouble(actReceive);
                resultMap.put("act_total",StringUtils.formatDouble(actReceive)+"");
                resultMap.put("cash_pay",StringUtils.formatDouble(cashTotal)+"");
                resultMap.put("ele_pay",StringUtils.formatDouble(eleTotal)+"");
                resultMap.put("out_money",StringUtils.formatDouble(outTotal)+"");
                backList.add(resultMap);
            }

            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("name","合计");
            resultMap.put("time",time);
            resultMap.put("cash_pay",StringUtils.formatDouble(cash_pay_money)+"");
            resultMap.put("ele_pay",StringUtils.formatDouble(ele_money)+"");
            resultMap.put("act_total",StringUtils.formatDouble(act_money)+"");
            resultMap.put("out_money",StringUtils.formatDouble(outMoney)+"");
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
        JSONObject result = selectResultByConditions(reqParameterMap);

        List<Object> resList = JSON.parseArray(result.get("rows").toString());

        logger.error("=========>>>>>>.导出订单" + resList.size());
        List<List<Object>> bodyList = new ArrayList<List<Object>>();
        if (resList != null && resList.size() > 0) {
            for (Object object : resList) {
                Map<String,Object> map = (Map)object;
                List<Object> values = new ArrayList<Object>();
                values.add(map.get("name"));
                values.add(map.get("time"));
                values.add(map.get("cash_pay"));
                values.add(map.get("ele_pay"));
                values.add(map.get("act_total"));
                values.add(map.get("out_money"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
