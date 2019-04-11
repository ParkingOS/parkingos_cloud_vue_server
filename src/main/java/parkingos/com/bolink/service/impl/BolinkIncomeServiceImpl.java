package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.BolinkIncomeTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkDataMapper;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkIncomeTbMapper;
import parkingos.com.bolink.models.BigScreenTrade;
import parkingos.com.bolink.models.BolinkIncomeTb;
import parkingos.com.bolink.models.ShortMessageTb;
import parkingos.com.bolink.service.AddValueService;
import parkingos.com.bolink.service.BolinkIncomeService;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.ExampleUtis;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BolinkIncomeServiceImpl implements BolinkIncomeService {

    Logger logger = LoggerFactory.getLogger(BolinkIncomeServiceImpl.class);
    @Autowired
    private BolinkIncomeService bolinkIncomeService;
//    private SupperSearchService supperSearchService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkIncomeTbMapper bolinkIncomeTbMapper;

    @Override
    public JSONObject query(Map<String, String> reqParameterMap) {
//        Long comid = Long.parseLong(reqParameterMap.get("comid"));
//        BolinkIncomeTb bolinkIncomeTb = new BolinkIncomeTb();
//        bolinkIncomeTb.setParkId(comid);
        JSONObject result = getIncomes(reqParameterMap,1);
//        JSONObject result = supperSearchService.supperSearch(bolinkIncomeTb,reqParameterMap);
        return result;
    }

    @Override
    public JSONObject groupQuery(Map<String, String> reqParameterMap) {
        JSONObject result = getIncomes(reqParameterMap,2);
        return result;
    }

    private JSONObject getIncomes(Map<String, String> reqParameterMap,int type) {
        logger.info("====>>>>>>>>>>>reqParameterMap:"+reqParameterMap);
        String tableName = "bolink_income_tb";
        JSONObject result = new JSONObject();
        if(type==1) {
            Long comid = Long.parseLong(reqParameterMap.get("comid"));
            tableName = commonService.getTableNameByComid(comid,1);
            reqParameterMap.put("comid_start",comid+"");
        }else if(type==2){
            Long groupId = Long.parseLong(reqParameterMap.get("groupid"));
            tableName =commonService.getTableNameByGroupId(groupId,1);
        }
        reqParameterMap.put("tableName",tableName);
        //增加默认的pay_time,默认今天一天的数据
        String payTime=reqParameterMap.get("pay_time_start");
        if(Check.isEmpty(payTime)){
            Long payStart = TimeTools.getToDayBeginTime();
            Long payEnd = payStart+86399L;
            reqParameterMap.put("pay_time_start",payStart+"");
            reqParameterMap.put("pay_time_end",payEnd+"");
        }

        BolinkIncomeTbExample example = ExampleUtis.createIncomeExampleByMap(reqParameterMap);
        logger.info("====>>>>>>>>>>>example:"+example);
        int count = bolinkIncomeTbMapper.getIncomeCounts(example);
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        if(count>0){
            resList = bolinkIncomeTbMapper.getIncomes(example);
            if(type==2){
                //查询交易记录的总金额

            }
        }
        result.put("rows", JSON.toJSON(resList));
        result.put("total",count);
        return result;
    }
}
