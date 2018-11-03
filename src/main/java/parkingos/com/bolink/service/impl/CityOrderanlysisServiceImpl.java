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
import parkingos.com.bolink.service.CityOrderAnlysisService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CityOrderanlysisServiceImpl implements CityOrderAnlysisService {

    Logger logger = LoggerFactory.getLogger(CityOrderanlysisServiceImpl.class);

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
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        String str = "{\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        String comidStr = reqmap.get("comid_start");
        Long groupid = Long.parseLong(reqmap.get("groupid"));
        Long cityid=orderMapper.getCityIdByGroupId(groupid);
        String tableName = "order_tb_new";
        if(cityid!=null&&cityid>-1){
            tableName +="_"+cityid%100;
        }
        reqmap.put("tableName",tableName);
        reqmap.put("end_time","between");
        String date = StringUtils.decodeUTF8(StringUtils.decodeUTF8(reqmap.get("date")));

        Long btime = null;
        Long etime = null;
        if(date==null||"".equals(date)){
            btime = TimeTools.getToDayBeginTime()-86400*9;
            etime =TimeTools.getToDayBeginTime()+86399;
        }else {
            String[] dateArr = date.split("至");
            String start =dateArr[0];
            String end = dateArr[1];
            btime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(start);
            etime = TimeTools.getLongMilliSecondFrom_HHMMDDHHmmss(end);
        }
        reqmap.put("end_time_start",btime+"");
        reqmap.put("end_time_end",etime+"");

        List<Map<String,String>> backList = orderServer.selectCityDayAnlysis(reqmap);



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
                values.add(map.get("time"));
//                values.add(map.get("comid"));
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
