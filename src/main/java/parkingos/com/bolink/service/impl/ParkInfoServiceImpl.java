package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ComInfoTb;
import parkingos.com.bolink.service.ParkInfoService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class ParkInfoServiceImpl implements ParkInfoService {

    Logger logger = Logger.getLogger(ParkInfoServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ComInfoTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ComInfoTb comInfoTb = new ComInfoTb();
        comInfoTb.setId(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(comInfoTb,reqmap);
        return result;
    }

}
