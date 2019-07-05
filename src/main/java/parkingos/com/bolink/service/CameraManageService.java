package parkingos.com.bolink.service;

import com.alibaba.fastjson.JSONObject;
import parkingos.com.bolink.models.CameraTb;

import java.util.List;

public interface CameraManageService {
    List<CameraTb> adminManage(Long cityid, Long comid);

    JSONObject bindPark(Long comid, Long cityid, Long id);

    List<CameraTb> parkQuery(Long comid, String camName, String camId);

    JSONObject editName(Long id, String camName);
}
