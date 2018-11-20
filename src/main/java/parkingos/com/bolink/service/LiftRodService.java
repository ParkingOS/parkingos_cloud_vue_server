package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public interface LiftRodService {
    public JSONObject selectResultByConditions(Map<String, String> map);

    String getLiftRodPicture(String comid, String liftrodId,HttpServletResponse response) throws Exception;

    List<List<Object>> exportExcel(Map<String, String> reqParameterMap);

    Object getLiftReason(int type);

    String getComidByLift(long liftId);
}
