package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.OrgCityMerchants;
import parkingos.com.bolink.service.CheckBolinkParkService;
import parkingos.com.bolink.service.GetDataService;
import parkingos.com.bolink.service.redis.RedisService;

import java.util.List;
import java.util.Map;


@Service
public class CheckBolinkParkServiceImpl implements CheckBolinkParkService {

    Logger logger = LoggerFactory.getLogger(CheckBolinkParkServiceImpl.class);
    @Autowired
    private CommonDao commonDao;
    @Autowired
    private GetDataService getDataService;
    @Autowired
    private RedisService redisService;

    @Override
    public String checkPark(String unionId, String parkId) {

        Object value = redisService.get("parkingos-yun-"+unionId);
        logger.info("===>>>from redis ："+value);
        if(value!=null){
            JSONArray array = JSONArray.parseArray(value+"");
            logger.info("redis 取出string串解析："+array);
            if(array!=null&&array.size()>0) {
                for (int i = 0; i < array.size(); i++) {
                    String comid = array.get(i) + "";
                    if (comid.equals(parkId)){
                        return "1";
                    }
                }
            }
            return "0";
        }
        logger.info("===>>>查询厂商");
        OrgCityMerchants orgCityMerchants = new OrgCityMerchants();
        orgCityMerchants.setUnionId(unionId);
        orgCityMerchants.setState(0);
        orgCityMerchants = (OrgCityMerchants) commonDao.selectObjectByConditions(orgCityMerchants);
        if(orgCityMerchants==null){
            logger.info("===>>>云平台不存在该厂商！");
            redisService.set("parkingos-"+unionId,"[]",3600);
            return "0";
        }

        Long cityId = orgCityMerchants.getId();
        //获取这个城市下面所有的车场
        List<Object> list = getDataService.getAllBolinkParks(cityId);
        logger.info("===>>>>"+list);
//        List<Map> list = JSONObject.parseArray(parks, Map.class);
        JSONArray jsonArray = new JSONArray();
        if(list!=null&&list.size()>0){
            for(Object object :list){
                jsonArray.add(object);
            }
        }
        logger.info("===>>>>jsonArray:"+jsonArray);
        if(jsonArray!=null&&jsonArray.size()>0) {
            redisService.set("parkingos-yun-"+unionId,jsonArray.toJSONString(),3600);
            for (int i = 0; i < jsonArray.size(); i++) {
                String comid = jsonArray.get(i) + "";
                if (comid.equals(parkId)){
                    return "1";
                }
            }
        }
        return "0";

    }
}
