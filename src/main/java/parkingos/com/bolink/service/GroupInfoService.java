package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface GroupInfoService {
    String getResultByCondition(Long groupid, Long uin);

    JSONObject updateGroupInfo(HttpServletRequest request);
}
