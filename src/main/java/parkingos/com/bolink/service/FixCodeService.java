package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.FixCodeTb;

import java.util.Map;

public interface FixCodeService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject addFixCode(FixCodeTb fixCodeTb);

    Long getId();

    JSONObject updateRole(FixCodeTb fixCodeTb);

    JSONObject userTicket(String code);
}
