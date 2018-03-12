package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.UserInfoTb;

import java.util.Map;

public interface GroupMemberService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    String getRoleByConditions(Map<String, String> reqParameterMap);

    JSONObject createMember(Map<String, String> reqParameterMap);

    JSONObject updateMember(UserInfoTb userInfoTb);

    JSONObject editpass(Map<String, String> reqParameterMap);

    JSONObject delmember(Map<String, String> reqParameterMap);
}
