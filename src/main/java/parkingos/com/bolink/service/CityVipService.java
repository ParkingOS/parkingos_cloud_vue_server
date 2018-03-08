package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface CityVipService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject createVip(HttpServletRequest req);

//    List<List<String>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject importExcel(HttpServletRequest request)  throws Exception;
}
