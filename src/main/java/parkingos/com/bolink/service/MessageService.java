package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface MessageService {

    JSONObject buyMessage(Integer count, Double money, String tradeNo, Long union_id, Long park_id);

    String notice(Long parkId, String carNumber, Long eTime, String mobile,Integer day);

    JSONObject getSendTrade(Map<String, String> reqParameterMap);

    JSONObject getBuyTrade(Map<String, String> reqParameterMap);

    List<List<Object>> exportSendTrade(Map<String, String> reqParameterMap);

    List<List<Object>> exportBuyTrade(Map<String, String> reqParameterMap);

    JSONObject getCodeState(String tradeNo);
}
