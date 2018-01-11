package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CarowerProduct;

import java.util.Map;

public interface VipService {
    JSONObject selectResultByConditions(Map<String,String> map);

    Long getkey();

    Integer selectCountByConditions(CarowerProduct carowerProduct);
}
