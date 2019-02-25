package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ReduceRecordService {

    JSONObject query(Map<String, String> reqParameterMap);
}
