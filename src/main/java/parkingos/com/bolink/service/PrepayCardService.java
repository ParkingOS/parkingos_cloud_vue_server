package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PrepayCardService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject renewProduct(HttpServletRequest req);

    JSONObject createPrepayCard(HttpServletRequest req);

    JSONObject editCard(HttpServletRequest req);

    JSONObject deleteCard(Long id, Long comid);
}
