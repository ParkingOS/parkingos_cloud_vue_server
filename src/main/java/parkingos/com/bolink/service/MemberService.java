package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.UserInfoTb;

import java.util.Map;

public interface MemberService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    String getRoleByConditions(Map<String, String> reqParameterMap);

    JSONObject createMember(Map<String, String> reqParameterMap);

    JSONObject updateMember(UserInfoTb userInfoTb);

    JSONObject editpass(Map<String, String> reqParameterMap);

    JSONObject delmember(Map<String, String> reqParameterMap);

    JSONObject isview(Map<String, String> reqParameterMap);
}
