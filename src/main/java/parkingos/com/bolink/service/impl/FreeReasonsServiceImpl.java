package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.FreeReasonsTb;
import parkingos.com.bolink.service.FreeReasonsService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class FreeReasonsServiceImpl implements FreeReasonsService {

    Logger logger = Logger.getLogger(FreeReasonsServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<FreeReasonsTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        FreeReasonsTb freeReasonsTb = new FreeReasonsTb();
        freeReasonsTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(freeReasonsTb,reqmap);
        return result;
    }

}
