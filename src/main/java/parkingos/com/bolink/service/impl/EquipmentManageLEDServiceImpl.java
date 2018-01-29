package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComLedTb;
import parkingos.com.bolink.service.EquipmentManageLEDService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageLEDServiceImpl implements EquipmentManageLEDService {

    Logger logger = Logger.getLogger(EquipmentManageLEDServiceImpl.class);

    @Autowired
    private SupperSearchService<ComLedTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        Long comid = Long.valueOf(Integer.valueOf(reqmap.get("comid")));
        ComLedTb comLedTb = new ComLedTb();
        comLedTb.setState(-1);
        comLedTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(comLedTb,reqmap);

        return result;
    }

    @Override
    @Transactional
    public Integer insertResultByConditions(ComLedTb comLedTb) {
        Integer result = commonDao.insert(comLedTb);
        return result;
    }

    @Override
    @Transactional
    public Integer updateResultByConditions(ComLedTb comLedTb) {
        Integer result = commonDao.updateByPrimaryKey(comLedTb);
        return result;
    }

    @Override
    @Transactional
    public Integer removeResultByConditions(ComLedTb comLedTb) {
        Integer result = commonDao.updateByPrimaryKey(comLedTb);
        return result;
    }

}
