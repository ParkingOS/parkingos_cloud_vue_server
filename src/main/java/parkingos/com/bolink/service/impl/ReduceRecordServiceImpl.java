package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.BolinkExpenseTb;
import parkingos.com.bolink.models.TicketDeductionTb;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.qo.PageOrderConfig;
import parkingos.com.bolink.qo.SearchBean;
import parkingos.com.bolink.service.ReduceRecordService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.OrmUtil;
import parkingos.com.bolink.utils.TimeTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReduceRecordServiceImpl implements ReduceRecordService {

    Logger logger = LoggerFactory.getLogger(ReduceRecordServiceImpl.class);
    @Autowired
    private SupperSearchService supperSearchService;
    @Autowired
    CommonDao commonDao;

    @Override
    public JSONObject query(Map<String, String> reqParameterMap) {
        Long comid = Long.parseLong(reqParameterMap.get("comid"));
        TicketDeductionTb ticketDeductionTb = new TicketDeductionTb();
        ticketDeductionTb.setComid(comid);
        String useTime = reqParameterMap.get("use_time_start");
        if(Check.isEmpty(useTime)){
            Long begin = TimeTools.getToDayBeginTime();
            Long end = begin+86399;
            reqParameterMap.put("use_time_start",begin+"");
            reqParameterMap.put("use_time_end",end+"");
        }
        JSONObject result = supperSearchService.supperSearch(ticketDeductionTb,reqParameterMap);
        return result;
    }

    @Override
    public JSONObject groupQuery(Map<String, String> reqParameterMap) {

        String str = "{\"total\":0,\"page\":1,\"rows\":[]}";
        JSONObject result = JSONObject.parseObject(str);

        Long groupid = Long.parseLong(reqParameterMap.get("groupid"));
        TicketDeductionTb ticketDeductionTb = new TicketDeductionTb();
        String useTime = reqParameterMap.get("use_time_start");
        if(Check.isEmpty(useTime)){
            Long begin = TimeTools.getToDayBeginTime();
            Long end = begin+86399;
            reqParameterMap.put("use_time_start",begin+"");
            reqParameterMap.put("use_time_end",end+"");
        }
        Map<String, Object> searchMap =  supperSearchService.getGroupOrCitySearch(ticketDeductionTb,reqParameterMap);
        if(searchMap==null){
            return result;
        }
        TicketDeductionTb baseQuery =(TicketDeductionTb)searchMap.get("base");
        List<SearchBean> supperQuery =(List<SearchBean>)searchMap.get("supper");
        PageOrderConfig config = (PageOrderConfig)searchMap.get("config");

        int count = commonDao.selectCountByConditions(baseQuery,supperQuery);
        if(count>0){
            List<Map<String, Object>> resList =new ArrayList<Map<String, Object>>();
            if(config==null){
                config = new PageOrderConfig();
                config.setPageInfo(null,null);
            }
            List<TicketDeductionTb> list = commonDao.selectListByConditions(baseQuery,supperQuery,config);
            if (list != null && !list.isEmpty()) {
                for (TicketDeductionTb ticketDeductionTb1 : list) {
                    OrmUtil<TicketDeductionTb> otm = new OrmUtil<TicketDeductionTb>();
                    Map<String, Object> map = otm.pojoToMap(ticketDeductionTb1);
                    resList.add(map);
                }
                result.put("rows", JSON.toJSON(resList));
            }
        }

        result.put("total",count);
        if(reqParameterMap.get("page")!=null){
            result.put("page",Integer.parseInt(reqParameterMap.get("page")));
        }
        return result;
    }
}
