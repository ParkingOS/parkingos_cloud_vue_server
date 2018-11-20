package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.OrderTb;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface OrderService {

    public int selectCountByConditions(OrderTb orderTb);

    public JSONObject selectResultByConditions(Map<String,String> map);

    JSONObject getPicResult(String orderid, Long comid);

    String getCarPics(String orderid, Long comid, String type, HttpServletResponse response) throws Exception;

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    Long getComidByOrder(Long id);

    void resetDataByComid(Long comid);
}
