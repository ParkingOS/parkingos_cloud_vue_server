//package parkingos.com.bolink.service.impl;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import parkingos.com.bolink.dao.mybatis.mapper.CityOrderTbMapper;
//import parkingos.com.bolink.models.OrderAndParkTb;
//import parkingos.com.bolink.service.CityUnorderService;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//@Service("citymybatis")
//public class CityOrderServiceMybatisImpl implements CityUnorderService {
//
//    Logger logger = LoggerFactory.getLogger(CityOrderServiceMybatisImpl.class);
//
//    @Autowired
//    CityOrderTbMapper cityorderTbMapper;
//
//
//    @Override
//    public JSONObject selectResultByConditions(Map<String, String> map) {
////        OrderAndParkTbExample orderAndParkTbExample = new OrderAndParkTbExample();
//        JSONObject result = new JSONObject();
//        List<Long> list = new ArrayList<Long>();
//        List<OrderAndParkTb> resList = cityorderTbMapper.queryByParam(list);
//        result.put("rows", JSON.toJSON(resList));
//        return result;
//    }
//
//    @Override
//    public List<List<Object>> exportExcel(Map<String, String> reqParameterMap) {
//        return null;
//    }
//
//}
