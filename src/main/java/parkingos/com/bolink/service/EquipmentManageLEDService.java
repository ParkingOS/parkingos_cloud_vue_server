package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ComLedTb;

import java.util.Map;

public interface EquipmentManageLEDService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(ComLedTb comLedTb);

    public Integer updateResultByConditions(ComLedTb comLedTb);

    public Integer removeResultByConditions(ComLedTb comLedTb);
}
