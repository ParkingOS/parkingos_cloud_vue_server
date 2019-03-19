package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

public interface PostManageService {

    JSONObject getChannels(Long comid,Integer rp,Integer page);

    JSONObject editName(Long comid,String name,String localId);
}
