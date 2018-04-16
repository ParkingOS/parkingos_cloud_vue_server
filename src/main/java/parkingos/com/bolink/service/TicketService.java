package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TicketService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject getTicketLog(Map<String, String> reqParameterMap);

    Map<String,Object> createTicket(Long shopid,Integer reduce,Integer type,Integer isauto);

    Map<String,Object> ifChangeCode(HttpServletRequest request);
}
