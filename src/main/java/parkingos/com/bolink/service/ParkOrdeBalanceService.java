package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ParkOrdeBalanceService {
    JSONObject getBalanceTrade(Map<String, String> reqParameterMap);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);
}
