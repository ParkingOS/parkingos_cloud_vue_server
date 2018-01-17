package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.CarTypeTb;
import parkingos.com.bolink.service.CarTypeService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class CarTypeServiceImpl implements CarTypeService {

    Logger logger = Logger.getLogger(CarTypeServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<CarTypeTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        CarTypeTb carTypeTb = new CarTypeTb();
        carTypeTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(carTypeTb,reqmap);
        return result;
    }

}
