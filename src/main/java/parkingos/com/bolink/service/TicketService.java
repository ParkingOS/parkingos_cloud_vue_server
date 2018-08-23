package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface TicketService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject getTicketLog(Map<String, String> reqParameterMap);

    Map<String,Object> createTicket(Long shopid, Integer reduce, Integer type, Integer isauto, Integer number, Integer timeRange, Long uin);

    Map<String,Object> ifChangeCode(HttpServletRequest request);

    List<String> getCodeList(Long shopId, Integer reduce, Integer type, Integer number, String code, String serverpath, Integer timeRange, Long uin);

    void exportCode(String code, HttpServletRequest request, HttpServletResponse resp);

    List<List<Object>> exportLog(Map<String, String> reqParameterMap, Integer hiddenType);
}
