package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.CityLogService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class CityLogServiceImpl implements CityLogService {

    Logger logger = LoggerFactory.getLogger(CityLogServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkLogTb> supperSearchService;
    @Autowired
    private CommonMethods commonMethods;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        JSONObject result = new JSONObject();

        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setGroupId(Long.parseLong(reqmap.get("groupid")));
        result = supperSearchService.supperSearch(parkLogTb,reqmap);
        return result;
    }

}
