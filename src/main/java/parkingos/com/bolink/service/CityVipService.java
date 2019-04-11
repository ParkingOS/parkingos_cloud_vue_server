package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CityVipService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

//    JSONObject createVip(HttpServletRequest req);

//    List<List<String>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject importExcel(MultipartFile file,Long groupid,Long cityid,String nickname,Long uin)  throws Exception;

    List<List<String>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject createVip(Long groupId,Long comid, String carNumber, String b_time, Integer months, Integer limit_day_type, Long pid, Long carTypeId, String mobile, String name, String address, String pLot,String remark,String nickname,Long uin)  throws Exception;

    JSONObject editVip(Long groupId, Long id, String carNumber, Integer limit_day_type, String mobile, String name, String pLot,String nickname,Long uin);

    JSONObject deleteVip(Long groupId, Long id, String nickname, Long uin);
}
