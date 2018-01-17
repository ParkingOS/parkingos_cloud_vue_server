package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ZldBlackTb;
import parkingos.com.bolink.service.BlackUserService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class BlackUserServiceImpl implements BlackUserService {

    Logger logger = Logger.getLogger(BlackUserServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ZldBlackTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {

        ZldBlackTb zldBlackTb = new ZldBlackTb();
        zldBlackTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(zldBlackTb,reqmap);

        return result;
    }

}
