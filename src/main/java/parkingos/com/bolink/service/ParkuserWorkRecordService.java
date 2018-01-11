package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ParkuserWorkRecordService {
    public JSONObject selectResultByConditions(Map<String, String> map);

}
