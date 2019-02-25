package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface UserProgramService {

    JSONObject buyUserProgram(Integer buyMonth, Double money, String tradeNo, Long park_id,Long userId);

    JSONObject getBuyTrade(Map<String, String> reqParameterMap);

    List<List<Object>> exportBuyTrade(Map<String, String> reqParameterMap);

}
