package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface BlackUserService {
    public JSONObject selectResultByConditions(Map<String, String> map);

}
