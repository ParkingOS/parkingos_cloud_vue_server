package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface BigScreenService {

    JSONObject getState(Long comid);

    JSONObject buyBigScreen(Integer count, Double money, String tradeNo, Long park_id);

//    String getBigScreenData(Long comid, int i);

    JSONObject getBuyTrade(Map<String, String> reqParameterMap);

    List<List<Object>> exportBuyTrade(Map<String, String> reqParameterMap);
}
