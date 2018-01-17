package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarNumberTypeTb;
import parkingos.com.bolink.service.BindCarTypeService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class BindCarTypeServiceImpl implements BindCarTypeService {

    Logger logger = Logger.getLogger(BindCarTypeServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<CarNumberTypeTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        CarNumberTypeTb carNumberTypeTb = new CarNumberTypeTb();
        carNumberTypeTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(carNumberTypeTb,reqmap);
        return result;
    }

}
