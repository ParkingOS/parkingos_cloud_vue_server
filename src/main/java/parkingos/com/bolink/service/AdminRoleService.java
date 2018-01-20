package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CollectorSetTb;
import parkingos.com.bolink.models.UserRoleTb;

import java.util.Map;

public interface AdminRoleService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    JSONObject addRole(UserRoleTb userRoleTb, Integer func);

    JSONObject deleteRole(UserRoleTb userRoleTb);

    JSONObject updateRole(UserRoleTb userRoleTb, Integer func);

    JSONObject precollectset(CollectorSetTb collectorSetTb);
}
