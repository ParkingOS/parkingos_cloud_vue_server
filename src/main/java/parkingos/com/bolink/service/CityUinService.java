package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

public interface CityUinService {
    JSONObject createCity(String name, String union_id, String ukey);

    JSONObject editSetting(Long cityid, Integer state,Integer type);

    JSONObject querySetting(Long cityid);
}
