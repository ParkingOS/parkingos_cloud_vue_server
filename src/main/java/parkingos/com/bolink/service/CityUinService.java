package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

public interface CityUinService {
    JSONObject createCity(String name, String union_id, String ukey);
}
