package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.models.BolinkExpenseTb;
import parkingos.com.bolink.models.TicketDeductionTb;
import parkingos.com.bolink.service.ReduceRecordService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.Check;
import parkingos.com.bolink.utils.TimeTools;

import java.util.Map;

@Service
public class ReduceRecordServiceImpl implements ReduceRecordService {

    Logger logger = LoggerFactory.getLogger(ReduceRecordServiceImpl.class);
    @Autowired
    private SupperSearchService supperSearchService;

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
}
