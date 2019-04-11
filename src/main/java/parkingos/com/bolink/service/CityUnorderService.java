package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public interface CityUnorderService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject toZero(Long id, Long cityid, Long createTime,String money,Long groupId,Long uin,String nickname);
}
