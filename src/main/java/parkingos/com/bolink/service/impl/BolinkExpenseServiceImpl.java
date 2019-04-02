package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.BolinkExpenseTbExample;
import parkingos.com.bolink.dao.mybatis.BolinkIncomeTbExample;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkDataMapper;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkExpenseTbMapper;
import parkingos.com.bolink.dao.mybatis.mapper.BolinkIncomeTbMapper;
import parkingos.com.bolink.models.BolinkExpenseTb;
import parkingos.com.bolink.service.BolinkExpenseService;
import parkingos.com.bolink.service.CommonService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.ExampleUtis;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class BolinkExpenseServiceImpl implements BolinkExpenseService {

    Logger logger = LoggerFactory.getLogger(BolinkExpenseServiceImpl.class);
//    @Autowired
//    private SupperSearchService supperSearchService;
//    @Autowired
//    private BolinkDataMapper bolinkDataMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private BolinkExpenseTbMapper bolinkExpenseTbMapper;

    @Override
    public JSONObject query(Map<String, String> reqParameterMap) {
//        Long comid = Long.parseLong(reqParameterMap.get("comid"));
//        BolinkExpenseTb bolinkExpenseTb = new BolinkExpenseTb();
//        bolinkExpenseTb.setParkId(comid);
//        JSONObject result = supperSearchService.supperSearch(bolinkExpenseTb,reqParameterMap);
        JSONObject result = getExpenses(reqParameterMap,1);
        return result;
    }

    @Override
    public JSONObject groupQuery(Map<String, String> reqParameterMap) {
        JSONObject result = getExpenses(reqParameterMap,2);
        return result;
    }

    private JSONObject getExpenses(Map<String, String> reqParameterMap,int type) {
        JSONObject result = new JSONObject();
        String tableName = "bolink_expense_tb";
        if(type==1) {
            Long comid = Long.parseLong(reqParameterMap.get("comid"));
            tableName = commonService.getTableNameByComid(comid,2);
            reqParameterMap.put("comid_start",comid+"");
        }else if (type==2){
            Long groupId = Long.parseLong(reqParameterMap.get("groupid"));
            tableName=commonService.getTableNameByGroupId(groupId,2);
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

        BolinkExpenseTbExample example = ExampleUtis.createExpenseExampleByMap(reqParameterMap);
        int count = bolinkExpenseTbMapper.getExpenseCounts(example);
        List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>();
        if(count>0){
            resList = bolinkExpenseTbMapper.getExpenses(example);
        }
        result.put("rows", JSON.toJSON(resList));
        result.put("total",count);
        return result;
    }
}
