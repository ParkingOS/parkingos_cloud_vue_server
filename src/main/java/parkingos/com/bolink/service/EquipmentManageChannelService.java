package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ComPassTb;

import java.util.Map;

public interface EquipmentManageChannelService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(ComPassTb comPassTb);

    public Integer updateResultByConditions(ComPassTb comPassTb);

    public Integer removeResultByConditions(ComPassTb comPassTb);

    Long getId();

    String addChannel(String passname, String passtype, Integer monthSet, String description, Long comid, Long cameraId);

    String updateChannel(Long id, String passname, String passtype, Integer monthSet, String description, Long cameraId);
}
