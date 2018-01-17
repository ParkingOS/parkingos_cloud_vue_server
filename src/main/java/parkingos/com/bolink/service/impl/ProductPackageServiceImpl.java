package parkingos.com.bolink.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingos.com.bolink.dao.spring.CommonDao;
import parkingos.com.bolink.models.ProductPackageTb;
import parkingos.com.bolink.service.ProductPackageService;
import parkingos.com.bolink.service.SupperSearchService;

import java.util.Map;

@Service
public class ProductPackageServiceImpl implements ProductPackageService {

    Logger logger = Logger.getLogger(ProductPackageServiceImpl.class);

    @Autowired
    private CommonDao commonDao;
    @Autowired
    private SupperSearchService<ProductPackageTb> supperSearchService;

    @Override
    public JSONObject selectResultByConditions(Map<String, String> reqmap) {
        ProductPackageTb productPackageTb = new ProductPackageTb();
        productPackageTb.setComid(Long.parseLong(reqmap.get("comid")));
        JSONObject result = supperSearchService.supperSearch(productPackageTb,reqmap);
        return result;
    }

}
