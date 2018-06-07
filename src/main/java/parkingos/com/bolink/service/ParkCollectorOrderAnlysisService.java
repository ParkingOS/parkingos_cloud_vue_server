package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface ParkCollectorOrderAnlysisService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);
}
