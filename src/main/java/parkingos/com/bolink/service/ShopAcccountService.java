package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ShopAcccountService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);
}
