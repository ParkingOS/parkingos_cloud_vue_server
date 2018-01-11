package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;

public interface LoginService {

    JSONObject getResultByUserNameAndPass(String userId, String cpasswd);
}
