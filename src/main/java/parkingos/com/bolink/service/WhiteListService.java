package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface WhiteListService {
    JSONObject groupQuery(Map<String, String> reqParameterMap);

    JSONObject add(String remark, String carNumber, Long btime, Long etime, Long comid, String userName, String mobile, String carLocation,String nickname,Long uin,Long groupId,int type,int endType);

    JSONObject delete(Long id, String nickname, Long uin, Long comid,Long groupid,int type);

    JSONObject importExcel(MultipartFile file,String nickname, Long uin, Long groupid);

    JSONObject edit(Long id, String remark, String carNumber, Long btime, Long etime, Long comid, String userName, String mobile, String carLocation, String nickname, Long uin, Long groupid, int i,int endType);

    JSONObject parkQuery(Map<String, String> reqParameterMap);

    List<List<Object>> groupExportExcel(Map<String, String> reqParameterMap,Long groupId,String nickName,Long uin);

    List<List<Object>> parkExportExcel(Map<String, String> reqParameterMap, Long comid, String nickname, Long uin);
}
