package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface WhiteListService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

    JSONObject add(String remark, String carNumber, Long btime, Long etime, Long comid, String userName, String mobile, String carLocation);

    JSONObject delete(Long aLong, String nickname, Long uin, Long id);

    JSONObject importExcel(MultipartFile file,String nickname, Long uin, Long groupid);
}
