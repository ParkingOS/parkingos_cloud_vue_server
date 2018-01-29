package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComCameraTb;
import parkingos.com.bolink.service.EquipmentManageCameraService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageCameraServiceImpl implements EquipmentManageCameraService {

    Logger logger = Logger.getLogger(EquipmentManageCameraServiceImpl.class);

    @Autowired
    private SupperSearchService<ComCameraTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        Long comid = Long.valueOf(Integer.valueOf(reqmap.get("comid")));
        ComCameraTb comCameraTb = new ComCameraTb();
        comCameraTb.setState(1);
        comCameraTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(comCameraTb,reqmap);
        return result;
    }

    @Override
    @Transactional
    public Integer insertResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.insert(comCameraTb);
        return result;
    }

    @Override
    @Transactional
    public Integer updateResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.updateByPrimaryKey(comCameraTb);
        return result;
    }

    @Override
    @Transactional
    public Integer removeResultByConditions(ComCameraTb comCameraTb) {
        Integer result = commonDao.updateByPrimaryKey(comCameraTb);
        return result;
    }

}
