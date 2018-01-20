package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CarTypeTb;

import java.util.Map;

public interface CarTypeService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    Long getId();

    JSONObject createCarType(CarTypeTb carTypeTb);

    JSONObject updateCarType(CarTypeTb carTypeTb);

    JSONObject deleteCarType(CarTypeTb carTypeTb);
}
