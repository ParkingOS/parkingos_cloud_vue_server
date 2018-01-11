package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface LiftRodService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    byte[] getLiftRodPicture(String comid, String liftrodId);
}
