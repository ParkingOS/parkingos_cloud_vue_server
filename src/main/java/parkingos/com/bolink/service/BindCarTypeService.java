package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface BindCarTypeService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    JSONObject bindCarType(Map<String, String> reqParameterMap);
}
