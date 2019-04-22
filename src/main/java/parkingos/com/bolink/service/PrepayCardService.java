package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PrepayCardService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject renewProduct(HttpServletRequest req,Long comid);

    JSONObject createPrepayCard(HttpServletRequest req,Long comid,Long groupId);

    JSONObject editCard(HttpServletRequest req,Long comid);

    JSONObject deleteCard(Long id, Long comid);

    JSONObject groupQuery(Map<String, String> reqParameterMap);
}
