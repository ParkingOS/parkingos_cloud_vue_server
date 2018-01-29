package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.PhoneInfoTb;

import java.util.Map;

public interface EquipmentManageIntercomService {
    public JSONObject selectResultByConditions(Map<String, String> map);

   /* public JSONObject findMonitorByConditions(Map<String, String> map);*/

    public Integer insertResultByConditions(PhoneInfoTb phoneInfoTb);

    public Integer updateResultByConditions(PhoneInfoTb phoneInfoTb);

    public Integer removeResultByConditions(PhoneInfoTb phoneInfoTb);
}
