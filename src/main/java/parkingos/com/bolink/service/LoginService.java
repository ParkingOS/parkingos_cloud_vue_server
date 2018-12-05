package parkingos.com.bolink.service;


import com.alibaba.fastjson.JSONObject;

public interface LoginService {

    JSONObject getResultByUserNameAndPass(String userId, String cpasswd);

    JSONObject getCkey(String userId, String mobile,Integer userType);

    JSONObject regUser(String mobile, Long userid);

    JSONObject checkCode(String mobile, String code);

    JSONObject resetPwd(String passwd, Long userId);

    String sendCode(String mobile, Long userid, String ticket, String randstr,String ip);
}
