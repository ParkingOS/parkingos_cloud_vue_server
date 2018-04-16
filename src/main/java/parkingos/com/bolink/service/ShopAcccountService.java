package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ShopTb;

import java.util.Map;

public interface ShopAcccountService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    Map<String, Object> getShopInfoByid(Long shopid);

    int updateShopById(ShopTb shopTb);

    JSONObject getRecharge(Map<String, String> reqParameterMap);

    Integer getTicketUnitById(Long shopid);
}
