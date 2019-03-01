package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

public interface ChargingService {

    JSONObject addJs(String name, String remark, String js,Long comid);

    JSONObject editParkCharging(Long id, String name, String remark, String jsContent);

    JSONObject deleteParkCharging(Long id);

    String testJs(Long id,Long createTime,Long endTime);

    JSONObject openOrClose(Long id, Integer isOpen);

    JSONObject cloudFirst(Long comid, Integer cloudFirst);
}
