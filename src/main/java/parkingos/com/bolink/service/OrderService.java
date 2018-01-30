package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.OrderTb;

import java.util.List;
import java.util.Map;

public interface OrderService {

    public int selectCountByConditions(OrderTb orderTb);

    public JSONObject selectResultByConditions(Map<String,String> map);

    JSONObject getPicResult(String orderid, Long comid);

    byte[] getCarPics(String orderid, Long comid, String type, Integer currentnum);

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);
}
