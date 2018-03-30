package parkingos.com.bolink.service;

import java.util.HashMap;
import java.util.List;

/**
 * 获取车场情况数据
 */
public interface GetParkInfoService {
    String getInfo(int groupid );
    String getInfoByComid(int comid);

}
