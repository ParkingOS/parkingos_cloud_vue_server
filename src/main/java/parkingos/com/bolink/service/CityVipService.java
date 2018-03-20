package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CityVipService {
    JSONObject selectResultByConditions(Map<String, String> reqParameterMap);

//    JSONObject createVip(HttpServletRequest req);

//    List<List<String>> exportExcel(Map<String, String> reqParameterMap);

    JSONObject importExcel(MultipartFile file,Long groupid,Long cityid)  throws Exception;

    List<List<String>> exportExcel(Map<String, String> reqParameterMap);
}
