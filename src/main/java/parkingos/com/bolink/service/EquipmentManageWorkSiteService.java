package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface EquipmentManageWorkSiteService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(Map<String, String> map);

}
