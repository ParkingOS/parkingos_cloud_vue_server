package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface AdminCityService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject addCity(String name, String union_id, String ukey, Long id);

    JSONObject deleteCity(Long id);
}
