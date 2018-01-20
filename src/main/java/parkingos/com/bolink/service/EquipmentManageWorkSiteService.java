package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.ComWorksiteTb;

import java.util.Map;

public interface EquipmentManageWorkSiteService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    public Integer insertResultByConditions(ComWorksiteTb comWorksiteTb);

    public Integer updateResultByConditions(ComWorksiteTb comWorksiteTb);

    public Integer removeResultByConditions(ComWorksiteTb comWorksiteTb);
}
