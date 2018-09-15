package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.HomeOwnerTb;

import java.util.List;
import java.util.Map;

public interface HomeOwnerService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject deleteOwner(Long id);

    JSONObject addOwner(HomeOwnerTb homeOwnerTb);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);
}
