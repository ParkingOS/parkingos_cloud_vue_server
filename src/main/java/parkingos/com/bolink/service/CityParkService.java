package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ComInfoTb;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CityParkService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject createPark(HttpServletRequest request);

    JSONObject editpark(ComInfoTb comInfoTb);

    JSONObject deletepark(ComInfoTb comInfoTb);

    JSONObject setpark(Long comid);
}
