package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.FixCodeTb;
import parkingos.com.bolink.models.ShopTb;

import java.util.Map;

public interface FixCodeService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject addFixCode(FixCodeTb fixCodeTb);

    Long getId();

    JSONObject updateRole(FixCodeTb fixCodeTb);

    JSONObject userTicket(String code);

    JSONObject setPublic(ShopTb shopTb);

    JSONObject setPwd(FixCodeTb fixCodeTb);

    JSONObject buyApp(Double money, String tradeNo, Long park_id, Long shop_id);

    int getOfficialState(Long shopId);

    JSONObject getBuyTrade(Map<String, String> reqParameterMap);
}
