package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface AddValueService {

    JSONObject getMessage(Map<String, String> reqParameterMap);

    JSONObject getBigScreen(Map<String, String> reqParameterMap);
}
