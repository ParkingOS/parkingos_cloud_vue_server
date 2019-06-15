package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface UnionService {
    JSONObject getRoles(Map<String, String> paramMap);

    JSONObject addOrEditRole(Long id, Long uin, Long oid, String name, String resume, Integer state);

    JSONObject addMember(Map<String, String> reqParameterMap);

    JSONObject queryMembers(Map<String, String> reqParameterMap);

    String getAllRoles(Map<String, String> reqParameterMap);

    JSONObject editMember(String name, String mobile, String phone, Long id, Long roleId);

    JSONObject deleteMember(Long id);

    JSONObject editMemberPass(Long id, String newPass, String confirmPass);
}
