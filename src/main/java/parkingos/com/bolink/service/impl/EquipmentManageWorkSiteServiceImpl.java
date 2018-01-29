package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComWorksiteTb;
import parkingos.com.bolink.service.EquipmentManageWorkSiteService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class EquipmentManageWorkSiteServiceImpl implements EquipmentManageWorkSiteService {

    Logger logger = Logger.getLogger(EquipmentManageWorkSiteServiceImpl.class);


    @Autowired
    private SupperSearchService<ComWorksiteTb> supperSearchService;
    @Autowired
    private CommonDao commonDao;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        Long comid = Long.valueOf(Integer.valueOf(reqmap.get("comid")));//comid：根据当前车场查询当前车场数据
        ComWorksiteTb comWorksiteTb = new ComWorksiteTb();
        comWorksiteTb.setState(0);
        comWorksiteTb.setComid(comid);
        JSONObject result = supperSearchService.supperSearch(comWorksiteTb,reqmap);

        return result;
    }

    @Override
    @Transactional
    public Integer insertResultByConditions(ComWorksiteTb comWorksiteTb) {

        Integer result = commonDao.insert(comWorksiteTb);
        return result;
    }

    @Override
    @Transactional
    public Integer updateResultByConditions(ComWorksiteTb comWorksiteTb) {
        Integer result = commonDao.updateByPrimaryKey(comWorksiteTb);
        return result;
    }

    @Override
    @Transactional
    public Integer removeResultByConditions(ComWorksiteTb comWorksiteTb) {
        Integer result = commonDao.updateByPrimaryKey(comWorksiteTb);
        return result;
    }

}
