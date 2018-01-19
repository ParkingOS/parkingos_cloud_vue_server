package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ComCameraTb;

import java.util.Map;

public interface EquipmentManageCameraService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(ComCameraTb comCameraTb);

    public Integer updateResultByConditions(ComCameraTb comCameraTb);

    public Integer removeResultByConditions(ComCameraTb comCameraTb);
}
