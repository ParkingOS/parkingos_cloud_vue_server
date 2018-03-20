package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.mybatis.mapper.CityOrderTbMapper;
import parkingos.com.bolink.models.OrderAndParkTb;
import parkingos.com.bolink.service.CityUnorderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("citymybatis")
public class CityOrderServiceMybatisImpl implements CityUnorderService {

    Logger logger = Logger.getLogger(CityOrderServiceMybatisImpl.class);

    @Autowired
    CityOrderTbMapper cityorderTbMapper;


    @Override
    public JSONObject selectResultByConditions(Map<String, String> map) {
        System.out.println("============mybatis+陈博文");
//        OrderAndParkTbExample orderAndParkTbExample = new OrderAndParkTbExample();
        JSONObject result = new JSONObject();
//        OrderAndParkTb orderAndParkTb = new OrderAndParkTb();
//        orderAndParkTb.setState(0);
//        orderAndParkTb.setIshd(0);
        List<Long> list = new ArrayList<>();
//        list.add(21782L);
//        list.add(21866L);
        List<OrderAndParkTb> resList = cityorderTbMapper.queryByParam(list);
        result.put("rows", JSON.toJSON(resList));
        return result;
    }

    @Override
    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {
        return null;
    }

}
