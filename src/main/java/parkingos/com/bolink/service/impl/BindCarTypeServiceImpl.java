package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarNumberTypeTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.BindCarTypeService;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.utils.StringUtils;

import java.util.Map;

@Service
public class BindCarTypeServiceImpl implements BindCarTypeService {

    Logger logger = Logger.getLogger(BindCarTypeServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<CarNumberTypeTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        CarNumberTypeTb carNumberTypeTb = new CarNumberTypeTb();
        carNumberTypeTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(carNumberTypeTb, reqmap);
        return result;
    }

    @Override
    public JSONObject bindCarType(Map<String, String> reqParameterMap) {

        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        String car_number = "";
        if (reqParameterMap.get("car_number") != null && !"".equals(reqParameterMap.get("car_number"))) {
            car_number = StringUtils.decodeUTF8(reqParameterMap.get("car_number")).toUpperCase();
        } else {
            result.put("msg", "请输入车牌号");
            return result;
        }
        if (car_number.length() < 6 || car_number.length() > 8) {
            result.put("msg", "车牌号错误");
            return result;
        }
        Long typeid = -1L;
        if (reqParameterMap.get("typeid") != null) {
            typeid = Long.parseLong(reqParameterMap.get("typeid"));
        }
        if (typeid < 0) {
            result.put("msg", "请选择正确车型");
            return result;
        }
        Long comid = Long.parseLong(reqParameterMap.get("comid"));
        if (comid > 0) {
            long currTime = System.currentTimeMillis() / 1000;

            CarNumberTypeTb carNumberTypeTb = new CarNumberTypeTb();
            carNumberTypeTb.setComid(comid);
            carNumberTypeTb.setCarNumber(car_number);
            int count = commonDao.selectCountByConditions(carNumberTypeTb);
            if (count > 0) {
                result.put("msg", "该车牌已绑定车型");
                return result;
            }
        } else {
            Long nextid = commonDao.selectSequence(CarNumberTypeTb.class);
            CarNumberTypeTb carNumberTypeTb = new CarNumberTypeTb();
            carNumberTypeTb.setId(nextid);
            carNumberTypeTb.setCarNumber(car_number);
            carNumberTypeTb.setComid(comid);
            carNumberTypeTb.setTypeid(typeid);
            carNumberTypeTb.setUpdateTime(System.currentTimeMillis()/1000);

            int ret = commonDao.insert(carNumberTypeTb);
            if(ret==1){
                result.put("state",1);
                result.put("msg","添加成功");
                insertSysn(carNumberTypeTb,0);
            }
        }
        return result;
    }

    private void insertSysn(CarNumberTypeTb carNumberTypeTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(carNumberTypeTb.getComid());
        syncInfoPoolTb.setTableId(carNumberTypeTb.getId());
        syncInfoPoolTb.setTableName("car_number_type_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        commonDao.insert(syncInfoPoolTb);
    }
}


