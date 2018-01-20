package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ZldBlackTb;

import java.util.Map;

public interface BlackUserService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    JSONObject editBlackUser(ZldBlackTb zldBlackTb);

    JSONObject deleteBlackUser(ZldBlackTb zldBlackTb);

    Long getId();

    JSONObject createBlackUser(ZldBlackTb zldBlackTb);
}
