package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

public interface ParkingosProgramService {

    JSONObject getMoneyData(Long comid, Long userId,Integer isManager);
}
