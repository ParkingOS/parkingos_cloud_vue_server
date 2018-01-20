package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface FreeReasonsService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    JSONObject createFreeReason(String name, Integer sort);
}
