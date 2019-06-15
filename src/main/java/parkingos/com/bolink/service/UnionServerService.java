package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface UnionServerService {

    JSONObject addOrEdit(String name, String address, String phone, Integer state, Long parentId, Long id,Long unionId,Long cityid,String nickname,Long uin);

    JSONObject queryMembers(Map<String, String> reqParameterMap);

    String getRoles(Map<String, String> reqParameterMap);

    JSONObject addMember(String name, String phone, String mobile, Long serverId);

    JSONObject editMember(String name, String mobile, String phone, Long id,Long roleId);

    JSONObject deleteMember(Long id);

    JSONObject editMemberPass(Long id, String newPass, String confirmPass);

    JSONObject groupsByServer(Map<String, String> paramMap);

    JSONObject parksByServer(Map<String, String> paramMap);

    JSONObject childServer(Map<String, String> paramMap);

    JSONObject getRolesByServer(Map<String, String> paramMap);

    JSONObject addOrEditRole(Long id, Long uin, Long oid, String name, String resume, Integer state);

    JSONObject addMemberFromServer(Map<String, String> reqParameterMap);
}
