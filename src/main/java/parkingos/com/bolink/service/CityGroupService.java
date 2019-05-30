package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface CityGroupService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject deleteGroup(Long id);

    JSONObject addGroup(String name, String cityid, String operatorid, Long id, Long serverId);
}
