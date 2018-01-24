package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CardRenewTb;

import java.util.List;
import java.util.Map;

public interface CarRenewService {

	public int selectCountByConditions(CardRenewTb cardRenewTb);

	JSONObject selectResultByConditions(Map<String, String> map);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);
}
