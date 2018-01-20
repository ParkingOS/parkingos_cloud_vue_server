package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.PriceTb;
import parkingos.com.bolink.service.PriceService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class PriceServiceImpl implements PriceService {

    Logger logger = Logger.getLogger(PriceServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<PriceTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        PriceTb priceTb = new PriceTb();
        priceTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(priceTb,reqmap);
        return result;
    }

}
