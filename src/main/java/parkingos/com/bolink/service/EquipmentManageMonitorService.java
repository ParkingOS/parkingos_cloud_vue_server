package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.MonitorInfoTb;

import java.util.Map;

public interface EquipmentManageMonitorService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(MonitorInfoTb monitorInfoTb);

    public Integer updateResultByConditions(MonitorInfoTb monitorInfoTb);

    public Integer removeResultByConditions(MonitorInfoTb monitorInfoTb);
}
