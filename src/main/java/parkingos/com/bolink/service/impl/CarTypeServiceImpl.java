package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.models.SyncInfoPoolTb;
import parkingos.com.bolink.service.CarTypeService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class CarTypeServiceImpl implements CarTypeService {

    Logger logger = Logger.getLogger(CarTypeServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<CarTypeTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setComid(Long.parseLong(reqmap.get("comid")));
        carTypeTb.setIsDelete(0);
        JSONObject result = supperSearchService.supperSearch(carTypeTb,reqmap);
        return result;
    }

    @Override
    public Long getId() {
        return commonDao.selectSequence(CarTypeTb.class);
    }

    @Override
    public JSONObject createCarType(CarTypeTb carTypeTb) {
        String str = "{\"state\":0,\"msg\":\"添加失败\"}";
        JSONObject result = JSONObject.parseObject(str);

        int ret = commonDao.insert(carTypeTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","添加成功");
            insertSysn(carTypeTb,0);
        }
        return result;
    }

    @Override
    public JSONObject updateCarType(CarTypeTb carTypeTb) {
        String str = "{\"state\":0,\"msg\":\"修改失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        int ret = commonDao.updateByPrimaryKey(carTypeTb);
        if(ret==1){
            carTypeTb = (CarTypeTb)commonDao.selectObjectByConditions(carTypeTb);
            result.put("state",1);
            result.put("msg","修改成功");
            insertSysn(carTypeTb,1);
        }
        return result;
    }

    @Override
    public JSONObject deleteCarType(CarTypeTb carTypeTb) {
        String str = "{\"state\":0,\"msg\":\"删除失败\"}";
        JSONObject result = JSONObject.parseObject(str);
        int ret = commonDao.updateByPrimaryKey(carTypeTb);
        if(ret==1){
            result.put("state",1);
            result.put("msg","删除成功");
            carTypeTb = (CarTypeTb)commonDao.selectObjectByConditions(carTypeTb);
            insertSysn(carTypeTb,1);
        }
        return result;
    }

    private void insertSysn(CarTypeTb carTypeTb, Integer operater){
        SyncInfoPoolTb syncInfoPoolTb = new SyncInfoPoolTb();
        syncInfoPoolTb.setComid(carTypeTb.getComid());
        syncInfoPoolTb.setTableId(carTypeTb.getId());
        syncInfoPoolTb.setTableName("car_type_tb");
        syncInfoPoolTb.setCreateTime(System.currentTimeMillis()/1000);
        syncInfoPoolTb.setOperate(operater);
        commonDao.insert(syncInfoPoolTb);
    }
}
