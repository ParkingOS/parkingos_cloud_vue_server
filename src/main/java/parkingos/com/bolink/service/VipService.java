package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CarowerProduct;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface VipService {
    JSONObject selectResultByConditions(Map<String, String> map);

    Long getkey();

    Integer selectCountByConditions(CarowerProduct carowerProduct);

    JSONObject deleteCarowerProById(Long id, Long comid);

    JSONObject createVip(HttpServletRequest req);

    JSONObject editCarNum(Long id, String carNumber, Long comid);

    JSONObject renewProduct(HttpServletRequest req);

    List<List<String>> exportExcel(Map<String, String> reqParameterMap);
}
