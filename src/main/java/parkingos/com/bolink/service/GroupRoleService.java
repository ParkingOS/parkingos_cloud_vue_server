package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.UserRoleTb;

import java.util.Map;

public interface GroupRoleService {

    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject addRole(UserRoleTb userRoleTb, Integer func);

    JSONObject updateRole(UserRoleTb userRoleTb, Integer func);

    JSONObject deleteRole(UserRoleTb userRoleTb);

    String getAuth(Long loginRoleId, Long id);

    JSONObject editRoleAuth(Long id, String auths);
}
