package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface BolinkExpenseService {

    JSONObject query(Map<String, String> reqParameterMap);

    JSONObject groupQuery(Map<String, String> reqParameterMap);
}
