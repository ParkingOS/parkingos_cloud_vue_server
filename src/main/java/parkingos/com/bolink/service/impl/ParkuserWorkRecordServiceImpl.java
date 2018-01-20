package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ParkuserWorkRecordTb;
import parkingos.com.bolink.service.ParkuserWorkRecordService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class ParkuserWorkRecordServiceImpl implements ParkuserWorkRecordService {

    Logger logger = Logger.getLogger(ParkuserWorkRecordServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ParkuserWorkRecordTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ParkuserWorkRecordTb parkuserWorkRecordTb = new ParkuserWorkRecordTb();
        parkuserWorkRecordTb.setParkId(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(parkuserWorkRecordTb,reqmap);
        return result;
    }

}
