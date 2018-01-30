package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

public interface ParkOrderAnlysisService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    JSONObject selectWorkdetail(String bt, String et, String fieldsstr, String pay_type, String type, Long uid, Long comid,String date);

    JSONObject getOrderdetail(Long uid, String btime, String etime, String type, Long comid);
}
