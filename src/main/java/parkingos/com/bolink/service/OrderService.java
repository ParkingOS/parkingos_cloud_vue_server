package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.OrderTb;

import java.util.Map;

public interface OrderService {

    public int selectCountByConditions(OrderTb orderTb);

    public JSONObject selectResultByConditions(Map<String,String> map);

}
