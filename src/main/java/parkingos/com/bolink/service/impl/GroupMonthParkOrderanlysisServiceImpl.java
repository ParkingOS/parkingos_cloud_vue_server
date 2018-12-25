package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.OrderMapper;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.orderserver.OrderServer;
import parkingos.com.bolink.service.GroupMonthParkOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GroupMonthParkOrderanlysisServiceImpl implements GroupMonthParkOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(GroupMonthParkOrderanlysisServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private CommonMethods commonMethods;

    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderServer orderServer;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) throws Exception{

        String strq = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(strq);


        Long groupid = Long.parseLong(reqmap.get("groupid"));

        Long cityid = orderMapper.getCityIdByGroupId(groupid);
        String tableName = "order_tb_new";
        if(cityid!=null&&cityid>-1){
            reqmap.put("cityId",cityid+"");
            tableName += "_"+cityid%100;
        }

        reqmap.put("end_time","between");
        reqmap.put("tableName",tableName);
        String btime = reqmap.get("btime");
        String etime = reqmap.get("etime");
        Long b= null;
        Long e = null;
        if(!Check.isEmpty(btime)){
            b = TimeTools.getLongMilliSecondFrom_YYYYMM(btime)/1000;
        }else{
            b=TimeTools.getMonthStartSeconds();
        }
        if (etime!=null&&!"".equals(etime)) {
            e =TimeTools.getDateFromStr2(etime)/1000;
        }else{
            e = TimeTools.getNextMonthStartMillis()/1000;
        }

        reqmap.put("end_time_start",b+"");
        reqmap.put("end_time_end",e+"");


        List<Map<String,String>> backList = orderServer.selectCityMonthAnlysis(reqmap);


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
                values.add(map.get("scount"));
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
