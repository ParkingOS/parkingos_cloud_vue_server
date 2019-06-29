package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface CityLogService {
//    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject cityQuery(Map<String, String> reqParameterMap);

    JSONObject getGroupLog(Long groupid, Long logId, Integer operateType, Long parkId, String typeStart, String remark, Long timeStart, Long timeEnd, Integer rp, Integer page);
}
