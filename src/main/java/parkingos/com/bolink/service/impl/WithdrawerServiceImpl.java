package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.WithdrawerTb;
import parkingos.com.bolink.service.SupperSearchService;
import parkingos.com.bolink.service.WithdrawerService;

import java.util.Map;

@Service
public class WithdrawerServiceImpl implements WithdrawerService {

    Logger logger = Logger.getLogger(WithdrawerServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<WithdrawerTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        WithdrawerTb withdrawerTb = new WithdrawerTb();
        withdrawerTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(withdrawerTb,reqmap);
        return result;
    }

}
