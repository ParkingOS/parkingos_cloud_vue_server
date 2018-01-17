package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkLogTb;
import parkingos.com.bolink.service.ParkLogService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class ParkLogServiceImpl implements ParkLogService {

    Logger logger = Logger.getLogger(ParkLogServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkLogTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ParkLogTb parkLogTb = new ParkLogTb();
        parkLogTb.setParkId(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(parkLogTb,reqmap);

        return result;
    }

}
