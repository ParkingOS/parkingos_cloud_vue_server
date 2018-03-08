package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface CityBlackUserService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject editBlackUser(Long id,Integer state);

}
