package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface LiftRodService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    byte[] getLiftRodPicture(String comid, String liftrodId);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    Object getLiftReason(int type);
}
