package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrderTb;
import parkingos.com.bolink.service.OrderService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service("orderSpring")
public class OrderServiceImpl implements OrderService {

    Logger logger = Logger.getLogger(OrderServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<OrderTb> supperSearchService;

    @Override
    public int selectCountByConditions(OrderTb orderTb) {
        return 0;
    }

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        OrderTb orderTb = new OrderTb();
        orderTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(orderTb,reqmap);
        String str = "{\"total\":12,\"page\":1,\"parkinfo\":\"场内停车52辆,临停车52辆,空车位:948辆\",\"rows\":[]}";
        return result;
    }

}
