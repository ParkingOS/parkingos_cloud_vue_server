package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface ParkInfoService {

    String getResultByComid(Long comid);

    JSONObject updateComInfo(HttpServletRequest request);
}
