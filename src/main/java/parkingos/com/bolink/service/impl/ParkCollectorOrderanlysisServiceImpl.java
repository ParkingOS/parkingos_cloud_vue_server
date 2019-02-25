package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkDataMapper;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.enums.BolinkAccountTypeEnum;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.ParkCollectorOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkCollectorOrderanlysisServiceImpl implements ParkCollectorOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(ParkCollectorOrderanlysisServiceImpl.class);


    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private OrderServer orderServer;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkDataMapper bolinkDataMapper;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        //reqmap 里面放comid_start

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        Long comid = Long.parseLong(reqmap.get("comid"));
        String outUid= reqmap.get("out_uid_start");

        Long groupId = orderMapper.getGroupIdByComId(comid);
        Long cityId = -1L;
        if(groupId!=null&&groupId>-1){
            cityId = orderMapper.getCityIdByGroupId(groupId);
        }else{
            cityId = orderMapper.getCityIdByComId(comid);
        }
        String tableName = "order_tb_new";
        if(cityId!=null&&cityId>-1){
            reqmap.put("cityId",cityId+"");
            tableName = "order_tb_new_"+cityId%100;
        }

        String date = reqmap.get("date");
        Long start = null;
        if(date==null||"".equals(date)){
            start = TimeTools.getToDayBeginTime();
        }else{
            start = Long.parseLong(date);
        }
        Long end = start+24*60*60;

        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        reqmap.put("end_time_start",start+"");
        reqmap.put("end_time_end",end+"");
        reqmap.put("comid_start",comid+"");

        List<Map<String,String>> cashList = orderServer.selectParkCollectorAnlysis(reqmap);
        String bolinkTableName = commonService.getTableNameByComid(comid,1);
        List<Map<String, Object>> inTransactions = new ArrayList<>();

        if(!Check.isEmpty(outUid)) {
            //查询某一个收费员的收入
            inTransactions = bolinkDataMapper.getTransactionsByUid(bolinkTableName, start, end, comid, Long.parseLong(outUid));
        }else{
            //查询所有收费员的收入
            inTransactions = bolinkDataMapper.getTransactionsByAllUid(bolinkTableName, start, end, comid);
        }
        List<Map<String,String>> backList = new ArrayList<>();
        List<Long> userList = new ArrayList<>();
        if(cashList!=null&&cashList.size()>0){
            for(Map<String,String> map:cashList){
                String user = map.get("name");
                if(Check.isLong(user)){
                    userList.add(Long.parseLong(user));
                }
            }
        }

        if(inTransactions!=null&&inTransactions.size()>0){
            for(Map<String,Object> map:inTransactions){
                Long user = (Long)map.get("collector");
                if(!userList.contains(user)) {
                    userList.add(user);
                }
            }
        }

        if(userList!=null&&userList.size()>0){
            Double act_money = 0.0d;//所有的收入金额
            Double cash_pay_money=0.0d;//所有的现金结算
            Double cash_prepay_money=0.0d;//所有的现金预付金额;
            Double ele_prepay_money=0.0d;//所有的电子支付金额
            Double free_money=0.0d;//所有的免费金额
            for (Long userId:userList){
                //根据userId获取名称
                String name = commonService.getUserNameById(userId);
                Map<String,String> resultMap = new HashMap<>();
                resultMap.put("name",name);
                resultMap.put("cash_pay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("cash_prepay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("ele_prepay",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("act_total",StringUtils.formatDouble(0.0d)+"");
                resultMap.put("free_pay",StringUtils.formatDouble(0.0d)+"");

                Double actReceive = 0.0d;
                Double cashTotal = 0.0d;
                if(cashList!=null&&cashList.size()>0){
                    for(Map<String,String>cashMap:cashList){
                        if(cashMap.get("name").equals(userId+"")){
                            cashTotal+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            actReceive+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            cash_pay_money+=StringUtils.formatDouble(cashMap.get("cash_pay"));
                            free_money+=StringUtils.formatDouble(cashMap.get("free_pay"));
                            resultMap.put("cash_pay",cashMap.get("cash_pay"));
                            resultMap.put("free_pay",cashMap.get("free_pay"));
                        }
                    }
                }

                if (inTransactions!=null&&inTransactions.size()>0){
                    for(Map<String,Object> inMap:inTransactions){
                        Long user = (Long)inMap.get("collector");
                        if(user.equals(userId)){
                            int type = (int) inMap.get("type");
                            if(type== BolinkAccountTypeEnum.CASH_PREPAY.type) {
                                cashTotal+=StringUtils.formatDouble(inMap.get("pay_money"));
                                actReceive += StringUtils.formatDouble(inMap.get("pay_money"));
                                cash_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
                                resultMap.put("cash_prepay", inMap.get("pay_money") + "");
                            }
                            //不再需要电子预付
//                            else if(type==BolinkAccountTypeEnum.ELE_PREPAY.type){
//                                actReceive += StringUtils.formatDouble(inMap.get("pay_money"));
//                                ele_prepay_money += StringUtils.formatDouble(inMap.get("pay_money"));
//                                resultMap.put("ele_prepay", inMap.get("pay_money") + "");
//                            }
                        }
                    }
                }
                act_money+=StringUtils.formatDouble(actReceive);
//                cash_money+=StringUtils.formatDouble(cash_prepay_money);
                resultMap.put("act_total",StringUtils.formatDouble(actReceive)+"");
                resultMap.put("cash_total",StringUtils.formatDouble(cashTotal)+"");
                backList.add(resultMap);
            }

            Map<String,String> resultMap = new HashMap<>();
            resultMap.put("name","合计");
            resultMap.put("cash_pay",StringUtils.formatDouble(cash_pay_money)+"");
            resultMap.put("cash_prepay",StringUtils.formatDouble(cash_prepay_money)+"");
            resultMap.put("ele_prepay",StringUtils.formatDouble(ele_prepay_money)+"");
            resultMap.put("act_total",StringUtils.formatDouble(act_money)+"");
            resultMap.put("free_pay",StringUtils.formatDouble(free_money)+"");
            backList.add(resultMap);
        }


//

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
                values.add(map.get("cash_pay"));
                values.add(map.get("cash_prepay"));
//                values.add(map.get("ele_prepay"));
                values.add(map.get("act_total"));
                values.add(map.get("free_pay"));
                bodyList.add(values);
            }
        }
        return bodyList;
    }
}
