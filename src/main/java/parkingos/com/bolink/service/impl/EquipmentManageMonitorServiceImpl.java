package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.MonitorInfoTb;
import parkingos.com.bolink.service.EquipmentManageMonitorService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageMonitorServiceImpl implements EquipmentManageMonitorService {

    Logger logger = Logger.getLogger(EquipmentManageMonitorServiceImpl.class);


    @Autowired
    private SupperSearchService<MonitorInfoTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        String comid = reqmap.get("comid");
        MonitorInfoTb monitorInfoTb = new MonitorInfoTb();
        monitorInfoTb.setState(1);
        monitorInfoTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(monitorInfoTb,reqmap);

        return result;
    }

    @Override
    public Integer insertResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.insert(monitorInfoTb);
        return result;
    }

    @Override
    public Integer updateResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(monitorInfoTb);
        return result;
    }

    @Override
    public Integer removeResultByConditions(MonitorInfoTb monitorInfoTb) {
        Integer result = commonDao.updateByPrimaryKey(monitorInfoTb);
        return result;
    }


}
