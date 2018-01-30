package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PhoneInfoTb;
import parkingos.com.bolink.service.EquipmentManageIntercomService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageIntercomServiceImpl implements EquipmentManageIntercomService {

    Logger logger = Logger.getLogger(EquipmentManageIntercomServiceImpl.class);


    @Autowired
    private SupperSearchService<PhoneInfoTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String comid = reqmap.get("comid");
        PhoneInfoTb phoneInfoTb = new PhoneInfoTb();
        phoneInfoTb.setState(1);
        phoneInfoTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(phoneInfoTb,reqmap);

        return result;
    }

    /*@Override
    public JSONObject findMonitorByConditions(Map<String, String> map) {
        String comid = map.get("comid");
        PhoneInfoTb phoneInfoTb = new PhoneInfoTb();
        phoneInfoTb.setState(1);
        phoneInfoTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(phoneInfoTb,map);

        return result;
    }*/

    @Override
    @Transactional
    public Integer insertResultByConditions(PhoneInfoTb phoneInfoTb) {
        Integer result = commonDao.insert(phoneInfoTb);
        return result;
    }

    @Override
    @Transactional
    public Integer updateResultByConditions(PhoneInfoTb phoneInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(phoneInfoTb);
        return result;
    }

    @Override
    @Transactional
    public Integer removeResultByConditions(PhoneInfoTb phoneInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(phoneInfoTb);
        return result;
    }


}
