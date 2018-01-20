package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ProductPackageTb;

import java.util.Map;

public interface ProductPackageService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    Long getId();

    JSONObject createProduct(ProductPackageTb productPackageTb);

    JSONObject deleteProduct(Long id, Long comid);

    JSONObject editProduct(Map<String, String> reqParameterMap);
}
