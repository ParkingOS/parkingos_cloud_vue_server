package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.PriceTb;

import java.util.Map;

public interface PriceService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    Long getId();

    JSONObject createPrice(PriceTb priceTb);

    JSONObject updatePrice(PriceTb priceTb);

    JSONObject deletePrice(PriceTb priceTb);
}
